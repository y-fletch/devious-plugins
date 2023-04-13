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

@Singleton
public class ActionBarManager
{
	public static final int NUM_ACTION_BARS = 10;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Gson gson;

	@Setter
	@Getter
	private int activeBarIndex = 0;

	@Getter
	private List<ActionBar> actionBars;

	public void startUp()
	{
		actionBars = load();
	}

	public void shutDown()
	{
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

	public Action[] getActiveActions()
	{
		return getActiveBar().getActions();
	}

	public Action addAction(SavedAction savedAction, Keybind keybind)
	{
		var bar = getActiveBar();
		var action = new Action(savedAction, keybind);
		bar.add(action);

		return action;
	}

	public void clearCurrentActionBar()
	{
		getActiveBar().clear();
	}

	public void nextActionBar()
	{
		activeBarIndex++;
		if (activeBarIndex >= actionBars.size())
		{
			activeBarIndex = 0;
		}
	}

	public void previousActionBar()
	{
		activeBarIndex--;
		if (activeBarIndex < 0)
		{
			activeBarIndex = actionBars.size() - 1;
		}
	}

	public void createActionBar()
	{
		actionBars.add(new ActionBar(actionBars.size(), new Action[ActionBar.NUM_ACTIONS]));
		activeBarIndex = actionBars.size() - 1;
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
		if (Strings.isNullOrEmpty(json))
		{
			return List.of(
				new ActionBar(0, new Action[ActionBar.NUM_ACTIONS])
			);
		}

		return new ArrayList<>(Arrays.asList(gson.fromJson(json, ActionBar[].class)));
	}
}
