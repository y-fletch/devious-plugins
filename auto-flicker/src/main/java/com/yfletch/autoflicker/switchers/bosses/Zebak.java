package com.yfletch.autoflicker.switchers.bosses;

import com.google.inject.Inject;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.BossHelper;
import net.runelite.api.Prayer;

public class Zebak implements AutoSwitchRule
{
	@Inject private BossHelper bossHelper;
	@Inject private AutoFlickerConfig config;

	@Override
	public boolean isEnabled()
	{
		return config.flickZebak()
			&& bossHelper.isNpcVisible(11730, 11741);
	}

	@Override
	public Prayer getPrayer()
	{
		if (bossHelper.incomingProjectile(2181))
		{
			return Prayer.PROTECT_FROM_MAGIC;
		}

		if (bossHelper.incomingProjectile(2187))
		{
			return Prayer.PROTECT_FROM_MISSILES;
		}

		if (bossHelper.isProjectileVisible(2176))
		{
			return Prayer.PROTECT_FROM_MAGIC;
		}

		if (bossHelper.isProjectileVisible(2178))
		{
			return Prayer.PROTECT_FROM_MISSILES;
		}

		return null;
	}
}
