package com.yfletch.actionbars.bar;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.actionbars.Action;
import com.yfletch.actionbars.ActionBar;
import com.yfletch.actionbars.ActionBarsConfig;
import com.yfletch.actionbars.SavedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.input.KeyManager;

@Singleton
public class ActionBarManager
{
	@Inject
	private ConfigManager configManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private Gson gson;

	@Setter
	@Getter
	private int activeBarIndex = 0;

	private int selectionIndex = 0;

	@Getter
	private List<ActionBar> actionBars;

	public void startUp()
	{
		actionBars = load();
		getActiveBar().registerHotkeys(keyManager);
	}

	public void shutDown()
	{
		getActiveBar().unregisterHotkeys(keyManager);
		save(actionBars);
	}

	public ActionBar getActiveBar()
	{
		if (activeBarIndex < 0 || activeBarIndex >= actionBars.size())
		{
			activeBarIndex = 0;
		}

		return actionBars.get(activeBarIndex);
	}

	public Action addAction(SavedAction savedAction, Keybind keybind)
	{
		var bar = getActiveBar();
		var action = new Action(savedAction, keybind);
		bar.getActions().add(selectionIndex++, action);

		bar.refreshHotkeys(keyManager);

		return action;
	}

	private void save(List<ActionBar> actionBars)
	{
		if (actionBars == null || actionBars.isEmpty())
		{
			configManager.unsetConfiguration(
				ActionBarsConfig.CONFIG_GROUP,
				ActionBarsConfig.CONFIG_STORAGE
			);
			return;
		}

		var json = gson.toJson(actionBars);
		configManager.setConfiguration(
			ActionBarsConfig.CONFIG_GROUP,
			ActionBarsConfig.CONFIG_STORAGE,
			json
		);
	}

	private List<ActionBar> load()
	{
		var json = configManager.getConfiguration(
			ActionBarsConfig.CONFIG_GROUP,
			ActionBarsConfig.CONFIG_STORAGE
		);
		if (Strings.isNullOrEmpty(json) || true)
		{
			return List.of(
				new ActionBar(0, new ArrayList<>())
			);
		}

		return new ArrayList<>(Arrays.asList(gson.fromJson(json, ActionBar[].class)));
	}
}
