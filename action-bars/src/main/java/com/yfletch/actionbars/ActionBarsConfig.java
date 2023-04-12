package com.yfletch.actionbars;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(ActionBarsConfig.CONFIG_GROUP)
public interface ActionBarsConfig extends Config
{
	String CONFIG_GROUP = "action-bars";
	String CONFIG_STORAGE = "storage";
}
