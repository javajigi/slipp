package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import models.Tag;

import org.apache.commons.lang.StringUtils;

public class TagHelper {
	public static List<String> parse(String tagNames) {
		if (StringUtils.isBlank(tagNames)){
			return new ArrayList<String>();
		}
		
		Pattern p = Pattern.compile("[\\s,;]+");
		String[] splitedTagNames = p.split(tagNames);
		return Arrays.asList(splitedTagNames);
	}

	public static List<Tag> getTagsFromName(List<String> tagNames) throws TagNotFoundException {
		List<Tag> tags = new ArrayList<Tag>();
		for (String tagName : tagNames) {
			Tag tag = Tag.findByName(tagName);
			if (tag==null){
				throw new TagNotFoundException(tagName + "은 사용할 수 없는 태그입니다. SLiPP은 예약된 태그만 사용할 수 있습니다.");
			}
			tags.add(tag);
		}
		return tags;
	}
}
