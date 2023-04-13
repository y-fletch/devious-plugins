package com.yfletch.actionbars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.MenuEntry;
import net.runelite.api.util.Text;
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

	private int itemId;

	public int getWidgetPackedId()
	{
		return param1;
	}

	public static SavedAction fromMenuEntry(MenuEntry menuEntry)
	{
		return new SavedAction(
			menuEntry.getOption(),
			menuEntry.getTarget(),
			menuEntry.getIdentifier(),
			menuEntry.getOpcode(),
			menuEntry.getParam0(),
			menuEntry.getParam1(),
			menuEntry.getItemId()
		);
	}

	public String getName()
	{
		return Text.removeTags(getOption() + " " + getTarget());
	}

	public boolean isItemAction()
	{
		return itemId > -1;
	}

	public int getInventoryIndex()
	{
		// find next in inventory
		final var inventory = Static.getClient().getItemContainer(InventoryID.INVENTORY);
		if (inventory == null || !inventory.contains(itemId))
		{
			return -1;
		}

		for (var i = 0; i < inventory.size(); i++)
		{
			final var item = inventory.getItem(i);
			if (item != null && item.getId() == itemId)
			{
				return i;
			}
		}

		return param0;
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
				isItemAction() ? getInventoryIndex() : param0,
				param1
			);
		});
	}
}
