package com.yfletch.autoflicker.bosses;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.BossHelper;
import net.runelite.api.Prayer;

@Singleton
public class KingBlackDragon implements AutoFlickRule
{
	private static final int DRAGON_FIRE = 393;
	private static final int TOXIC_FIRE = 394;
	private static final int ICY_FIRE = 395;
	private static final int SHOCKING_FIRE = 396;

	private static final int KING_BLACK_DRAGON = 239;

	private static final int MELEE_ANIM_1 = 80;
	private static final int MELEE_ANIM_2 = 91;

	@Inject
	private BossHelper bossHelper;

	@Inject
	private AutoFlickerConfig config;

	@Override
	public boolean isEnabled()
	{
		return config.flickKBD()
			&& bossHelper.isNpcVisible(KING_BLACK_DRAGON);
	}

	@Override
	public Prayer[] getPriority()
	{
		return new Prayer[]{Prayer.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MELEE};
	}

	@Override
	public boolean shouldProtectMelee()
	{
		return bossHelper.isBesideNpc(KING_BLACK_DRAGON);
	}

	@Override
	public boolean shouldProtectMagic()
	{
		return bossHelper.incomingProjectile(DRAGON_FIRE, TOXIC_FIRE, SHOCKING_FIRE, ICY_FIRE);
	}

	@Override
	public boolean shouldProtectMissiles()
	{
		return false;
	}

	@Override
	public boolean shouldSmite()
	{
		return false;
	}
}
