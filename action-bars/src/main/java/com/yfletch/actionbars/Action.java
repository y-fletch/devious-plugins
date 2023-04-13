package com.yfletch.actionbars;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.SpritePixels;
import net.runelite.api.widgets.ItemQuantityMode;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.Keybind;
import net.unethicalite.client.Static;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Action
{
	@NonNull
	private SavedAction savedAction;

	@NonNull
	private Keybind keybind;

	public Widget getWidget()
	{
		return Static.getClient().getWidget(savedAction.getWidgetPackedId());
	}

	public BufferedImage getImage()
	{
		if (savedAction.isItemAction())
		{
			final var itemWidget = getWidget().getChild(savedAction.getInventoryIndex());

			final var image = Static.getItemManager().getImage(
				savedAction.getItemId(),
				itemWidget.getItemQuantity(),
				itemWidget.getItemQuantityMode() != ItemQuantityMode.STACKABLE
			);

			if (savedAction.getInventoryIndex() == -1 || itemWidget.getItemQuantity() < 1)
			{
				return SpriteHelper.darkenImage(image);
			}

			return image;
		}

		if (getWidget() != null)
		{
			try
			{
				return SpriteHelper.combineSprites(getChildSprites(getWidget()));
			}
			catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e)
			{
				// probably no sprite found or summin
				return null;
			}
		}

		return null;
	}

	public List<SpritePixels> getChildSprites(Widget widget)
	{
		final var sprites = new ArrayList<SpritePixels>();

		if (widget == null || widget.isSelfHidden())
		{
			return sprites;
		}

		if (widget.getSprite() != null)
		{
			sprites.add(widget.getSprite());
		}

		if (widget.getChildren() != null)
		{
			for (final var child : widget.getChildren())
			{
				sprites.addAll(getChildSprites(child));
			}
		}

		return sprites;
	}

	public void invoke()
	{
		savedAction.invoke();
	}

	public String getName()
	{
		return savedAction.getName();
	}
}
