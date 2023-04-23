package com.yfletch.autoflicker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.util.PrayerHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;

@Slf4j
@Singleton
public class PrayerFlicker
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;
	@Inject private AutoFlickerConfig config;
	@Inject private PrayerHelper prayerHelper;

	private final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private final List<Prayer> activePrayers = new ArrayList<>();

	/**
	 * Flick all active prayers off then on (1t flick)
	 */
	public void flickAll()
	{
		if (!config.enableFlicking() || activePrayers.isEmpty()) return;

		// to successfully flick, a prayer needs to be deactivated
		// and reactivated in the same tick
		EXECUTOR.execute(() -> {
			// pre-wait before starting to flick - just in case
			// the boss/weapon flicker has changed the active prayers.
			// we don't want to click the same widget twice in one instant
			sleep(50);

			// deactivate currently active prayers
			clickAllActive();

			// wait a lil bit, then activate all prayers again
			sleep(200);
			clickAllInactive();
		});
	}

	/**
	 * Activate the target prayer, disabling any conflicting prayers.
	 * If flicking is enabled, the prayer will continue to be 1t flicked.
	 */
	public void activate(Prayer prayer)
	{
		if (activePrayers.contains(prayer)) return;

		log.info("Activating prayer " + prayer.name());

		removeIncompatiblePrayers(prayer);
		activePrayers.add(prayer);
		if (!isActive(prayer)) click(prayer);
	}

	/**
	 * Activate the target prayers, disabling any conflicting prayers.
	 * If flicking is enabled, the prayer will continue to be 1t flicked.
	 */
	public void activate(List<Prayer> prayers)
	{
		prayers.forEach(this::activate);
	}

	/**
	 * Deactivate the target prayer
	 */
	public void deactivate(Prayer prayer)
	{
		if (!activePrayers.contains(prayer)) return;

		log.info("Deactivating prayer " + prayer.name());

		activePrayers.remove(prayer);
		if (isActive(prayer)) click(prayer);
	}

	/**
	 * Deactivate the target prayers
	 */
	public void deactivate(List<Prayer> prayers)
	{
		prayers.forEach(this::deactivate);
	}

	/**
	 * Check if the plugin is currently flicking the prayer
	 */
	public boolean isFlicking(Prayer prayer)
	{
		return activePrayers.contains(prayer);
	}

	private void removeIncompatiblePrayers(Prayer prayer)
	{
		activePrayers.removeIf(otherPrayer -> prayer != otherPrayer
			&& PrayerType.areIncompatible(prayer, otherPrayer));
	}

	/**
	 * Click the prayer widget to enable or disable it
	 */
	private void click(Prayer prayer)
	{
		final var widget = prayerHelper.getWidget(prayer);
		if (widget == null) return;

		clientThread.invokeLater(
			() -> client.invokeMenuAction(
				isActive(widget) ? "Deactivate" : "Activate",
				widget.getName(),
				1,
				MenuAction.CC_OP.getId(),
				-1,
				widget.getId()
			)
		);
	}

	private void clickAllInactive()
	{
		activePrayers.stream()
			.filter(prayer -> !isActive(prayer))
			.forEach(this::click);
	}

	private void clickAllActive()
	{
		activePrayers.stream()
			.filter(this::isActive)
			.forEach(this::click);
	}

	private boolean isActive(Widget widget)
	{
		final var actions = widget.getActions();
		return actions != null && Arrays.asList(actions).contains("Deactivate");
	}

	private boolean isActive(Prayer prayer)
	{
		final var widget = prayerHelper.getWidget(prayer);
		return widget != null && isActive(widget);
	}

	private void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException ignored)
		{
		}
	}
}
