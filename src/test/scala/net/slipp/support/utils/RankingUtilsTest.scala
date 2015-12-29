package net.slipp.support.utils

import java.util.Calendar
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
  * http://amix.dk/blog/post/19588 문서에 따른 랭킹을 자바로 구현
  *
  * @author javajigi
  */
class RankingUtilsTest {
  private var logger: Logger = LoggerFactory.getLogger(classOf[RankingUtilsTest])

  @Test
  @throws(classOf[Exception])
  def caculateScore {
    val standard: Calendar = Calendar.getInstance
    standard.set(1970, 0, 1)
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(0)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 1, standard, createCalendar(-5)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-10)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-11)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-12)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-13)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-50)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-100)))
    logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-1000)))
  }

  private def createCalendar(amount: Int): Calendar = {
    val date: Calendar = Calendar.getInstance
    date.add(Calendar.HOUR_OF_DAY, amount)
    return date
  }

  @Test
  @throws(classOf[Exception])
  def epochSeconds {
    val standard: Calendar = Calendar.getInstance
    standard.set(2012, 5, 6)
    logger.debug("standard : " + standard.getTime)
    val now: Calendar = Calendar.getInstance
    logger.debug("now : " + now.getTime)
    logger.debug("epochSeconds : " + RankingUtils.epochSeconds(standard, now))
  }

  @Test
  @throws(classOf[Exception])
  def calculateCommentRanking {
    logger.debug("result : " + RankingUtils.calculateCommentRanking(1, 0))
    logger.debug("result : " + RankingUtils.calculateCommentRanking(10, 1))
    logger.debug("result : " + RankingUtils.calculateCommentRanking(40, 20))
    logger.debug("result : " + RankingUtils.calculateCommentRanking(100, 40))
  }
}
