package net.slipp.support.utils;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://amix.dk/blog/post/19588 문서에 따른 랭킹을 자바로 구현
 *  
 * @author javajigi
 */
public class RankingUtilsTest {
	private static Logger logger = LoggerFactory.getLogger(RankingUtilsTest.class);
	
	@Test
	public void caculateScore() throws Exception {
		Calendar standard = Calendar.getInstance();
		standard.set(1970, 0, 1);
		
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(0)));
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-5)));
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-10)));	
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-11)));	
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-12)));	
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-13)));	
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-50)));	
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-100)));
		logger.debug("score : {}", RankingUtils.calculateHotScore(10, 0, standard, createCalendar(-1000)));
	}

	private Calendar createCalendar(int amount) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.HOUR_OF_DAY, amount);
		return date;
	}
	
	@Test
	public void epochSeconds() throws Exception {
		Calendar standard = Calendar.getInstance();
		standard.set(2012, 5, 6);
		logger.debug("standard : " + standard.getTime());
		Calendar now = Calendar.getInstance();
		logger.debug("now : " + now.getTime());
		logger.debug("epochSeconds : " + RankingUtils.epochSeconds(standard, now));
	}
	
	@Test
	public void calculateCommentRanking() throws Exception {
		logger.debug("result : " + RankingUtils.calculateCommentRanking(1, 0));
		logger.debug("result : " + RankingUtils.calculateCommentRanking(10, 1));
		logger.debug("result : " + RankingUtils.calculateCommentRanking(40, 20));
		logger.debug("result : " + RankingUtils.calculateCommentRanking(100, 40));
	}
}
