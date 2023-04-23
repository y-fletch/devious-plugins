package com.yfletch.autoflicker.switchers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.switchers.bosses.AutoSwitchRule;
import com.yfletch.autoflicker.switchers.bosses.KingBlackDragon;
import com.yfletch.autoflicker.switchers.bosses.Vorkath;
import com.yfletch.autoflicker.switchers.bosses.Zebak;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Prayer;

@Slf4j
@Singleton
public class DefensiveSwitcher implements PrayerSwitcher
{
	@Inject private Vorkath vorkath;
	@Inject private KingBlackDragon kingBlackDragon;
	@Inject private Zebak zebak;

	@Getter
	private List<Prayer> previousPrayers;

	private List<AutoSwitchRule> getEnabledRules()
	{
		return Stream.of(vorkath, kingBlackDragon, zebak)
			.filter(AutoSwitchRule::isEnabled)
			.collect(Collectors.toList());
	}

	@Override
	public List<Prayer> getPrayers()
	{
		for (final var rule : getEnabledRules())
		{
			final var prayer = rule.getPrayer();
			if (prayer != null)
			{
				// set previous prayers here - so if, on the next
				// tick, there's no matching prayers, it will be used
				// to disable this prayer
				previousPrayers = List.of(prayer);
				return List.of(prayer);
			}
		}

		return null;
	}

	@Override
	public void clearPreviousPrayers()
	{
		previousPrayers = null;
	}
}
