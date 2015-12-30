package net.slipp.domain.qna

import java.util.List
import net.slipp.domain.user.SocialUser

import scala.collection.JavaConversions._

class Answers(answers: List[Answer]) {
  def findFacebookAnswerers: Set[SocialUser] = {
    answers.filter(a => a.isFacebookWriter).map(a => a.getWriter).toSet
  }
}
