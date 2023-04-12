package com.yfletch.replygpt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message
{
	private final static Pattern pattern = Pattern.compile("^\\((\\w+)\\) (.+): (.*)$");

	private String type;
	private String user;
	private String message;

	public String toString()
	{
		return "(" + type + ") " + user + ": " + message;
	}

	public static Message fromString(String msg)
	{
		System.out.println("Parsing message:");
		System.out.println(msg);

		if (msg.contains("No response"))
		{
			return null;
		}

		final Matcher matcher = pattern.matcher(msg);
		if (matcher.find() && matcher.groupCount() == 3)
		{
			return new Message(
				matcher.group(1),
				matcher.group(2),
				matcher.group(3)
			);
		}

		return null;
	}
}
