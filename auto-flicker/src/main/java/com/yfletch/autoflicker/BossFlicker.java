package com.yfletch.autoflicker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.bosses.AutoFlickRule;
import com.yfletch.autoflicker.bosses.KingBlackDragon;
import com.yfletch.autoflicker.bosses.Vorkath;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Prayer;

@Slf4j
@Singleton
public class BossFlicker
{
	@Inject private Vorkath vorkath;
	@Inject private KingBlackDragon kingBlackDragon;

	private List<AutoFlickRule> getEnabledRules()
	{
		return Stream.of(vorkath, kingBlackDragon)
			.filter(AutoFlickRule::isEnabled)
			.collect(Collectors.toList());
	}

	public Prayer getPrayer()
	{
		for (final var rule : getEnabledRules())
		{
			final var activePrayers = Map.of(
				Prayer.PROTECT_FROM_MAGIC, rule.shouldProtectMagic(),
				Prayer.PROTECT_FROM_MELEE, rule.shouldProtectMelee(),
				Prayer.PROTECT_FROM_MISSILES, rule.shouldProtectMissiles(),
				Prayer.SMITE, rule.shouldSmite()
			);

			for (final var prayer : rule.getPriority())
			{
				if (activePrayers.getOrDefault(prayer, false))
				{
					return prayer;
				}
			}
		}

		return null;
	}
}
