package net.slipp.service.tag;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import net.slipp.domain.tag.Tag;

public class TagHelper {

	public static String denormalizedTags(Set<Tag> tags) {
		if (tags == null) {
            return null;
        }

        Function<Tag, String> tagToString = new Function<Tag, String>() {
            @Override
            public String apply(Tag input) {
                return input.getName();
            }
        };

        return Joiner.on(",").join(Collections2.transform(tags, tagToString));
	}
}
