package com.yfletch.autoswitcher;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("auto-switcher")
public interface AutoSwitcherConfig extends Config
{
	@ConfigItem(
		name = "Enabled",
		description = "Enabled",
		keyName = "enabled"
	)
	default boolean enabled()
	{
		return false;
	}

	@ConfigItem(
		name = "Sleep",
		description = "Time to sleep between equipping items (ms)",
		keyName = "sleep"
	)
	default long sleep()
	{
		return 50;
	}

	@ConfigSection(
		name = "Switches",
		description = "",
		position = 1
	)
	String switches = "switches";

	@ConfigItem(
		name = "Trigger 1",
		description = "Trigger 'Switch 1' when this item is clicked",
		keyName = "trigger1",
		section = switches,
		position = 0
	)
	default String trigger1()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Switch 1",
		description = "Equip all of these items when 'Trigger 1' is clicked",
		keyName = "switch1",
		section = switches,
		position = 1
	)
	default String switch1()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Trigger 2",
		description = "Trigger 'Switch 2' when this item is clicked",
		keyName = "trigger2",
		section = switches,
		position = 2
	)
	default String trigger2()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Switch 2",
		description = "Equip all of these items when 'Trigger 2' is clicked",
		keyName = "switch2",
		section = switches,
		position = 3
	)
	default String switch2()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Trigger 3",
		description = "Trigger 'Switch 3' when this item is clicked",
		keyName = "trigger3",
		section = switches,
		position = 4
	)
	default String trigger3()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Switch 3",
		description = "Equip all of these items when 'Trigger 3' is clicked",
		keyName = "switch3",
		section = switches,
		position = 5
	)
	default String switch3()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Trigger 4",
		description = "Trigger 'Switch 4' when this item is clicked",
		keyName = "trigger4",
		section = switches,
		position = 6
	)
	default String trigger4()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Switch 4",
		description = "Equip all of these items when 'Trigger 4' is clicked",
		keyName = "switch4",
		section = switches,
		position = 7
	)
	default String switch4()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Trigger 5",
		description = "Trigger 'Switch 5' when this item is clicked",
		keyName = "trigger5",
		section = switches,
		position = 8
	)
	default String trigger5()
	{
		return "-1";
	}

	@ConfigItem(
		name = "Switch 5",
		description = "Equip all of these items when 'Trigger 5' is clicked",
		keyName = "switch5",
		section = switches,
		position = 9
	)
	default String switch5()
	{
		return "-1";
	}
}
