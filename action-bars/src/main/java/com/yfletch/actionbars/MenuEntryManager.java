package com.yfletch.actionbars;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.actionbars.bar.ActionBarManager;
import java.awt.Color;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.client.util.ColorUtil;

@Singleton
public class MenuEntryManager
{
	@Inject
	private Client client;

	@Inject
	private ActionBarManager actionBarManager;

	@Setter
	private Action hoveredAction;

	@Setter
	private boolean hasCreatedEntries = false;

	public void createMenuEntries()
	{
		if (hoveredAction == null || hasCreatedEntries || client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}

		final var inner = hoveredAction.getSavedAction();
		final var colored = ColorUtil.wrapWithColorTag(
			inner.getOption() + " " + inner.getTarget(),
			Color.WHITE
		);
		final var actionBar = actionBarManager.getActiveBar();

		client.createMenuEntry(-1)
			.setOption("Swap right")
			.setTarget(colored)
			.onClick(e -> actionBar.swapRight(hoveredAction));

		client.createMenuEntry(-1)
			.setOption("Swap left")
			.setTarget(colored)
			.onClick(e -> actionBar.swapLeft(hoveredAction));

		client.createMenuEntry(-1)
			.setOption("Unbind")
			.setTarget(colored)
			.onClick(e -> actionBar.remove(hoveredAction));

		client.createMenuEntry(-1)
			.setOption("Activate")
			.setTarget(colored)
			.onClick(e -> inner.invoke());

		hasCreatedEntries = true;
	}
}
