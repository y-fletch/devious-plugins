package com.yfletch.actionbars.overlay;

import com.yfletch.actionbars.Action;
import com.yfletch.actionbars.ActionBar;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

@Setter
@Builder
public class ActionBarComponent implements LayoutableRenderableEntity
{
	private final ActionBar actionBar;

	@Builder.Default
	@Getter
	private final Rectangle bounds = new Rectangle();

	@Builder.Default
	private Point preferredLocation = new Point();

	@Builder.Default
	private Rectangle border = new Rectangle(
		ComponentConstants.STANDARD_BORDER,
		ComponentConstants.STANDARD_BORDER,
		ComponentConstants.STANDARD_BORDER,
		ComponentConstants.STANDARD_BORDER
	);

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var actions = actionBar.getActions();

		final var size = new Dimension(
			border.x + border.width + actions.size() * ActionButtonComponent.SIZE.width,
			border.y + border.height + ActionButtonComponent.SIZE.height
		);

		// render background
//		final BackgroundComponent backgroundComponent = new BackgroundComponent();
//		backgroundComponent.setRectangle(new Rectangle(preferredLocation, size));
//		backgroundComponent.setBackgroundColor(Color.BLACK);
//		backgroundComponent.render(graphics);

		final var baseX = preferredLocation.x + border.x;
		final var baseY = preferredLocation.y + border.y;
		var x = baseX;
		var y = baseY;

		for (final var action : actions)
		{
			var child = ActionButtonComponent.builder()
				.action(action.getName())
				.keybind(action.getKeybind())
				.build();

			child.setPreferredLocation(new Point(x, y));
			final var dimension = child.render(graphics);
			y += dimension.height;
		}

		bounds.setLocation(preferredLocation);
		bounds.setSize(size);

		return size;
	}

	public void renderAction(Graphics2D graphics, Action action)
	{

	}

	@Override
	public void setPreferredSize(Dimension dimension)
	{

	}
}
