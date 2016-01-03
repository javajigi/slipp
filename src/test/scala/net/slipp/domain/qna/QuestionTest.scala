package net.slipp.domain.qna

import com.google.common.collect.Sets
import net.slipp.domain.ProviderType
import net.slipp.domain.qna.AnswerBuilder._
import net.slipp.domain.qna.QuestionBuilder._
import net.slipp.domain.tag.Tag
import net.slipp.domain.tag.TagBuilder._
import net.slipp.domain.tag.TagTest._
import net.slipp.domain.user.{SocialUser, SocialUserBuilder}
import net.slipp.repository.tag.TagRepository
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.{Before, Test}
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(classOf[MockitoJUnitRunner])
class QuestionTest {
  private var dut: Question = null
  @Mock private var tagRepository: TagRepository = null

  @Before def setup {
    dut = new Question
  }

  @Test def 질문한_사람이_같다 {
    val writer: SocialUser = new SocialUser(10L)
    dut = aQuestion.withWriter(writer).build
    assertThat(dut.isWritedBy(writer), is(true))
  }

  @Test def 질문한_사람이_다르다 {
    val writer: SocialUser = new SocialUser(10L)
    dut = aQuestion.withWriter(writer).build
    val actual: Boolean = dut.isWritedBy(new SocialUser(11L))
    assertThat(actual, is(false))
  }

  @Test
  @throws(classOf[Exception])
  def 새로운_질문 {
    val writer: SocialUser = new SocialUser
    val title: String = "title"
    val contents: String = "contents"
    val java: Tag = JAVA
    val newQuestion: Question = new Question(writer, title, contents, Sets.newHashSet(java))
    assertThat(newQuestion.getTitle, is(title))
    assertThat(newQuestion.getContents, is(contents))
    assertThat(newQuestion.hasTag(java), is(true))
    assertThat(newQuestion.getDenormalizedTags, is(java.getName))
  }

  @Test
  @throws(classOf[Exception])
  def 질문_수정 {
    val writer: SocialUser = new SocialUser
    val java: Tag = JAVA
    val newQuestion: Question = new Question(writer, "title", "contents", Sets.newHashSet(java))
    val updateTitle: String = "update title"
    val updateContents: String = "update contents"
    val javascript: Tag = JAVASCRIPT
    newQuestion.update(writer, updateTitle, updateContents, Sets.newHashSet(java, javascript))
    assertThat(newQuestion.getTitle, is(updateTitle))
    assertThat(newQuestion.getContents, is(updateContents))
    assertThat(newQuestion.hasTag(java), is(true))
  }

  @Test
  def 화제의_답변이_존재한다 {
    val dut: Question = aQuestion.withAnswer(anAnswer.withTotalLiked(1).build).withAnswer(anAnswer.withTotalLiked(0).build).withAnswer(anAnswer.withTotalLiked(3).build).build
    val bestAnswer: Answer = dut.getBestAnswer
    assertThat(bestAnswer.getSumLike, is(new Integer(3)))
  }

  @Test
  def 화제의_답변이_존재하지_않는다 {
    val dut: Question = aQuestion.withAnswer(anAnswer.withTotalLiked(1).build).withAnswer(anAnswer.withTotalLiked(0).build).build
    assertThat(dut.getBestAnswer, is(nullValue))
  }

  @Test
  def 답변이_존재하지_않는다 {
    val dut: Question = aQuestion.build
    assertThat(dut.getBestAnswer, is(nullValue))
  }

  @Test
  @throws(classOf[Exception])
  def 질문에_답변을_한다 {
    val writer1: SocialUser = new SocialUser(10L)
    val dut: Question = aQuestion.withWriter(writer1).build
    assertThat(dut.getLatestParticipant, is(writer1))
    val writer2: SocialUser = new SocialUser(11L)
    val answer: Answer = anAnswer.`with`(writer2).build
    dut.newAnswered(answer)
    assertThat(dut.getLatestParticipant, is(writer2))
  }

  @Test
  @throws(classOf[Exception])
  def move_answers {
    val answer1: Answer = anAnswer(1L).build
    val answer2: Answer = anAnswer(2L).build
    val answer3: Answer = anAnswer(3L).build
    val writer1: SocialUser = new SocialUser(10L)
    val dut: Question = aQuestion(1L).withWriter(writer1).withAnswer(answer1).withAnswer(answer2).withAnswer(answer3).build
    assertThat(dut.getAnswerCount, is(3))
    val newQuestion: Question = aQuestion(2L).build
    dut.moveAnswers(newQuestion, Array(1L, 3L))
    assertThat(answer1.getQuestion, is(newQuestion))
    assertThat(answer3.getQuestion, is(newQuestion))
  }

  @Test
  @throws(classOf[Exception])
  def delete_answer_when_two_answer {
    val writer2: SocialUser = new SocialUser(11L)
    val answer1: Answer = anAnswer.`with`(writer2).build
    val writer3: SocialUser = new SocialUser(12L)
    val answer2: Answer = anAnswer.`with`(writer3).build
    val writer1: SocialUser = new SocialUser(10L)
    val dut: Question = aQuestion.withWriter(writer1).withAnswer(answer1).withAnswer(answer2).build
    assertThat(dut.getAnswerCount, is(2))
    dut.deAnswered(answer2)
    assertThat(dut.getAnswerCount, is(1))
    assertThat(dut.getLatestParticipant, is(writer2))
  }

  @Test def 질문을_삭제한다 {
    val java: Tag = aTag.withName("java").build
    val writer: SocialUser = new SocialUser
    val dut: Question = aQuestion.withWriter(writer).withTag(java).build
    dut.delete(writer)
    assertThat(dut.isDeleted, is(true))
    assertThat(java.getTaggedCount, is(0))
  }

  @Test
  @throws(classOf[Exception])
  def detaggedTags {
    val java: Tag = aTag.withName("java").withTaggedCount(3).build
    val javascript: Tag = aTag.withName("javascript").withTaggedCount(2).build
    val originalTags = Sets.newHashSet(java, javascript)
    Question.detaggedTags(originalTags)
    assertThat(java.getTaggedCount, is(2))
    assertThat(javascript.getTaggedCount, is(1))
  }

  @Test
  @throws(classOf[Exception])
  def taggedTags {
    val java: Tag = aTag.withName("java").withTaggedCount(3).build
    val javascript: Tag = aTag.withName("javascript").withTaggedCount(2).build
    val originalTags = Sets.newHashSet(java, javascript)
    Question.taggedTags(originalTags)
    assertThat(java.getTaggedCount, is(4))
    assertThat(javascript.getTaggedCount, is(3))
  }

  @Test
  @throws(classOf[Exception])
  def taggedTag {
    val java: Tag = aTag.withName("java").withTaggedCount(3).build
    val javascript: Tag = aTag.withName("javascript").withTaggedCount(2).build
    val writer: SocialUser = new SocialUser
    val dut: Question = aQuestion.withWriter(writer).withTag(java).build
    dut.taggedTag(javascript)
    assertThat(dut.getTags.size, is(2))
    assertThat(javascript.getTaggedCount, is(3))
    assertTrue(dut.getDenormalizedTags.contains("javascript"))
  }

  @Test
  @throws(classOf[Exception])
  def deTaggedTag {
    val java: Tag = aTag.withName("java").build
    val javascript: Tag = aTag.withName("javascript").build
    val writer: SocialUser = new SocialUser
    val dut: Question = aQuestion.withWriter(writer).withTag(java).withTag(javascript).build
    dut.detaggedTag(javascript)
    assertThat(dut.getTags.size, is(1))
    assertThat(javascript.getTaggedCount, is(0))
    assertTrue(!dut.getDenormalizedTags.contains("javascript"))
  }

  @Test
  @throws(classOf[Exception])
  def connected {
    val writer: SocialUser = new SocialUser(10L)
    writer.setProviderId("facebook")
    dut = aQuestion.withWriter(writer).build
    val postId: String = "123456"
    val actual: SnsConnection = dut.connected(postId)
    assertThat(dut.getSnsConnection.size, is(1))
    val expected: SnsConnection = new SnsConnection(ProviderType.facebook, postId)
    assertThat(actual, is(expected))
    dut.connected(postId)
    assertThat(dut.getSnsConnection.size, is(1))
  }

  @Test
  @throws(classOf[Exception])
  def getTotalAnswerCount {
    val writer1: SocialUser = new SocialUserBuilder().withProviderType(ProviderType.facebook).build
    val dut: Question = aQuestion.withWriter(writer1).withAnswer(new Answer).withAnswer(new Answer).build
    val connection: SnsConnection = dut.connected("12345")
    connection.updateAnswerCount(3)
    assertThat(dut.getTotalAnswerCount, is(5))
  }
}
