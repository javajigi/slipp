package net.slipp.support.wiki.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.slipp.support.wiki.SlippLanguage;
import net.slipp.support.wiki.WikiContents;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectFactory;

/**
 * Wiki 구문을 HTML로 변경해준다.
 */
public class WikiHtmlTag extends SimpleTagSupport {

	private static ObjectFactory<SlippLanguage> slippWikiMarkupLanguageObjectFactory;

	/** 마크업 문자열 */
	private String contents;

	public void setArcheageWikiMarkupLanguageObjectFactory(
			ObjectFactory<SlippLanguage> slippWikiMarkupLanguageObjectFactory) {
		WikiHtmlTag.slippWikiMarkupLanguageObjectFactory = slippWikiMarkupLanguageObjectFactory;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		if (StringUtils.isBlank(contents)) {
			return;
		}

		printHtml(out);
	}

	private void printHtml(JspWriter out) throws IOException {
	    String parsedContents = WikiContents.parse(contents, getMarkupLanguage());
	    out.write(parsedContents);
	}

	private SlippLanguage getMarkupLanguage() {
		return slippWikiMarkupLanguageObjectFactory.getObject();
	}
}
