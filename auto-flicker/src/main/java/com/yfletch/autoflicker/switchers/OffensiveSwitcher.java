package com.yfletch.autoflicker.switchers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.autoflicker.AutoFlickerConfig;
import com.yfletch.autoflicker.util.PrayerHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Prayer;

@Slf4j
@Singleton
public class OffensiveSwitcher implements PrayerSwitcher
{
	@Inject private Client client;
	@Inject private PrayerHelper prayerHelper;
	@Inject private AutoFlickerConfig config;

	@Getter
	private List<Prayer> previousPrayers;

	@Override
	public List<Prayer> getPrayers()
	{
		if (!config.flickOffensivePrayers()) return null;

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

	@Override
	public void clearPreviousPrayers()
	{
		previousPrayers = null;
	}

	private List<Prayer> getPrayers(String input)
	{
		if (Strings.isNullOrEmpty(input))
		{
			return new ArrayList<>();
		}

		previousPrayers = Arrays.stream(input.split(","))
			.map(name -> prayerHelper.getPrayer(name))
			.collect(Collectors.toList());
		return new ArrayList<>(previousPrayers);
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
