package com.yfletch.actionbars.overlay;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.config.Keybind;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.TextComponent;

@Setter
@Builder
public class ActionButtonComponent implements LayoutableRenderableEntity
{
	public final static Dimension SIZE = new Dimension(48, 48);

	private String action;
	private Keybind keybind;

	private Runnable onClick;

	@Builder.Default
	private Point preferredLocation = new Point();

	@Builder.Default
	@Getter
	private final Rectangle bounds = new Rectangle();

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var x = preferredLocation.x;
		final var y = preferredLocation.y;
		final var w = SIZE.width;
		final var h = SIZE.height;

		final var fontMetrics = graphics.getFontMetrics();
		final var keybindText = keybind.toString();
		final var keybindWidth = fontMetrics.stringWidth(keybindText);

		final var text = new TextComponent();

		// action
		text.setPosition(new Point(x, y));
		text.setText(action);
		text.render(graphics);

		// keybind
		text.setPosition(new Point(x + w - keybindWidth, y + h - fontMetrics.getHeight()));
		text.setText(keybindText);
		text.render(graphics);

		bounds.setLocation(preferredLocation);
		bounds.setSize(SIZE);

		return SIZE;
	}

	@Override
	public void setPreferredSize(Dimension dimension)
	{
		// no
	}
}
