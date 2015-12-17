package net.slipp.service.qna

import net.slipp.domain.tag.TagTest.JAVA
import net.slipp.domain.tag.TagTest.NEWTAG
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import java.util.HashSet
import java.util.Set
import net.slipp.domain.qna.Answer
import net.slipp.domain.qna.Question
import net.slipp.domain.qna.QuestionDto
import net.slipp.domain.tag.Tag
import net.slipp.domain.user.SocialUser
import net.slipp.repository.qna.AnswerRepository
import net.slipp.repository.qna.QuestionRepository
import net.slipp.service.rank.ScoreLikeService
import net.slipp.service.tag.TagService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.security.access.AccessDeniedException
import com.google.common.collect.{Lists, Sets}

@RunWith(classOf[MockitoJUnitRunner])
class QnaServiceTest {
  @Mock private var questionRepository: QuestionRepository = null
  @Mock private var answerRepository: AnswerRepository = null
  @Mock private var tagService: TagService = null
  @Mock private var scoreLikeService: ScoreLikeService = null
  @InjectMocks private var dut: QnaService = new QnaService

  @Test def updateQuestion_sameUser {
    val loginUser: SocialUser = new SocialUser(10)
    val dto: QuestionDto = new QuestionDto(1L, "title", "contents", "java javascript")
    val originalTags: Set[Tag] = Sets.newHashSet(JAVA)
    val existedQuestion: Question = new Question(1L, loginUser, dto.getTitle, dto.getContents, originalTags)
    when(questionRepository.findOne(dto.getQuestionId)).thenReturn(existedQuestion)
    val tags: Set[Tag] = Sets.newHashSet(JAVA, NEWTAG)
    when(tagService.processTags(dto.getPlainTags)).thenReturn(tags)

    dut.updateQuestion(loginUser, dto)
  }

  @Test
  def deleteAnswer_sameUser {
    val loginUser: SocialUser = new SocialUser(10)
    val answer: Answer = new Answer(2L)
    answer.writedBy(loginUser)
    val answer2: Answer = new Answer(3L)
    val question: Question = new Question(1L, loginUser, null, null, new HashSet[Tag])
    question.setAnswers(Lists.newArrayList(answer, answer2))

    when(answerRepository.findOne(answer.getAnswerId)).thenReturn(answer)
    when(questionRepository.findOne(question.getQuestionId)).thenReturn(question)

    dut.deleteAnswer(loginUser, question.getQuestionId, answer.getAnswerId)
  }

  @Test(expected = classOf[AccessDeniedException])
  def deleteAnswer_differentUser {
    val loginUser: SocialUser = new SocialUser(10)
    val questionId: Long = 1L
    val answer: Answer = new Answer(2L)
    answer.writedBy(new SocialUser(11))
    when(answerRepository.findOne(answer.getAnswerId)).thenReturn(answer)
    dut.deleteAnswer(loginUser, questionId, answer.getAnswerId)
  }

  @Test
  def likeAnswer_ {
    val loginUser: SocialUser = new SocialUser(10)
    val answer: Answer = new Answer(2L)
    answer.writedBy(loginUser)
    when(answerRepository.findOne(answer.getAnswerId)).thenReturn(answer)
    dut.likeAnswer(loginUser, answer.getAnswerId)
  }
}
