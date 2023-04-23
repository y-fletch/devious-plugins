package com.yfletch.autoflicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("auto-flicker")
public interface AutoFlickerConfig extends Config
{
	@ConfigItem(
		name = "Disable prayer sounds",
		description = "Disable all prayer sounds",
		keyName = "disableSounds"
	)
	default boolean disableSounds()
	{
		return false;
	}

	@ConfigItem(
		name = "Flick as default option",
		description = "Set flick as the first option for prayers",
		keyName = "firstOption"
	)
	default boolean firstOption()
	{
		return false;
	}

	@ConfigSection(
		name = "Auto-flick offensives",
		description = "",
		position = 2
	)
	String autoFlickOffensives = "autoFlickOffensives";

	@ConfigItem(
		name = "Enabled",
		description = "",
		keyName = "flickOffensivePrayers",
		section = autoFlickOffensives
	)
	default boolean flickOffensivePrayers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "meleeWeapons",
		name = "Melee weapons",
		description = "Flick melee offensive prayer(s) when one of these weapons is equipped.",
		section = autoFlickOffensives,
		position = 0
	)
	default String meleeWeapons()
	{
		return "-1";
	}

	@ConfigItem(
		keyName = "meleePrayers",
		name = "Melee prayers",
		description = "Melee prayers to flick - use the prayer enum name",
		section = autoFlickOffensives,
		position = 1
	)
	default String meleePrayers()
	{
		return "PIETY";
	}

	@ConfigItem(
		keyName = "rangedWeapons",
		name = "Ranged weapons",
		description = "Flick ranged offensive prayer(s) when one of these weapons is equipped.",
		section = autoFlickOffensives,
		position = 2
	)
	default String rangedWeapons()
	{
		return "-1";
	}

	@ConfigItem(
		keyName = "rangedPrayers",
		name = "Ranged prayers",
		description = "Ranged prayers to flick - use the prayer enum name",
		section = autoFlickOffensives,
		position = 3
	)
	default String rangedPrayers()
	{
		return "RIGOUR";
	}

	@ConfigItem(
		keyName = "magicWeapons",
		name = "Magic weapons",
		description = "Flick magic offensive prayer(s) when one of these weapons is equipped.",
		section = autoFlickOffensives,
		position = 4
	)
	default String magicWeapons()
	{
		return "-1";
	}

	@ConfigItem(
		keyName = "magicPrayers",
		name = "Magic prayers",
		description = "Magic prayers to flick - use the prayer enum name",
		section = autoFlickOffensives,
		position = 5
	)
	default String magicPrayers()
	{
		return "AUGURY";
	}

	@ConfigSection(
		name = "Auto-flick bosses",
		description = "",
		position = 3
	)
	String autoFlickBosses = "autoFlickBosses";

	@ConfigItem(
		keyName = "onlySwitch",
		name = "Only switch",
		description = "Don't attempt to flick bosses, just switch prayers as needed. "
			+ "Some bosses attack 1 tick after an animation, and flicking will not protect "
			+ "the first tick after the plugin switches.",
		section = autoFlickBosses,
		position = 0
	)
	default boolean onlySwitch()
	{
		return true;
	}

	@ConfigItem(
		keyName = "flickVorkath",
		name = "Vorkath",
		description = "Vorkath",
		section = autoFlickBosses,
		position = 1
	)
	default boolean flickVorkath()
	{
		return false;
	}

	@ConfigItem(
		keyName = "flickKBD",
		name = "King Black Dragon",
		description = "King Black Dragon",
		section = autoFlickBosses,
		position = 1
	)
	default boolean flickKBD()
	{
		return false;
	}

	@ConfigItem(
		keyName = "flickZebak",
		name = "Zebak",
		description = "Zebak",
		section = autoFlickBosses,
		position = 1
	)
	default boolean flickZebak()
	{
		return false;
	}
}
