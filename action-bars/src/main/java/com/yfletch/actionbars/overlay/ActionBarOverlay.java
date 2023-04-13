package com.yfletch.actionbars.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.actionbars.MenuEntryManager;
import com.yfletch.actionbars.bar.ActionBarManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Constants;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.unethicalite.client.Static;

@Singleton
public class ActionBarOverlay extends OverlayPanel
{
	private static final ImageComponent PLACEHOLDER_IMAGE = new ImageComponent(
		new BufferedImage(Constants.ITEM_SPRITE_WIDTH, Constants.ITEM_SPRITE_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR));

	private final ActionBarManager actionBarManager;
	private final MenuEntryManager menuEntryManager;
	private List<ActionButtonComponent> actionButtons;

	@Inject
	private ActionBarOverlay(ActionBarManager actionBarManager, MenuEntryManager menuEntryManager)
	{
		setPosition(OverlayPosition.BOTTOM_LEFT);
		panelComponent.setWrap(true);
		panelComponent.setGap(new Point(6, 4));
		panelComponent.setPreferredSize(new Dimension(10 * (Constants.ITEM_SPRITE_WIDTH + 6), 0));
		panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);

		final var barId = actionBarManager.getActiveBarIndex() + 1;
		addMenuEntry(
			MenuAction.RUNELITE,
			"Clear",
			"Action bar " + barId,
			e -> actionBarManager.clearCurrentActionBar()
		);
		addMenuEntry(
			MenuAction.RUNELITE,
			"Next",
			"Action bar " + barId,
			e -> actionBarManager.nextActionBar()
		);
		addMenuEntry(
			MenuAction.RUNELITE,
			"Previous",
			"Action bar " + barId,
			e -> actionBarManager.previousActionBar()
		);
		addMenuEntry(
			MenuAction.RUNELITE,
			"Create",
			"Action bar",
			e -> actionBarManager.createActionBar()
		);

		this.actionBarManager = actionBarManager;
		this.menuEntryManager = menuEntryManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var actions = actionBarManager.getActiveBar().getActions();
		actionButtons = new ArrayList<>();

		final var metrics = graphics.getFontMetrics(FontManager.getRunescapeBoldFont());
		final var barNumberIndicator = actionBarManager.getActiveBarIndex() + 1 + "/" + actionBarManager.getActionBars()
			.size();

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(barNumberIndicator)
				.leftColor(Color.CYAN)
				.leftFont(FontManager.getRunescapeBoldFont())
				.preferredSize(new Dimension(metrics.stringWidth(barNumberIndicator), 0))
				.build()
		);

		for (int i = 0; i < 10; i++)
		{
			final var action = actions[i];
			if (action == null)
			{
				panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				continue;
			}

			final var actionButton = new ActionButtonComponent(action);
			panelComponent.getChildren().add(actionButton);
			actionButtons.add(actionButton);
		}

		return super.render(graphics);
	}

	@Override
	public void onMouseOver()
	{
		final var mouse = Static.getClient().getMouseCanvasPosition();
		for (final var actionButton : actionButtons)
		{
			final var bounds = actionButton.getBounds();
			final var x = bounds.getX() + getBounds().getX();
			final var y = bounds.getY() + getBounds().getY();
			if (
				mouse.getX() > x && mouse.getX() < x + bounds.getWidth()
					&& mouse.getY() > y && mouse.getY() < y + bounds.getHeight()
			)
			{
				menuEntryManager.setHoveredAction(actionButton.getAction());
				return;
			}
		}

		menuEntryManager.setHoveredAction(null);
	}
}
