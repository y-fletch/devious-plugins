package com.yfletch.replygpt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

@Singleton
public class ChatBot
{
	private final ReplyGPTConfig config;
	private final ChatGPT gpt;

	@Getter
	private boolean ready;

	@Inject
	public ChatBot(ReplyGPTConfig config)
	{
		this.config = config;
		gpt = new ChatGPT(config.apiKey());
		System.out.println(config.apiKey());
	}

	public void init()
	{
		var response = gpt.ask(config.prompt());
		if (!response.equalsIgnoreCase("All systems operational."))
		{
			throw new RuntimeException(
				"ChatGPT to respond correctly to the given prompt. Please restart the plugin to try again."
			);
		}

		ready = true;
	}

	public void forget()
	{
		gpt.forget();
	}

	public Message respond(Message message)
	{
		System.out.println("Sending:");
		System.out.println(message);
		return Message.fromString(gpt.ask(message.toString()));
	}
}
