package net.slipp.support.wiki;

import org.eclipse.mylyn.internal.wikitext.confluence.core.block.AbstractConfluenceDelimitedBlock;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;

public class QuoteBlock extends AbstractConfluenceDelimitedBlock {

    private int paraLine = 0;

    private boolean paraOpen = false;

    private Block nestedBlock = null;

    public QuoteBlock() {
        super("quote"); //$NON-NLS-1$
    }

    @Override
    protected void resetState() {
        super.resetState();
        paraOpen = false;
        paraLine = 0;
        nestedBlock = null;
    }

    @Override
    protected void beginBlock() {
        Attributes attributes = new Attributes();
        builder.beginBlock(BlockType.QUOTE, attributes);
    }

    @Override
    protected void endBlock() {
        if (nestedBlock != null) {
            nestedBlock.setClosed(true);
            nestedBlock = null;
        }
        if (paraOpen) {
            builder.endBlock(); // para
            paraLine = 0;
            paraOpen = false;
        }
        builder.endBlock(); // quote
    }

    @Override
    protected void handleBlockContent(String content) {
        if (nestedBlock == null) {
            SlippLanguage markupLanguage = (SlippLanguage) getMarkupLanguage();
            for (Block block : markupLanguage.getNestedBlocks()) {
                if (block.canStart(content, 0)) {
                    nestedBlock = block.clone();
                    nestedBlock.setParser(getParser());
                    nestedBlock.setState(getState());
                    if (paraOpen) {
                        builder.endBlock(); // para
                        paraOpen = false;
                        paraLine = 0;
                    }
                    break;
                }
            }
        }
        if (nestedBlock != null) {
            int lineOffset = nestedBlock.processLine(content, 0);
            if (nestedBlock.isClosed()) {
                nestedBlock = null;
            }
            if (lineOffset < content.length() && lineOffset >= 0) {
                if (nestedBlock != null) {
                    throw new IllegalStateException("if a block does not fully process a line then it must be closed"); //$NON-NLS-1$
                }
                content = content.substring(lineOffset);
            } else {
                return;
            }
        }
        if (blockLineCount == 1 && content.length() == 0) {
            return;
        }
        if (blockLineCount > 1 && paraOpen && getMarkupLanguage().isEmptyLine(content)) {
            builder.endBlock(); // para
            paraOpen = false;
            paraLine = 0;
            return;
        }
        if (!paraOpen) {
            builder.beginBlock(BlockType.PARAGRAPH, new Attributes());
            paraOpen = true;
        }
        if (paraLine != 0) {
            builder.lineBreak();
        }
        ++paraLine;
        getMarkupLanguage().emitMarkupLine(getParser(), state, content, 0);

    }

    @Override
    protected void setOption(String key, String value) {
        // no options
    }
}