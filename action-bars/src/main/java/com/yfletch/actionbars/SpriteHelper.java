package com.yfletch.actionbars;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;
import net.runelite.api.SpritePixels;

public class SpriteHelper
{
	private static final RescaleOp DARKEN_OP = new RescaleOp(0.5f, 0, null);

	public static BufferedImage darkenImage(BufferedImage image)
	{
		final var newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		DARKEN_OP.filter(image, newImage);
		return newImage;
	}

	public static BufferedImage combineSprites(List<SpritePixels> sprites)
	{
		int width = 0;
		int height = 0;
		for (final var sprite : sprites)
		{
			width = Math.max(width, sprite.getWidth());
			height = Math.max(height, sprite.getHeight());
		}

		final var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (final var sprite : sprites)
		{
			writeSprite(sprite, image);
		}

		return image;
	}

	public static void writeSprite(SpritePixels sprite, BufferedImage image)
	{
		if (image.getWidth() < sprite.getWidth() || image.getHeight() < sprite.getHeight())
		{
			throw new IllegalArgumentException("Cannot draw sprite onto a smaller image");
		}

		final var pixels = sprite.getPixels();

		for (int i = 0; i < pixels.length; i++)
		{
			if (pixels[i] != 0)
			{
				final var pixel = pixels[i] | 0xff000000;
				sprite.getOffsetX();

				image.setRGB(
					sprite.getOffsetX() + (i % sprite.getWidth()),
					sprite.getOffsetY() + i / sprite.getWidth(),
					pixel
				);
			}
		}
	}
}
