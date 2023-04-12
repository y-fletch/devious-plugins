package com.yfletch.actionbars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.unethicalite.client.Static;

@Getter
@AllArgsConstructor
@Slf4j
public class SavedAction
{
	private String option;
	private String target;
	private int identifier;
	private int opcode;
	private int param0;
	private int param1;

	public static SavedAction fromMenuEntry(MenuEntry menuEntry)
	{
		return new SavedAction(
			menuEntry.getOption(),
			menuEntry.getTarget(),
			menuEntry.getIdentifier(),
			menuEntry.getOpcode(),
			menuEntry.getParam0(),
			menuEntry.getParam1()
		);
	}

	// TODO:
	// Item actions - do first in inventory (allow bank/player inv)
	// Extract widget/draw on overlay

	public String getName()
	{
		return (getOption() + " " + getTarget())
			.replaceAll("</?col(?:=.{3,6})?>", "");
	}

	public void invoke()
	{
		log.info("Invoke " + getName());

		Static.getClientThread().invokeLater(() -> {
			Static.getClient().invokeMenuAction(
				option,
				target,
				identifier,
				opcode,
				param0,
				param1
			);
		});
	}
}
