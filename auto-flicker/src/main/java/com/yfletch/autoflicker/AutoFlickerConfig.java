package com.yfletch.autoflicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

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
}
