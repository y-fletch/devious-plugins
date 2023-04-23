package com.yfletch.autoflicker.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;

@Singleton
public class PrayerHelper
{
	private static final int PRAYER_CONTAINER = 35454979;

	@Inject
	private Client client;

	public Prayer getPrayer(Widget widget)
	{
		if (widget.getName().equals("Quick-prayers"))
		{
			return null;
		}

		return getPrayer(widget.getName());
	}

	public Prayer getPrayer(String name)
	{
		final var enumName = Text.removeTags(name)
			.replaceAll(" ", "_")
			.toUpperCase();
		return Prayer.valueOf(enumName);
	}

	public Widget getWidget(Prayer prayer)
	{
		return client.getWidget(prayer.getWidgetInfo());
	}
}
