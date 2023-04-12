package com.yfletch.replygpt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilittlecat.chatgpt.offical.entity.ChatCompletionRequestBody;
import com.lilittlecat.chatgpt.offical.entity.ChatCompletionResponseBody;
import com.lilittlecat.chatgpt.offical.entity.Constant;
import static com.lilittlecat.chatgpt.offical.entity.Constant.DEFAULT_USER;
import com.lilittlecat.chatgpt.offical.entity.Message;
import com.lilittlecat.chatgpt.offical.entity.Model;
import com.lilittlecat.chatgpt.offical.exception.BizException;
import com.lilittlecat.chatgpt.offical.exception.Error;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <p>
 * a Java client for ChatGPT uses official API.
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Slf4j
public class ChatGPT
{
	private final String apiKey;
	protected OkHttpClient client;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final ArrayList<Message> messages = new ArrayList<>();

	public ChatGPT(String apiKey)
	{
		this.apiKey = apiKey;
		this.client = new OkHttpClient();
	}

	public ChatGPT(String apiKey, OkHttpClient client)
	{
		this.apiKey = apiKey;
		this.client = client;
	}

	public ChatGPT(String apiKey, Proxy proxy)
	{
		this.apiKey = apiKey;
		client = new OkHttpClient.Builder().proxy(proxy).build();
	}

	public ChatGPT(String apiKey, String proxyHost, int proxyPort)
	{
		this.apiKey = apiKey;
		client = new OkHttpClient.Builder().
			proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
			.build();
	}

	public String ask(String input)
	{
		return ask(Model.GPT_3_5_TURBO.getName(), DEFAULT_USER, input);
	}

	public void forget()
	{
		messages.clear();
	}

	private String buildRequestBody(String model, String role, String content)
	{
		try
		{
			messages.add(Message.builder().role(role).content(content).build());
			ChatCompletionRequestBody requestBody = ChatCompletionRequestBody.builder()
				.model(model)
				.messages(messages)
				.build();
			return objectMapper.writeValueAsString(requestBody);
		}
		catch (JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * ask for response message
	 *
	 * @param model
	 * @param role
	 * @param content
	 * @return ChatCompletionResponseBody
	 */
	public ChatCompletionResponseBody askOriginal(String model, String role, String content)
	{
		RequestBody body = RequestBody.create(
			buildRequestBody(model, role, content), MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
			.url(Constant.CHAT_COMPLETION_API_URL)
			.header("Authorization", "Bearer " + apiKey)
			.post(body)
			.build();

		try (Response response = client.newCall(request).execute())
		{
			if (!response.isSuccessful())
			{
				if (response.body() == null)
				{
					log.error("Request failed: {}, please try again", response.message());
					throw new BizException(response.code(), "Request failed");
				}
				else
				{
					log.error("Request failed: {}, please try again", response.body().string());
					throw new BizException(response.code(), response.body().string());
				}
			}
			else
			{
				assert response.body() != null;
				String bodyString = response.body().string();
				return objectMapper.readValue(bodyString, ChatCompletionResponseBody.class);
			}
		}
		catch (IOException e)
		{
			log.error("Request failed: {}", e.getMessage());
			throw new BizException(Error.SERVER_HAD_AN_ERROR.getCode(), e.getMessage());
		}
	}

	/**
	 * ask for response message
	 *
	 * @param model
	 * @param role
	 * @param content
	 * @return String message
	 */
	public String ask(String model, String role, String content)
	{
		ChatCompletionResponseBody chatCompletionResponseBody = askOriginal(model, role, content);
		List<ChatCompletionResponseBody.Choice> choices = chatCompletionResponseBody.getChoices();

		for (ChatCompletionResponseBody.Choice choice : choices)
		{
			System.out.println("Choice: " + choice);
		}

		Message message = choices.get(0).getMessage();

		if (message != null)
		{
			messages.add(message);

			return message.getContent();
		}

		return null;
	}

}
