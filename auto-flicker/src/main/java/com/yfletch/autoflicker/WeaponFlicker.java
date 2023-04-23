package com.yfletch.autoflicker;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.util.PrayerHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Prayer;

@Singleton
public class WeaponFlicker
{
	@Inject private Client client;
	@Inject private AutoFlickerConfig config;
	@Inject private PrayerHelper prayerHelper;

	public List<Prayer> getPrayers()
	{
		if (!config.flickOffensivePrayers())
		{
			return null;
		}

		if (hasMeleeWeaponEquipped())
		{
			return getPrayers(config.meleePrayers());
		}

		if (hasRangedWeaponEquipped())
		{
			return getPrayers(config.rangedPrayers());
		}

		if (hasMagicWeaponEquipped())
		{
			return getPrayers(config.magicPrayers());
		}

		return null;
	}

	private List<Prayer> getPrayers(String input)
	{
		if (Strings.isNullOrEmpty(input))
		{
			return new ArrayList<>();
		}

		return Arrays.stream(input.split(","))
			.map(name -> prayerHelper.getPrayer(name))
			.collect(Collectors.toList());
	}

	private Stream<Integer> getIds(String input)
	{
		if (Strings.isNullOrEmpty(input))
		{
			return Stream.<Integer>builder().build();
		}

		return Arrays.stream(input.split(",")).map(Integer::parseInt);
	}

	private boolean hasMeleeWeaponEquipped()
	{
		return getIds(config.meleeWeapons()).anyMatch(this::hasEquipped);
	}

	private boolean hasRangedWeaponEquipped()
	{
		return getIds(config.rangedWeapons()).anyMatch(this::hasEquipped);
	}

	private boolean hasMagicWeaponEquipped()
	{
		return getIds(config.magicWeapons()).anyMatch(this::hasEquipped);
	}

	private boolean hasEquipped(int... itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
		if (container == null)
		{
			return false;
		}

		return Arrays.stream(itemId).map(container::count).sum() > 0;
	}
}
