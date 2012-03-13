package supports.wiki;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.internal.wikitext.confluence.core.block.ColorBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.ExtendedPreformattedBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.ExtendedQuoteBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.HeadingBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.ListBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.QuoteBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.TableBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.TableOfContentsBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.block.TextBoxBlock;
import org.eclipse.mylyn.internal.wikitext.confluence.core.phrase.ConfluenceWrappedPhraseModifier;
import org.eclipse.mylyn.internal.wikitext.confluence.core.phrase.EmphasisPhraseModifier;
import org.eclipse.mylyn.internal.wikitext.confluence.core.phrase.HyperlinkPhraseModifier;
import org.eclipse.mylyn.internal.wikitext.confluence.core.phrase.SimplePhraseModifier;
import org.eclipse.mylyn.internal.wikitext.confluence.core.phrase.SimpleWrappedPhraseModifier;
import org.eclipse.mylyn.internal.wikitext.confluence.core.token.AnchorReplacementToken;
import org.eclipse.mylyn.internal.wikitext.confluence.core.token.EscapedCharacterReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.SpanType;
import org.eclipse.mylyn.wikitext.core.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.EntityReferenceReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.ImpliedHyperlinkReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.PatternEntityReferenceReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.PatternLineBreakReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.PatternLiteralReplacementToken;

import supports.ServiceType;

public class SlippLanguage extends AbstractMarkupLanguage {
	/**
	 * blocks that may be nested in side a quote block
	 * 
	 * @see ExtendedQuoteBlock
	 */
	private final List<Block> nestedBlocks = new ArrayList<Block>();
	
	public SlippLanguage() {
		setName("Confluence"); //$NON-NLS-1$
	}
	
	@Override
	protected void clearLanguageSyntax() {
		super.clearLanguageSyntax();
		nestedBlocks.clear();
	}

	public List<Block> getNestedBlocks() {
		return nestedBlocks;
	}

	@Override
	protected void addStandardBlocks(List<Block> blocks, List<Block> paragraphBreakingBlocks) {
		// IMPORTANT NOTE: Most items below have order dependencies.  DO NOT REORDER ITEMS BELOW!!

		HeadingBlock headingBlock = new HeadingBlock();
		blocks.add(headingBlock);
		paragraphBreakingBlocks.add(headingBlock);
		nestedBlocks.add(headingBlock);
		ListBlock listBlock = new ListBlock();
		blocks.add(listBlock);
		paragraphBreakingBlocks.add(listBlock);
		nestedBlocks.add(listBlock);
		blocks.add(new QuoteBlock());
		TableBlock tableBlock = new TableBlock();
		blocks.add(tableBlock);
		paragraphBreakingBlocks.add(tableBlock);
		nestedBlocks.add(tableBlock);
		ExtendedQuoteBlock quoteBlock = new ExtendedQuoteBlock();
		blocks.add(quoteBlock);
		paragraphBreakingBlocks.add(quoteBlock);
		ExtendedPreformattedBlock noformatBlock = new ExtendedPreformattedBlock();
		blocks.add(noformatBlock);
		paragraphBreakingBlocks.add(noformatBlock);

		blocks.add(new TextBoxBlock(BlockType.PANEL, "panel")); //$NON-NLS-1$
		blocks.add(new TextBoxBlock(BlockType.NOTE, "note")); //$NON-NLS-1$
		blocks.add(new TextBoxBlock(BlockType.INFORMATION, "info")); //$NON-NLS-1$
		blocks.add(new TextBoxBlock(BlockType.WARNING, "warning")); //$NON-NLS-1$
		blocks.add(new TextBoxBlock(BlockType.TIP, "tip")); //$NON-NLS-1$
		CodeBlock codeBlock = new CodeBlock();
		blocks.add(codeBlock);
		paragraphBreakingBlocks.add(codeBlock);
		blocks.add(new TableOfContentsBlock());
		ColorBlock colorBlock = new ColorBlock();
		blocks.add(colorBlock);
		paragraphBreakingBlocks.add(colorBlock);
	}

	@Override
	protected void addStandardPhraseModifiers(PatternBasedSyntax phraseModifierSyntax) {
		phraseModifierSyntax.beginGroup("(?:(?<=[\\s\\.,\\\"'?!;:\\)\\(\\[\\]])|^)(?:", 0); //$NON-NLS-1$
		phraseModifierSyntax.add(new HyperlinkPhraseModifier());
		phraseModifierSyntax.add(new SimplePhraseModifier("*", SpanType.STRONG, true)); //$NON-NLS-1$
		phraseModifierSyntax.add(new EmphasisPhraseModifier());
		phraseModifierSyntax.add(new SimplePhraseModifier("??", SpanType.CITATION, true)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("-", SpanType.DELETED, true)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("+", SpanType.UNDERLINED, true)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("^", SpanType.SUPERSCRIPT, false)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("~", SpanType.SUBSCRIPT, false)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimpleWrappedPhraseModifier("{{", "}}", SpanType.MONOSPACE, false)); //$NON-NLS-1$ //$NON-NLS-2$
		phraseModifierSyntax.add(new ConfluenceWrappedPhraseModifier("{quote}", SpanType.QUOTE, true)); //$NON-NLS-1$ 
		phraseModifierSyntax.add(new SlippImagePhraseModifier());
		phraseModifierSyntax.endGroup(")(?=\\W|$)", 0); //$NON-NLS-1$
	}

	@Override
	protected void addStandardTokens(PatternBasedSyntax tokenSyntax) {
		tokenSyntax.add(new PatternLineBreakReplacementToken("(\\\\\\\\)")); // line break //$NON-NLS-1$
		tokenSyntax.add(new EscapedCharacterReplacementToken()); // ORDER DEPENDENCY must come after line break
		tokenSyntax.add(new EntityReferenceReplacementToken("(tm)", "#8482")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(TM)", "#8482")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(c)", "#169")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(C)", "#169")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(r)", "#174")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(R)", "#174")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new PatternEntityReferenceReplacementToken("(?:(?<=\\w\\s)(---)(?=\\s\\w))", "#8212")); // emdash //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new PatternEntityReferenceReplacementToken("(?:(?<=\\w\\s)(--)(?=\\s\\w))", "#8211")); // endash //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new PatternLiteralReplacementToken("(----)", "<hr/>")); // horizontal rule //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new ImpliedHyperlinkReplacementToken());
		tokenSyntax.add(new AnchorReplacementToken());
	}

	@Override
	protected Block createParagraphBlock() {
		return new SlippParagraphBlock();
	}
}
