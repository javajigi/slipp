package net.slipp.service.wiki

import java.sql.Timestamp
import java.util.Date
import javax.annotation.Resource

import com.google.common.collect.Lists
import net.slipp.domain.wiki.{WikiDao, WikiPage}
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WikiService {
  @Resource(name = "wikiDao") private var wikiDao: WikiDao = null

  @Cacheable(value = Array("blogPages"))
  def findWikiPages = {
    wikiDao.findWikiPages
  }

  def findDummyWikiPages = {
    val wikiPages = Lists.newArrayList[WikiPage]
    (0 to 3).foreach(_ => {
      wikiPages.add(new WikiPage(1L, "테스트 입니다.", new Timestamp(new Date().getTime), "요즘 2학기 수업 준비하는데 온 신경을 집중하다보니 책을 읽어도 책 내용이 잘 읽히지 않고, 집중이 잘 안된다. 이 책을 읽는데도 2주 이상의 시간이 흘렀다. 그 만큼 다른 것에 정신이 팔려있는 요즘이다. 더딘 속도이기는 하지만 그래도 책을 끝까지 읽었다. 최근에는 책"))
    })
    wikiPages
  }
}
