package com.yfletch.autoflicker.util;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Projectile;

@Slf4j
@Singleton
public class ProjectileHelper
{
	@Inject
	private Client client;

	public List<Projectile> query(Predicate<Projectile> predicate)
	{
		return Lists.newArrayList(client.getProjectiles().iterator()).stream()
			.filter(predicate)
			.collect(Collectors.toList());
	}

	public boolean isProjectileVisible(int... projectileId)
	{
		List<Integer> list = Ints.asList(projectileId);
		return !query(p -> list.contains(p.getId())).isEmpty();
	}

	public boolean isProjectileTargeting(Actor actor, int... projectileId)
	{
		List<Integer> list = Ints.asList(projectileId);
		return !query(p -> list.contains(p.getId())
			&& p.getInteracting().equals(actor)).isEmpty();
	}

	public boolean isProjectileTargetingPlayer(int... projectileId)
	{
		return isProjectileTargeting(client.getLocalPlayer(), projectileId);
	}

}
