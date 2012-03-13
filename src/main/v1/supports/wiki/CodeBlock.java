/*******************************************************************************
 * Copyright (c) 2007, 2009 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package supports.wiki;

import org.eclipse.mylyn.internal.wikitext.confluence.core.block.AbstractConfluenceDelimitedBlock;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;

/**
 * @author David Green
 */
public class CodeBlock extends AbstractConfluenceDelimitedBlock {
	private static final String DEFAULT_LANGUAGE = "java";
	
	private String title;

	private String language = DEFAULT_LANGUAGE;

	public CodeBlock() {
		super("code"); //$NON-NLS-1$
	}

	@Override
	protected void beginBlock() {
		if (title != null) {
			Attributes attributes = new Attributes();
			attributes.setTitle(title);
			builder.beginBlock(BlockType.PANEL, attributes);
		}
		Attributes preAttributes = new Attributes();
		preAttributes.setCssClass("brush: " + language + ";");
		
		builder.beginBlock(BlockType.PREFORMATTED, preAttributes);
	}

	@Override
	protected void handleBlockContent(String content) {
		builder.characters(content);
		builder.characters("\n"); //$NON-NLS-1$
	}

	@Override
	protected void endBlock() {
		if (title != null) {
			builder.endBlock(); // panel	
		}
		builder.endBlock(); // pre
	}

	@Override
	protected void resetState() {
		super.resetState();
		title = null;
	}

	@Override
	protected void setOption(String key, String value) {
		if (key.equals("title")) { //$NON-NLS-1$
			title = value;
		}
	}

	@Override
	protected void setOption(String option) {
		language = option.toLowerCase();
	}
}
