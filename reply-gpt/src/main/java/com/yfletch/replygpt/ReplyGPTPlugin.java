package com.yfletch.replygpt;

import com.google.inject.Inject;
import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.input.Keyboard;
import org.pf4j.Extension;

@Extension
@Slf4j
@PluginDescriptor(
	name = "Reply GPT",
	enabledByDefault = false,
	description = "Behold the power of AI"
)
public class ReplyGPTPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ReplyGPTConfig config;

	@Inject
	private ChatBot bot;

	@Override
	protected void startUp()
	{
		try
		{
			bot.init();

			clientThread.invokeLater(() -> {
				client.addChatMessage(
					ChatMessageType.GAMEMESSAGE,
					"Reply GPT",
					"Reply GPT successfully activated.",
					null
				);
			});
		}
		catch (RuntimeException e)
		{
			JOptionPane.showMessageDialog(client.getCanvas(), e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		bot.forget();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!config.enabled())
		{
			return;
		}

		log.info(event.toString());

		if (event.getType() == ChatMessageType.PRIVATECHAT
			|| event.getType() == ChatMessageType.PUBLICCHAT)
		{
			Executors.newSingleThreadExecutor().execute(() -> {
				var response = bot.respond(new Message(
					event.getType() == ChatMessageType.PRIVATECHAT ? "private" : "public",
					event.getName(),
					event.getMessage()
				));

				if (response == null)
				{
					return;
				}

				System.out.println(event.getMessage());
				System.out.println(response.getMessage());

				if (event.getType() == ChatMessageType.PRIVATECHAT)
				{
					Keyboard.type((char) KeyEvent.VK_TAB);
				}
				else
				{
					Keyboard.sendEnter();
				}

				Time.sleep(2000);
				Keyboard.type(response.getMessage());
				Time.sleep(10000);
//				Keyboard.sendEnter();
			});
		}
	}

	@Provides
	ReplyGPTConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ReplyGPTConfig.class);
	}
}
