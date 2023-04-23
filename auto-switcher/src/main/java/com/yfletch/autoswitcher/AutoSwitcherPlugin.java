package com.yfletch.autoswitcher;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "Auto Switcher",
	description = "Trigger extra equipment switches on equip"
)
public class AutoSwitcherPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;
	@Inject private ItemManager itemManager;
	@Inject private AutoSwitcherConfig config;

	private static final Set<String> EQUIP_OPTIONS = Set.of("Wield", "Wear");

	private final Executor EXECUTOR = Executors.newSingleThreadExecutor();

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!EQUIP_OPTIONS.contains(event.getMenuOption()))
		{
			return;
		}

		final var switches = getSwitches(event.getItemId());
		if (switches != null)
		{
			EXECUTOR.execute(() -> {
				for (final var switchItem : switches)
				{
					try
					{
						Thread.sleep(config.sleep());
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					equip(switchItem);
				}
			});
		}
	}

	private List<Integer> getSwitches(int itemId)
	{
		final var triggers = getTriggers();
		final var switches = getSwitches();
		for (var i = 0; i < triggers.size(); i++)
		{
			final var itemIds = parse(triggers.get(i));
			if (itemIds.stream().anyMatch(id -> itemId == id))
			{
				return parse(switches.get(i));
			}
		}

		return null;
	}

	private void equip(int itemId)
	{
		final var index = getInventoryIndex(itemId);
		if (index == -1)
		{
			return;
		}

		final var item = itemManager.getItemComposition(itemId);
		final var actionIndex = getEquipActionIndex(item);
		if (actionIndex == -1)
		{
			return;
		}

		log.info("Equip " + itemId);

		clientThread.invokeLater(() -> {
			client.invokeMenuAction(
				item.getInventoryActions()[actionIndex],
				item.getName(),
				actionIndex,
				MenuAction.CC_OP.getId(),
				index,
				WidgetInfo.INVENTORY.getId()
			);
		});
	}

	private int getEquipActionIndex(ItemComposition item)
	{
		final var actions = item.getInventoryActions();

		for (var i = 0; i < actions.length; i++)
		{
			if (actions[i] != null && EQUIP_OPTIONS.contains(actions[i]))
			{
				return i + 2;
			}
		}

		return -1;
	}

	private int getInventoryIndex(int itemId)
	{
		// find next in inventory
		final var inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null || !inventory.contains(itemId))
		{
			return -1;
		}

		for (var i = 0; i < inventory.size(); i++)
		{
			final var item = inventory.getItem(i);
			if (item != null && item.getId() == itemId)
			{
				return i;
			}
		}

		return -1;
	}

	private List<Integer> parse(String input)
	{
		input = input.split("//", 2)[0].trim();

		if (Strings.isNullOrEmpty(input))
		{
			return new ArrayList<>();
		}

		return Arrays.stream(input.split(",")).map(Integer::parseInt)
			.collect(Collectors.toList());
	}

	private List<String> getTriggers()
	{
		return List.of(
			config.trigger1(),
			config.trigger2(),
			config.trigger3(),
			config.trigger4(),
			config.trigger5()
		);
	}

	private List<String> getSwitches()
	{
		return List.of(
			config.switch1(),
			config.switch2(),
			config.switch3(),
			config.switch4(),
			config.switch5()
		);
	}

	@Provides
	AutoSwitcherConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoSwitcherConfig.class);
	}
}
