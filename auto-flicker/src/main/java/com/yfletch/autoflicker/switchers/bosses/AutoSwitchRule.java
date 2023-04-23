package com.yfletch.autoflicker.switchers.bosses;

import net.runelite.api.Prayer;

public interface AutoSwitchRule
{
	boolean isEnabled();

	Prayer getPrayer();
}
