package supports.wiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;

public class SlippParagraphBlock extends Block {

	private static final Pattern confluenceBlockStart = Pattern.compile("\\{(code|info|tip|warning|panel|note|toc|color|noformat|quests)(?:(:[^\\}]*))?\\}"); //$NON-NLS-1$

	private int blockLineCount = 0;

	@Override
	public int processLineContent(String line, int offset) {
		if (blockLineCount == 0) {
			Attributes attributes = new Attributes();

			builder.beginBlock(BlockType.PARAGRAPH, attributes);
		}

		if (markupLanguage.isEmptyLine(line)) {
			setClosed(true);
			return 0;
		}

		++blockLineCount;

		
		AbstractMarkupLanguage markupLanguage = (AbstractMarkupLanguage) getMarkupLanguage();

		// NOTE: in Confluence paragraphs can have nested lists and other things, however
		//       the resulting XHTML is invalid -- so here we allow for similar constructs
		//       however we cause them to end the paragraph rather than being nested.
		for (Block block : markupLanguage.getParagraphBreakingBlocks()) {
			if (block.canStart(line, offset)) {
				setClosed(true);
				return 0;
			}
		}

		Matcher blockStartMatcher = confluenceBlockStart.matcher(line);
		if (offset > 0) {
			blockStartMatcher.region(offset, line.length());
		}
		if (blockStartMatcher.find()) {
			int end = blockStartMatcher.start();
			if (end > offset) {
				markupLanguage.emitMarkupLine(getParser(), state, offset, line.substring(offset, end), 0);
			}
			setClosed(true);
			return end;
		}
		if (blockLineCount > 1) {
			builder.lineBreak();
		}
		markupLanguage.emitMarkupLine(getParser(), state, line, offset);

		return -1;
	}

	@Override
	public boolean canStart(String line, int lineOffset) {
		blockLineCount = 0;
		return true;
	}

	@Override
	public void setClosed(boolean closed) {
		if (closed && !isClosed()) {
			builder.endBlock();
		}
		super.setClosed(closed);
	}
}
