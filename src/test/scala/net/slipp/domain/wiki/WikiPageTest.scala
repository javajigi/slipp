package net.slipp.domain.wiki

import java.sql.Timestamp

import org.junit.Test

class WikiPageTest {
  @Test def create() {
    val wikiPage = new WikiPage(1L, "title", new Timestamp(System.currentTimeMillis()), "contents")
    assert(wikiPage.getTitle == "title")
  }

}
