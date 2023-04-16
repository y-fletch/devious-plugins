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
}
