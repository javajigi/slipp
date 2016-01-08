package net.slipp.support.utils

import java.util.Calendar

object RankingUtils {
  def calculateHotScore(comments: Int, likes: Int, standard: Calendar, date: Calendar): Double = {
    val score = calculateScore(comments, likes)
    val order: Double = Math.log10(Math.max(Math.abs(score), 1))
    val seconds: Long = epochSeconds(standard, date) - 1134028003
    val result: Double = order + seconds / 45000
    return result * 10000000.0.round / 10000000.0
  }

  private def calculateScore(comments: Int, likes: Int): Int = {
    return comments + likes
  }

  private[utils] def epochSeconds(standard: Calendar, date: Calendar): Long = {
    val differenceMillis: Long = date.getTimeInMillis - standard.getTimeInMillis
    return differenceMillis / 1000
  }

  def calculateCommentRanking(ups: Int, downs: Int): Double = {
    val number: Int = ups + downs
    if (number == 0) {
      return 0
    }
    val n: Int = ups + downs
    val z: Double = 1.0
    val phat: Float = new Integer(ups).floatValue / n
    return Math.sqrt(phat + z * z / (2 * n) - z * ((phat * (1 - phat) + z * z / (4 * n)) / n)) / (1 + z * z / n)
  }
}

