package com.yfletch.actionbars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class ActionBar
{
	public final static int NUM_ACTIONS = 10;

	private int id;
	private Action[] actions;

	public int getNextEmptyActionIndex()
	{
		for (int i = 0; i < NUM_ACTIONS; i++)
		{
			if (actions[i] == null)
			{
				return i;
			}
		}

		return -1;
	}

	public void add(Action action)
	{
		final var index = getNextEmptyActionIndex();
		if (index == -1)
		{
			throw new IllegalArgumentException("Cannot insert new action");
		}

		actions[getNextEmptyActionIndex()] = action;
	}

	public void remove(Action action)
	{
		for (int i = 0; i < NUM_ACTIONS; i++)
		{
			if (actions[i] == action)
			{
				actions[i] = null;
			}
		}
	}

	public void swapRight(Action action)
	{
		for (int i = 0; i < NUM_ACTIONS - 1; i++)
		{
			if (actions[i] == action)
			{
				actions[i] = actions[i + 1];
				actions[i + 1] = action;
				return;
			}
		}
	}

	public void swapLeft(Action action)
	{
		for (int i = 1; i < NUM_ACTIONS; i++)
		{
			if (actions[i] == action)
			{
				actions[i] = actions[i - 1];
				actions[i - 1] = action;
				return;
			}
		}
	}

	public void clear()
	{
		actions = new Action[NUM_ACTIONS];
	}
}
