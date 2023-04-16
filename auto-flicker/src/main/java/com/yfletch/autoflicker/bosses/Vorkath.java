package com.yfletch.autoflicker.bosses;

import com.google.inject.Inject;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.BossHelper;
import net.runelite.api.Prayer;

public class Vorkath implements AutoFlickRule
{
	private static final int DRAGON_FIRE = 393;
	private static final int ICE_BARRAGE = 395;
	private static final int PURPLE_FIRE = 1471;
	private static final int VENOM_FIRE = 1470;
	private static final int BLUE_FIRE = 1479;
	private static final int SUPER_FIRE = 1481;
	private static final int SPIKY_BALL = 1477;

	private static final int MELEE_ANIM = 7951;

	private static final int[] VORKATHS = new int[]{8058, 8059, 8061};

	@Inject
	private BossHelper bossHelper;

	@Inject
	private AutoFlickerConfig config;

	@Override
	public boolean isEnabled()
	{
		return config.flickVorkath()
			&& bossHelper.isNpcVisible(VORKATHS);
	}

	@Override
	public Prayer[] getPriority()
	{
		return new Prayer[]{
			Prayer.PROTECT_FROM_MELEE,
			Prayer.PROTECT_FROM_MAGIC,
			Prayer.PROTECT_FROM_MISSILES
		};
	}

	@Override
	public boolean shouldProtectMelee()
	{
		return bossHelper.npcHasAnimation(MELEE_ANIM, VORKATHS);
	}

	@Override
	public boolean shouldProtectMagic()
	{
		return bossHelper.incomingProjectile(
			DRAGON_FIRE,
			ICE_BARRAGE,
			PURPLE_FIRE,
			VENOM_FIRE,
			BLUE_FIRE,
			SUPER_FIRE
		);
	}

	@Override
	public boolean shouldProtectMissiles()
	{
		return true;
	}

	@Override
	public boolean shouldSmite()
	{
		return false;
	}
}
