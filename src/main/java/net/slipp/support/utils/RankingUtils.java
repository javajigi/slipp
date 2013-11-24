package net.slipp.support.utils;

import java.util.Calendar;

public class RankingUtils {
    public static double calculateHotScore(int comments, int likes, Calendar standard, Calendar date) {
        int score = score(comments, likes);
        double order = Math.log10(Math.max(Math.abs(score), 1));
        long seconds = epochSeconds(standard, date) - 1134028003;
        double result = order + seconds / 45000;
        return Math.round(result * 10000000.0) / 10000000.0;
    }

    private static int score(int comments, int likes) {
        return comments + likes;
    }

    static long epochSeconds(Calendar standard, Calendar date) {
        long differenceMillis = date.getTimeInMillis() - standard.getTimeInMillis();
        return differenceMillis / 1000;
    }

    public static double calculateCommentRanking(int ups, int downs) {
        int number = ups + downs;
        if (number == 0) {
            return 0;
        }

        int n = ups + downs;
        double z = 1.0;
        float phat = new Integer(ups).floatValue() / n;
        return Math.sqrt(phat + z * z / (2 * n) - z * ((phat * (1 - phat) + z * z / (4 * n)) / n)) / (1 + z * z / n);
    }
}
