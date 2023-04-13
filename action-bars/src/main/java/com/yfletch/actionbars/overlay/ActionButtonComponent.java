package com.yfletch.actionbars.overlay;

import com.yfletch.actionbars.Action;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Constants;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.TextComponent;

@Setter
public class ActionButtonComponent implements LayoutableRenderableEntity
{
	@Getter
	private final Action action;

	public ActionButtonComponent(Action action)
	{
		this.action = action;
	}

	@Getter
	private final Rectangle bounds = new Rectangle();

	private Point preferredLocation = new Point();

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var image = action.getImage();
		if (image != null)
		{
			graphics.drawImage(image, preferredLocation.x, preferredLocation.y, null);
		}

		final var text = new TextComponent();
		final var metrics = graphics.getFontMetrics();
		final var lines = lineBreak(action.getKeybind().toString(), metrics);
		final var lineHeight = metrics.getHeight();
		final var totalLineHeight = lines.length * lineHeight;

		for (int i = 0; i < lines.length; i++)
		{
			final var line = lines[i];
			text.setText(line);
			text.setPosition(new Point(
				preferredLocation.x + Constants.ITEM_SPRITE_WIDTH - metrics.stringWidth(line),
				preferredLocation.y + Constants.ITEM_SPRITE_HEIGHT - totalLineHeight + (i + 1) * lineHeight
			));
			text.render(graphics);
		}

		final Dimension dimension = new Dimension(Constants.ITEM_SPRITE_WIDTH, Constants.ITEM_SPRITE_HEIGHT);
		bounds.setLocation(preferredLocation);
		bounds.setSize(dimension);
		return dimension;
	}

	@Override
	public void setPreferredSize(Dimension dimension)
	{
		// Just use image dimensions for now
	}

	private String[] lineBreak(String text, FontMetrics fontMetrics)
	{
		var split = new String[]{text};
		var wraps = 0;

		while (mustWrap(split, fontMetrics) && wraps < 3)
		{
			var current = split.clone();
			var end = current[current.length - 1];
			var endSplit = end.split("(?!^)\\+", 2);
			if (endSplit.length < 2)
			{
				return split;
			}

			split = new String[current.length + 1];
			// copy existing lines into new split array
			// (except the end word we are splitting again)
			System.arraycopy(current, 0, split, 0, current.length - 1);
			split[split.length - 2] = endSplit[0];
			split[split.length - 1] = "+" + endSplit[1];

			wraps++;
		}

		return split;
	}

	private boolean mustWrap(String[] lines, FontMetrics fontMetrics)
	{
		return Arrays.stream(lines).anyMatch(line -> fontMetrics.stringWidth(line) >= Constants.ITEM_SPRITE_WIDTH);
	}
}
