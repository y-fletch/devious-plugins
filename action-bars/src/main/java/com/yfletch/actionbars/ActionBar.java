package com.yfletch.actionbars;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.input.KeyManager;

@Getter
@AllArgsConstructor
public class ActionBar
{
	private int id;
	private List<Action> actions;

	public void refreshHotkeys(KeyManager keyManager)
	{
		unregisterHotkeys(keyManager);
		registerHotkeys(keyManager);
	}

	public void registerHotkeys(KeyManager keyManager)
	{
		for (var action : actions)
		{
			keyManager.registerKeyListener(action.getHotkeyListener());
		}
	}

	public void unregisterHotkeys(KeyManager keyManager)
	{
		for (var action : actions)
		{
			keyManager.unregisterKeyListener(action.getHotkeyListener());
		}
	}
}
