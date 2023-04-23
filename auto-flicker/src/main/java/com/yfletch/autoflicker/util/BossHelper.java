package com.yfletch.autoflicker.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@Singleton
public class BossHelper
{
	@Inject
	private Client client;

	@Inject
	private NpcHelper npcHelper;

	@Inject
	private ProjectileHelper projectileHelper;

	/**
	 * Get current player
	 */
	public Player getPlayer()
	{
		return client.getLocalPlayer();
	}

	/**
	 * Get current player location
	 */
	public WorldPoint getPlayerLocation()
	{
		return getPlayer().getWorldLocation();
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(WorldPoint worldPoint)
	{
		return getPlayerLocation().equals(worldPoint);
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(RegionPoint regionPoint)
	{
		return getPlayerLocation().equals(regionPoint.toWorld());
	}

	/**
	 * Check if player is near a position
	 */
	public boolean isNear(WorldPoint worldPoint, int maxDist)
	{
		return getPlayerLocation().distanceTo(worldPoint) < maxDist;
	}

	/**
	 * Check if player is near a position
	 */
	public boolean isNear(RegionPoint regionPoint, int maxDist)
	{
		return getPlayerLocation().distanceTo(regionPoint.toWorld()) < maxDist;
	}

	/**
	 * Check if player is in zone
	 */
	public boolean isInZone(WorldPoint lowerBound, WorldPoint upperBound)
	{
		return WorldPoint.isInZone(lowerBound, upperBound, getPlayerLocation());
	}

	/**
	 * Check if player is in zone
	 */
	public boolean isInZone(RegionPoint lowerBound, RegionPoint upperBound)
	{
		return isInZone(lowerBound.toWorld(), upperBound.toWorld());
	}

	public boolean npcHasAnimation(int animId, int... npcId)
	{
		final var npc = npcHelper.getNearest(npcId);
		return npc != null && npc.getAnimation() == animId;
	}

	public boolean incomingProjectile(int... projectileIds)
	{
		return projectileHelper.isProjectileTargetingPlayer(projectileIds);
	}

	public boolean isProjectileVisible(int... projectileIds)
	{
		return projectileHelper.isProjectileVisible(projectileIds);
	}

	public boolean isNpcVisible(int... npcId)
	{
		return npcHelper.getNearest(npcId) != null;
	}

	public boolean isBesideNpc(int npcId)
	{
		return npcHelper.isBeside(npcId);
	}
}
