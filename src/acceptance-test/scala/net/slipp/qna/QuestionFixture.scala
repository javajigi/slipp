package net.slipp.qna

import scala.beans.BeanProperty

class QuestionFixture(t: String = "DEFAULT - 지속 가능한 삶, 프로그래밍, 프로그래머",
                      c: String = "DEFAULT - 이 공간은 삶과 일의 균형을 맞추면서 지속 가능한 삶을 살아갈 것인가에 고민을 담기 위한 곳이다.",
                      p: String = "java javascript") {
  @BeanProperty
  var title = t

  @BeanProperty
  var contents = c

  @BeanProperty
  var plainTags = p
}

