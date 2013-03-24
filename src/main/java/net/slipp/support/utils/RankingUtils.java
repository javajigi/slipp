package net.slipp.support.utils;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RankingUtils {
	private static Logger logger = LoggerFactory.getLogger(RankingUtils.class);
	
	public static double calculateHotScore(int ups, int downs, Calendar standard, Calendar date) {
		int score = score(ups, downs);
		double order = Math.log10(Math.max(Math.abs(score), 1));
		int sign = getSign(score);
		long seconds = epochSeconds(standard, date) - 1134028003;
		double result = order + (sign*seconds)/45000;
		logger.debug("result : {}", result);
		return Math.round(result * 10000000.0)/10000000.0;
	}

	private static int getSign(int score) {
		if (score > 0) {
			return 1;
		} else if (score < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public static int score(int ups, int downs) {
		return ups - downs;
	}
	
	public static long epochSeconds(Calendar standard, Calendar date) {
		long differenceMillis = date.getTimeInMillis() - standard.getTimeInMillis();
		return differenceMillis/1000;
	}

	public static double calculateCommentRanking(int ups, int downs) {
		int number = ups + downs;
		if (number == 0) {
			return 0;
		}
		
		int n = ups + downs;
		double z = 1.0;
		float phat = new Integer(ups).floatValue() / n;
		return Math.sqrt(phat+z*z/(2*n)-z*((phat*(1-phat)+z*z/(4*n))/n))/(1+z*z/n);
	}
}
