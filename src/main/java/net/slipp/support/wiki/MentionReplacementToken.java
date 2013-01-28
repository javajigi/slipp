package net.slipp.support.wiki;

import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.LinkAttributes;
import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElement;
import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElementProcessor;

public class MentionReplacementToken extends PatternBasedElement {

    @Override
    protected String getPattern(int groupOffset) {
        return "@([a-zA-Z가-힣.@]*)";
    }

    @Override
    protected int getPatternGroupCount() {
        return 1;
    }

    @Override
    protected PatternBasedElementProcessor newProcessor() {
        return new MentionReplacementTokenProcessor();
    }

    private static class MentionReplacementTokenProcessor extends PatternBasedElementProcessor {
        @Override
        public void emit() {
            String name = group(1);
            Attributes attributes = new LinkAttributes();
            attributes.setCssClass("author-name");
            getBuilder().link(attributes, "http://facebook.com/profile.php?id=1324855987", name);
        }
    }
}
