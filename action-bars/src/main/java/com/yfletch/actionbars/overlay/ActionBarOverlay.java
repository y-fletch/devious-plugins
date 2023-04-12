package com.yfletch.actionbars.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.actionbars.bar.ActionBarManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class ActionBarOverlay extends OverlayPanel
{
	@Inject
	private ActionBarManager actionBarManager;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var activeBar = actionBarManager.getActiveBar();

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Action bar")
				.leftColor(Color.CYAN)
				.right("" + (actionBarManager.getActiveBarIndex() + 1))
				.build()
		);

		panelComponent.getChildren().add(
			ActionBarComponent.builder()
				.actionBar(activeBar)
				.build()
		);

		return super.render(graphics);
	}
}
