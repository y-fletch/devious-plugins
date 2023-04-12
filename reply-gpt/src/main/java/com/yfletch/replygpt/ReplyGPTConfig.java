package com.yfletch.replygpt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("reply-gpt")
public interface ReplyGPTConfig extends Config
{
	@ConfigItem(
		keyName = "apiKey",
		name = "OpenAI API Key",
		description = "",
		position = 0
	)
	default String apiKey()
	{
		return "";
	}

	@ConfigItem(
		keyName = "enabled",
		name = "Enabled",
		description = "Enables auto-reply for all chat",
		position = 1
	)
	default boolean enabled()
	{
		return false;
	}

	@ConfigSection(
		name = "Generator settings",
		description = "",
		position = 2
	)
	String generatorSettings = "generatorSettings";

	@ConfigItem(
		keyName = "prompt",
		name = "Prompt",
		description = "Initial prompt for GPT. ChatGPT must respond with \"All systems operational.\" for the plugin to enable.",
		position = 2,
		section = generatorSettings
	)
	default String prompt()
	{
		return "Your name is \"Bob\". You are an average OSRS (Runescape) player. Every response you give must look like it was typed in-game by a normal person, including an adequate lack of grammar, use of game-related slang, and occasional typos. \n" +
			"\n" +
			"You should not be overly helpful (NEVER offer help, or ask followup questions), and keep replies as short and succinct as possible. All replies must be less than 60 characters. Never mention that you are an AI, language model, or a bot. Do not be rude unless it is in a joking or sarcastic manner.\n" +
			"\n" +
			"I will give you incoming messages in the following format: `(public/private) [name]: [message]`, and you must respond in the same format.\n" +
			"\n" +
			"`(private)` indicates a direct message from a known friend. `(public)` indicates a public chat message visible to all other players around you. \n" +
			"\n" +
			"You must only respond to messages if appropriate, and they are directed at you. All private messages are directed at you. If you should not respond to a message, just say \"No response\". \n" +
			"\n" +
			"If you understand, respond with \"All systems operational\".";
	}
}
