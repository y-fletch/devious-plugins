package com.yfletch.autoflicker.bosses;

import com.google.inject.Inject;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.BossHelper;
import net.runelite.api.Prayer;

public class Zebak implements AutoFlickRule
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
	public Prayer[] getPriority()
	{
		return new Prayer[]{Prayer.PROTECT_FROM_MISSILES, Prayer.PROTECT_FROM_MAGIC};
	}

	@Override
	public boolean shouldProtectMelee()
	{
		return false;
	}

	@Override
	public boolean shouldProtectMagic()
	{
		return bossHelper.incomingProjectile(2176, 2181);
	}

	@Override
	public boolean shouldProtectMissiles()
	{
		return bossHelper.incomingProjectile(2178, 2187);
	}

	@Override
	public boolean shouldSmite()
	{
		return false;
	}
}
