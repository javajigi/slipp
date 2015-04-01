package net.slipp.support.wiki;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class WikiContents {
	public static String parse(String contents) {
		return new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS).markdownToHtml(contents);
	}
}
