package com.yfletch.actionbars;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Keybind;
import net.runelite.client.util.HotkeyListener;

@Getter
@RequiredArgsConstructor
public class Action
{
	@NonNull
	private SavedAction savedAction;

	@NonNull
	private Keybind keybind;

	@Getter
	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> keybind)
	{
		@Override
		protected void hotkeyPressed()
		{
			savedAction.invoke();
		}
	};

	public String getName()
	{
		return savedAction.getName();
	}

	// something to get icon
}
