package net.slipp.support.wiki.pegdown;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentParserTest {
	private static final Logger log = LoggerFactory.getLogger(ComponentParserTest.class);

	@Test
	public void parse() throws Exception {
		PegDownPlugins plugins = new PegDownPlugins.Builder().withPlugin(ComponentParser.class).build();

		String markdown = "## 제목\r* 리스트1\r* 리스트2\r\r" 
				+ "%%% someMethod(someParam=someValue)\r" 
				+ "body goes here\r" 
				+ "%%%";
		PegDownProcessor processor = new PegDownProcessor(0, plugins);
		RootNode ast = processor.parseMarkdown(markdown.toCharArray());

		List<ToHtmlSerializerPlugin> serializePlugins = Arrays
				.asList((ToHtmlSerializerPlugin) (new ComponentSerializer()));

		String finalHtml = new ToHtmlSerializer(new LinkRenderer(), serializePlugins).toHtml(ast);
		
		log.debug("result : {}", finalHtml);
	}
}
