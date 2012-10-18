package net.slipp.support.wiki;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;

public class SLiPPListBlock extends Block {

	private static final int LINE_REMAINDER_GROUP_OFFSET = 2;

	static final Pattern startPattern = Pattern.compile("((?:(?:\\*)|(?:#)|(?:-))+)\\s(.*+)"); //$NON-NLS-1$

	private int blockLineCount = 0;

	private Matcher matcher;

	private Stack<ListState> listState;

	public SLiPPListBlock() {
	}

	@Override
	public int processLineContent(String line, int offset) {
		boolean continuation = false;
		if (blockLineCount == 0) {
			listState = new Stack<ListState>();
			Attributes attributes = new Attributes();
			String listSpec = matcher.group(1);
			int level = calculateLevel(listSpec);
			BlockType type = calculateType(listSpec);

			if (type == BlockType.BULLETED_LIST && "-".equals(listSpec)) { //$NON-NLS-1$
				attributes.setCssStyle("list-style: square"); //$NON-NLS-1$
			}

			// 0-offset matches may start with the "*** " prefix.
			offset = matcher.start(LINE_REMAINDER_GROUP_OFFSET);

			listState.push(new ListState(1, type));
			builder.beginBlock(type, attributes);

			adjustLevel(listSpec, level, type);
		} else {
			Matcher matcher = startPattern.matcher(line);
			if (!matcher.matches()) {
				boolean empty = offset == 0 && markupLanguage.isEmptyLine(line);
				boolean breaking = SlippParagraphBlock.paragraphBreakingBlockMatches(getMarkupLanguage(), line, offset);
				if (empty || breaking) {
					setClosed(true);
					return 0;
				} else {
					continuation = true;
				}
			} else {
				String listSpec = matcher.group(1);
				int level = calculateLevel(listSpec);
				BlockType type = calculateType(listSpec);

				offset = matcher.start(LINE_REMAINDER_GROUP_OFFSET);

				adjustLevel(listSpec, level, type);
			}
		}
		++blockLineCount;

		ListState listState = this.listState.peek();
		if (listState.openItem) {
			if (continuation) {
				builder.lineBreak();
			} else {
				builder.endBlock();
				listState.openItem = false;
			}
		}
		if (!listState.openItem) {
			listState.openItem = true;
			builder.beginBlock(BlockType.LIST_ITEM, new Attributes());
		}

		markupLanguage.emitMarkupLine(getParser(), state, line, offset);

		return -1;
	}

	private void adjustLevel(String listSpec, int level, BlockType type) {
		for (ListState previousState = listState.peek(); level != previousState.level || previousState.type != type; previousState = listState.peek()) {

			if (level > previousState.level) {
				if (!previousState.openItem) {
					builder.beginBlock(BlockType.LIST_ITEM, new Attributes());
					previousState.openItem = true;
				}

				Attributes blockAttributes = new Attributes();
				if (type == BlockType.BULLETED_LIST && "-".equals(listSpec)) { //$NON-NLS-1$
					blockAttributes.setCssStyle("list-style: square"); //$NON-NLS-1$
				}
				listState.push(new ListState(previousState.level + 1, type));
				builder.beginBlock(type, blockAttributes);
			} else {
				closeOne();
				if (listState.isEmpty()) {
					Attributes blockAttributes = new Attributes();
					if (type == BlockType.BULLETED_LIST && "-".equals(listSpec)) { //$NON-NLS-1$
						blockAttributes.setCssStyle("list-style: square"); //$NON-NLS-1$
					}

					listState.push(new ListState(1, type));
					builder.beginBlock(type, blockAttributes);
				}
			}
		}
	}

	private int calculateLevel(String listSpec) {
		return listSpec.length();
	}

	private BlockType calculateType(String listSpec) {
		return listSpec.charAt(listSpec.length() - 1) == '#' ? BlockType.NUMERIC_LIST : BlockType.BULLETED_LIST;
	}

	@Override
	public boolean canStart(String line, int lineOffset) {
		blockLineCount = 0;
		listState = null;
		if (lineOffset == 0) {
			matcher = startPattern.matcher(line);
			final boolean matches = matcher.matches();
			if (matches) {
				String listSpec = matcher.group(1);
				if (listSpec.charAt(0) == '-') {
					int level = calculateLevel(listSpec);
					if (level > 1) {
						// don't match hr, emdash, endash etc.
						// list block must start at level 1s
						return false;
					}
				}
			}
			return matches;
		} else {
			matcher = null;
			return false;
		}
	}

	@Override
	public void setClosed(boolean closed) {
		if (closed && !isClosed()) {
			while (listState != null && !listState.isEmpty()) {
				closeOne();
			}
			listState = null;
		}
		super.setClosed(closed);
	}

	private void closeOne() {
		ListState e = listState.pop();
		if (e.openItem) {
			builder.endBlock();
		}
		builder.endBlock();
	}

	private static class ListState {
		int level;

		BlockType type;

		boolean openItem;

		private ListState(int level, BlockType type) {
			super();
			this.level = level;
			this.type = type;
		}

	}
}