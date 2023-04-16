package com.yfletch.autoflicker.bosses;

import net.runelite.api.Prayer;

public interface AutoFlickRule
{
	boolean isEnabled();

	Prayer[] getPriority();

	boolean shouldProtectMelee();

	boolean shouldProtectMagic();

	boolean shouldProtectMissiles();

	boolean shouldSmite();
}
