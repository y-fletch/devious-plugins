package com.yfletch.actionbars;

import com.google.inject.Inject;
import com.yfletch.actionbars.bar.ActionBarManager;
import com.yfletch.actionbars.overlay.ActionBarOverlay;
import java.awt.event.KeyEvent;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "Action bars",
	description = "Keybind-able action bars"
)
public class ActionBarsPlugin extends Plugin
{
	private final static Set<String> IGNORED_ENTRIES = Set.of("Cancel", "Examine");

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ActionBarManager actionBarManager;

	@Inject
	private ActionBarOverlay actionBarOverlay;

	@Override
	protected void startUp()
	{
		actionBarManager.startUp();
		overlayManager.add(actionBarOverlay);
	}

	@Override
	protected void shutDown()
	{
		actionBarManager.shutDown();
		overlayManager.remove(actionBarOverlay);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final var hotkeyPressed = client.isKeyPressed(KeyCode.KC_CONTROL);
		if (hotkeyPressed && !IGNORED_ENTRIES.contains(event.getOption()))
		{

			final var menuEntry = event.getMenuEntry();

			client.createMenuEntry(-1)
				.setOption("<col=00ff00>+</col> " + menuEntry.getOption())
				.setTarget(event.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e -> {
					log.info("Adding " + menuEntry.getOption() + " " + menuEntry.getTarget() + " to action bar");

					createAction(SavedAction.fromMenuEntry(menuEntry));
				});
		}
	}

	private void createAction(SavedAction savedAction)
	{
		sendChatMessage(
			"Please press the desired hotkey for \""
				+ savedAction.getName() + "\""
		);
		sendChatMessage("Press esc to cancel.");

		keyManager.registerKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					keyManager.unregisterKeyListener(this);
					sendChatMessage("Cancelled.");
					return;
				}

				// skip modifier keys
				if (e.getKeyCode() == KeyEvent.VK_ALT
					|| e.getKeyCode() == KeyEvent.VK_CONTROL
					|| e.getKeyCode() == KeyEvent.VK_META
					|| e.getKeyCode() == KeyEvent.VK_SHIFT)
				{
					return;
				}

				var action = actionBarManager.addAction(savedAction, new Keybind(e));
				sendChatMessage("Action \"" + action.getName() + "\" bound to [" + action.getKeybind() + "]");
				keyManager.unregisterKeyListener(this);
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
			}
		});
	}

	public void sendChatMessage(String message)
	{
		if (!client.isClientThread())
		{
			clientThread.invokeLater(() -> sendChatMessage(message));
			return;
		}

		client.addChatMessage(
			ChatMessageType.GAMEMESSAGE,
			"Action bars",
			"[Action bars] " + message,
			null
		);
	}
}
