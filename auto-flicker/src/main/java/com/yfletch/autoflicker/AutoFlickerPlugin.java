package com.yfletch.autoflicker;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.autoflicker.switchers.DefensiveSwitcher;
import com.yfletch.autoflicker.switchers.OffensiveSwitcher;
import com.yfletch.autoflicker.util.PrayerHelper;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.SoundEffectID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.SoundEffectPlayed;
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

	@Inject private Client client;
	@Inject private ClientThread clientThread;

	@Inject private PrayerHelper prayerHelper;
	@Inject private PrayerFlicker prayerFlicker;
	@Inject private DefensiveSwitcher defensiveSwitcher;
	@Inject private OffensiveSwitcher offensiveSwitcher;

	@Inject private AutoFlickerConfig config;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		// apply boss/weapon flicks here
		final var defensivePrayers = defensiveSwitcher.getPrayers();
		if (defensivePrayers != null)
		{
			prayerFlicker.activate(defensivePrayers);
		}
		else
		{
			// attempt to disable the old prayers
			final var previousDefensivePrayers = defensiveSwitcher.getPreviousPrayers();
			if (previousDefensivePrayers != null)
			{
				prayerFlicker.deactivate(previousDefensivePrayers);
				defensiveSwitcher.clearPreviousPrayers();
			}
		}

		final var offensivePrayers = offensiveSwitcher.getPrayers();
		if (offensivePrayers != null)
		{
			prayerFlicker.activate(offensivePrayers);
		}
		else
		{
			// attempt to disable the old prayers
			final var previousOffensivePrayers = offensiveSwitcher.getPreviousPrayers();
			if (previousOffensivePrayers != null)
			{
				prayerFlicker.deactivate(previousOffensivePrayers);
				offensiveSwitcher.clearPreviousPrayers();
			}
		}

		prayerFlicker.flickAll();
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final var menuEntry = event.getMenuEntry();

		if (menuEntry.getOption().equals("Activate")
			|| menuEntry.getOption().equals("Deactivate"))
		{
			final var widget = menuEntry.getWidget();
			if (widget == null) return;

			final var prayer = prayerHelper.getPrayer(widget);
			if (prayer == null) return;

			final var isFlicking = prayerFlicker.isFlicking(prayer);

			client.createMenuEntry(-1)
				.setOption(isFlicking ? STOP_FLICK : FLICK)
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e -> {
					if (isFlicking)
					{
						prayerFlicker.deactivate(prayer);
						return;
					}

					prayerFlicker.activate(prayer);
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

	@Provides
	public AutoFlickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoFlickerConfig.class);
	}
}
