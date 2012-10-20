
package net.slipp.qna;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class NewTagsPage {
	private WebDriver driver;
	
	public NewTagsPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean existNewTag(String newTag) {
		List<String> newTags = findAllNewTags();
		return newTags.contains(newTag);
	}

	private List<String> findAllNewTags() {
		List<String> newTags = Lists.newArrayList();
		List<WebElement> tagElements = driver.findElements(By.xpath("//tbody/tr/td[2]"));
		for (WebElement tagElement : tagElements) {
			newTags.add(tagElement.getText());
		}
		return newTags;
	}

}
