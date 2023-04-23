package com.yfletch.autoflicker.switchers;

import java.util.List;
import net.runelite.api.Prayer;

public interface PrayerSwitcher
{
	List<Prayer> getPrayers();

	List<Prayer> getPreviousPrayers();

	void clearPreviousPrayers();
}
