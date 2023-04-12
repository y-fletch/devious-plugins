package com.yfletch.autoflicker;

import com.google.inject.Inject;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Prayer;
import net.runelite.api.SoundEffectID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "Auto Flicker",
	description = "Auto flick any prayer"
)
public class AutoFlickerPlugin extends Plugin
{
	private final static String FLICK = "Flick";
	private final static String STOP_FLICK = "Stop flicking";

	private final static List<Integer> PRAYER_SOUNDS = List.of(
		SoundEffectID.PRAYER_ACTIVATE_THICK_SKIN,
		SoundEffectID.PRAYER_ACTIVATE_BURST_OF_STRENGTH,
		SoundEffectID.PRAYER_ACTIVATE_CLARITY_OF_THOUGHT,
		SoundEffectID.PRAYER_ACTIVATE_SHARP_EYE_RIGOUR,
		SoundEffectID.PRAYER_ACTIVATE_MYSTIC_WILL_AUGURY,
		SoundEffectID.PRAYER_ACTIVATE_ROCK_SKIN,
		SoundEffectID.PRAYER_ACTIVATE_SUPERHUMAN_STRENGTH,
		SoundEffectID.PRAYER_ACTIVATE_IMPROVED_REFLEXES,
		SoundEffectID.PRAYER_ACTIVATE_RAPID_RESTORE_PRESERVE,
		SoundEffectID.PRAYER_ACTIVATE_RAPID_HEAL,
		SoundEffectID.PRAYER_ACTIVATE_PROTECT_ITEM,
		SoundEffectID.PRAYER_ACTIVATE_HAWK_EYE,
		SoundEffectID.PRAYER_ACTIVATE_MYSTIC_LORE,
		SoundEffectID.PRAYER_ACTIVATE_STEEL_SKIN,
		SoundEffectID.PRAYER_ACTIVATE_ULTIMATE_STRENGTH,
		SoundEffectID.PRAYER_ACTIVATE_INCREDIBLE_REFLEXES,
		SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MAGIC,
		SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MISSILES,
		SoundEffectID.PRAYER_ACTIVATE_PROTECT_FROM_MELEE,
		SoundEffectID.PRAYER_ACTIVATE_EAGLE_EYE,
		SoundEffectID.PRAYER_ACTIVATE_MYSTIC_MIGHT,
		SoundEffectID.PRAYER_ACTIVATE_RETRIBUTION,
		SoundEffectID.PRAYER_ACTIVATE_REDEMPTION,
		SoundEffectID.PRAYER_ACTIVATE_SMITE,
		SoundEffectID.PRAYER_ACTIVATE_CHIVALRY,
		SoundEffectID.PRAYER_ACTIVATE_PIETY,
		SoundEffectID.PRAYER_DEACTIVE_VWOOP
	);

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private AutoFlickerConfig config;

	private final List<Widget> flickWidgets = new ArrayList<>();
	private final Executor DEACTIVATE_EXECUTOR = Executors.newSingleThreadExecutor();

	@Subscribe
	public void onGameTick(GameTick event)
	{
		// activate any inactive prayers
		clientThread.invokeLater(this::clickInactiveWidgets);

		DEACTIVATE_EXECUTOR.execute(() -> {
			try
			{
				Thread.sleep(50);

				// deactivate
				clientThread.invokeLater(this::clickWidgets);

				Thread.sleep(200);

				// activate
				clientThread.invokeLater(this::clickWidgets);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final var menuEntry = event.getMenuEntry();

		if (menuEntry.getOption().equals("Activate")
			|| menuEntry.getOption().equals("Deactivate"))
		{
			final var isActivated = menuEntry.getOption().equals("Deactivate");
			final var widget = event.getMenuEntry().getWidget();
			if (widget == null)
			{
				return;
			}

			final var enabled = flickWidgets.contains(widget);

			client.createMenuEntry(-1)
				.setOption(enabled ? STOP_FLICK : FLICK)
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e -> {
					log.info("Auto flick - " + (enabled ? "disabled: " : "enabled: ") + menuEntry.getTarget());

					if (enabled)
					{
						flickWidgets.remove(widget);
						if (isActivated)
						{
							click(widget);
						}
						return;
					}

					flickWidgets.add(widget);
					disableIncompatiblePrayers(widget);
				});
		}
	}

	@Subscribe
	public void onPostMenuSort(PostMenuSort event)
	{
		if (config.firstOption())
		{
			final var menuEntries = client.getMenuEntries();
			final var lastEntry = menuEntries[menuEntries.length - 1];

			if (
				lastEntry.getOption().equals("Activate")
					|| lastEntry.getOption().equals("Deactivate")
			)
			{
				// find flick menu entry
				final var flickEntryIndex = IntStream.range(0, menuEntries.length)
					.filter(i -> menuEntries[i].getOption().equals(FLICK)
						|| menuEntries[i].getOption().equals(STOP_FLICK))
					.findFirst().orElse(-1);

				if (flickEntryIndex > 0)
				{
					menuEntries[menuEntries.length - 1] = menuEntries[flickEntryIndex];
					menuEntries[flickEntryIndex] = lastEntry;
					client.setMenuEntries(menuEntries);
				}
			}
		}
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		if (config.disableSounds())
		{
			if (event.getSource() == null && PRAYER_SOUNDS.contains(event.getSoundId()))
			{
				event.consume();
			}
		}
	}

	private void click(Widget widget)
	{
		client.invokeMenuAction(
			"Activate",
			widget.getName(),
			1,
			MenuAction.CC_OP.getId(),
			-1,
			widget.getId()
		);
	}

	private void clickInactiveWidgets()
	{
		for (final var widget : flickWidgets)
		{
			if (!isActive(widget))
			{
				click(widget);
			}
		}
	}

	private void clickWidgets()
	{
		for (final var widget : flickWidgets)
		{
			click(widget);
		}
	}

	private Prayer getPrayerForWidget(Widget widget)
	{
		if (widget.getName().equals("Quick-prayers"))
		{
			return null;
		}

		final var enumName = Text.removeTags(widget.getName())
			.replaceAll(" ", "_")
			.toUpperCase();
		return Prayer.valueOf(enumName);
	}

	private void disableIncompatiblePrayers(Widget widget)
	{
		final var prayer = getPrayerForWidget(widget);
		if (prayer == null) return;

		for (final var otherWidget : new ArrayList<>(flickWidgets))
		{
			final var otherPrayer = getPrayerForWidget(otherWidget);
			if (otherPrayer != null
				&& prayer != otherPrayer
				&& PrayerType.areIncompatible(prayer, otherPrayer))
			{
				flickWidgets.remove(otherWidget);
				if (isActive(otherWidget))
				{
					click(otherWidget);
				}
			}
		}
	}

	private boolean isActive(Widget widget)
	{
		final var actions = widget.getActions();
		return actions != null && Arrays.asList(actions).contains("Deactivate");
	}

	@Provides
	public AutoFlickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoFlickerConfig.class);
	}
}
