package com.yfletch.autoflicker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.Prayer;

public enum PrayerType
{
	DEFENCE,
	STRENGTH,
	ATTACK,
	RANGED,
	MAGIC,
	REGEN,
	OVERHEAD;

	private static final Map<Prayer, List<PrayerType>> TYPES = new HashMap<>();

	static
	{
		TYPES.put(Prayer.THICK_SKIN, List.of(DEFENCE));
		TYPES.put(Prayer.BURST_OF_STRENGTH, List.of(STRENGTH));
		TYPES.put(Prayer.CLARITY_OF_THOUGHT, List.of(ATTACK));
		TYPES.put(Prayer.SHARP_EYE, List.of(RANGED));
		TYPES.put(Prayer.MYSTIC_WILL, List.of(MAGIC));
		TYPES.put(Prayer.ROCK_SKIN, List.of(DEFENCE));
		TYPES.put(Prayer.SUPERHUMAN_STRENGTH, List.of(STRENGTH));
		TYPES.put(Prayer.IMPROVED_REFLEXES, List.of(ATTACK));
		TYPES.put(Prayer.RAPID_RESTORE, List.of(REGEN));
		TYPES.put(Prayer.RAPID_HEAL, List.of(REGEN));
		TYPES.put(Prayer.PROTECT_ITEM, List.of());
		TYPES.put(Prayer.HAWK_EYE, List.of(RANGED));
		TYPES.put(Prayer.MYSTIC_LORE, List.of(MAGIC));
		TYPES.put(Prayer.STEEL_SKIN, List.of(DEFENCE));
		TYPES.put(Prayer.ULTIMATE_STRENGTH, List.of(STRENGTH));
		TYPES.put(Prayer.INCREDIBLE_REFLEXES, List.of(ATTACK));
		TYPES.put(Prayer.PROTECT_FROM_MAGIC, List.of(OVERHEAD));
		TYPES.put(Prayer.PROTECT_FROM_MISSILES, List.of(OVERHEAD));
		TYPES.put(Prayer.PROTECT_FROM_MELEE, List.of(OVERHEAD));
		TYPES.put(Prayer.EAGLE_EYE, List.of(RANGED));
		TYPES.put(Prayer.MYSTIC_MIGHT, List.of(MAGIC));
		TYPES.put(Prayer.RETRIBUTION, List.of(OVERHEAD));
		TYPES.put(Prayer.REDEMPTION, List.of(OVERHEAD));
		TYPES.put(Prayer.SMITE, List.of(OVERHEAD));
		TYPES.put(Prayer.PRESERVE, List.of());
		TYPES.put(Prayer.CHIVALRY, allCombat());
		TYPES.put(Prayer.PIETY, allCombat());
		TYPES.put(Prayer.RIGOUR, allCombat());
		TYPES.put(Prayer.AUGURY, allCombat());
	}

	public static List<PrayerType> allCombat()
	{
		return List.of(
			DEFENCE,
			STRENGTH,
			ATTACK,
			RANGED,
			MAGIC
		);
	}

	public static boolean areIncompatible(Prayer p1, Prayer p2)
	{
		final var p1Types = TYPES.get(p1);
		final var p2Types = TYPES.get(p2);
		for (final var type : p1Types)
		{
			if (p2Types.contains(type))
			{
				return true;
			}
		}

		return false;
	}
}
