package com.yfletch.autoflicker.switchers.bosses;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.BossHelper;
import net.runelite.api.Prayer;

@Singleton
public class KingBlackDragon implements AutoSwitchRule
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
	public Prayer getPrayer()
	{
		if (bossHelper.incomingProjectile(DRAGON_FIRE, TOXIC_FIRE, SHOCKING_FIRE, ICY_FIRE))
		{
			return Prayer.PROTECT_FROM_MAGIC;
		}

		if (bossHelper.isBesideNpc(KING_BLACK_DRAGON))
		{
			return Prayer.PROTECT_FROM_MELEE;
		}

		return null;
	}
}
