package com.vieztech.utils;

import java.util.Stack;

public class StringTools {
	public static String encodeAsJSString(String raw) {
		if (raw == null || "".equals(raw))
			return raw;

		final String replaceChars[][] = { { "\\\\", "\\\"", "\\\r", "\\\n", "\\\t" },
				{ "\\\\\\\\", "\\\\\"", "\\\\\r", "\\\\\n", "\\\\\t" } };
		for (int i = 0; i < replaceChars[0].length; i++) {
			raw = raw.replaceAll(replaceChars[0][i], replaceChars[1][i]);
		}
		return raw;
	}

	public static String encodeAsHtmlString(String raw) {
		if (raw == null || "".equals(raw))
			return raw;

		final String replaceChars[][] = { { "&", "\"", "<", ">", "'" },
				{ "&amp;", "&quot;", "&lt;", "&gt;", "&#39;" } };
		for (int i = 0; i < replaceChars[0].length; i++) {
			raw = raw.replaceAll(replaceChars[0][i], replaceChars[1][i]);
		}
		return raw;
	}

	public static String getEventName(String url) {
		String partialURL = url.split("events/")[1];
		String eventName;
		if (partialURL.indexOf("/") == -1) {
			eventName = partialURL;
		} else {
			eventName = partialURL.substring(0, partialURL.indexOf("/"));
		}

		return eventName;

	}

	//
	public static String subString(String raw, int length, boolean hasHtmlTags) {
		if (raw == null || raw.length() < 1)
			return raw;
		if (!hasHtmlTags) {
			return raw.substring(0, length);
		} else {
			Stack<String> tags = new Stack<>();
			StringBuilder sb = new StringBuilder();
			StringBuilder sbTag = new StringBuilder();
			StringBuilder endTag = new StringBuilder();
			boolean tagStart = false;
			boolean countChars = true;
			boolean endTagStart = false;
			int cntr = 0;

			for (int i = 0; i < raw.length() && cntr < length; i++) {
				char currentChar = raw.charAt(i);

				if (tagStart && !endTagStart && (currentChar == ' ' || currentChar == '>')) {
					tags.push(sbTag.toString());
					sbTag.setLength(0);
					tagStart = false;
				}
				if (tagStart && !endTagStart && currentChar != '/' && currentChar != '>') {
					sbTag.append(currentChar);
				}
				if (endTagStart && currentChar != '>') {
					endTag.append(currentChar);
				}

				if (!endTagStart && tagStart && currentChar == '/') {
					endTag.setLength(0);
					endTagStart = true;
				}
				if (endTagStart && currentChar == '>') {
					if (endTag.toString().equalsIgnoreCase(tags.peek())) {
						tags.pop();
					}
				}
				if (!tagStart && currentChar == '<') {
					tagStart = true;
					countChars = false;
				}

				// if (!tagStart) {
				sb.append(currentChar);
				// }

				if (countChars) {
					cntr++;
				}

				if (!countChars && currentChar == '>') {
					countChars = true;
					tagStart = false;
					endTagStart = false;
				}
			}

			if (tagStart || endTagStart) {
				CharSequence subSequence = sb.subSequence(0, sb.lastIndexOf("<"));
				sb.setLength(0);
				sb.append(subSequence);
			}
			while (!tags.isEmpty()) {
				sb.append("</").append(tags.pop()).append(">");
			}

			return sb.toString();
		}
	}
}
