package com.yfletch.actionbars;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.actionbars.bar.ActionBarManager;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.Keybind;
import net.runelite.client.input.KeyListener;

@Slf4j
@Singleton
public class KeybindListener implements KeyListener
{
	@Inject
	private ActionBarManager actionBarManager;

	private final Map<Action, Boolean> isPressed = new HashMap<>();
	private final Map<Action, Boolean> isConsumingTyped = new HashMap<>();

	@Override
	public boolean isEnabledOnLoginScreen()
	{
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (isConsumingTyped.containsValue(true))
		{
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		for (final var action : actionBarManager.getActiveActions())
		{
			if (action == null)
			{
				continue;
			}

			if (action.getKeybind().matches(e))
			{
				boolean wasPressed = isPressed.getOrDefault(action, false);
				isPressed.put(action, true);
				if (!wasPressed)
				{
					action.invoke();
				}
				if (Keybind.getModifierForKeyCode(e.getKeyCode()) == null)
				{
					isConsumingTyped.put(action, true);
					// Only consume non modifier keys
					e.consume();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		for (final var action : actionBarManager.getActiveActions())
		{
			if (action == null)
			{
				continue;
			}

			if (action.getKeybind().matches(e))
			{
				isPressed.remove(action);
				isConsumingTyped.remove(action);
			}
		}
	}
}
