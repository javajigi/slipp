package net.slipp.support.utils

import java.util.Date

object TimeUtils {
  val ONE_SECOND: Long = 1000
  val SECONDS: Long = 60
  val ONE_MINUTE: Long = ONE_SECOND * 60
  val MINUTES: Long = 60
  val ONE_HOUR: Long = ONE_MINUTE * 60
  val HOURS: Long = 24
  val ONE_DAY: Long = ONE_HOUR * 24

  /**
    * converts time (in milliseconds) to human-readable format
    * "<w> days, <x> hours, <y> minutes and (z) seconds"
    */
  def millisToLongDHMS(d: Long): String = {
    val res: StringBuffer = new StringBuffer
    var temp: Long = 0
    var duration = d
    if (duration >= ONE_SECOND) {
      temp = duration / ONE_DAY
      if (temp > 0) {
        duration = temp * ONE_DAY
        res.append(temp).append(" day").append(if (temp > 1) "s" else "").append(if (duration >= ONE_MINUTE) ", " else "")
      }
      temp = duration / ONE_HOUR
      if (temp > 0) {
        duration -= temp * ONE_HOUR
        res.append(temp).append(" hour").append(if (temp > 1) "s" else "").append(if (duration >= ONE_MINUTE) ", " else "")
      }
      temp = duration / ONE_MINUTE
      if (temp > 0) {
        duration -= temp * ONE_MINUTE
        res.append(temp).append(" minute").append(if (temp > 1) "s" else "")
      }
      if (!(res.toString == "") && duration >= ONE_SECOND) {
        res.append(" and ")
      }
      temp = duration / ONE_SECOND
      if (temp > 0) {
        res.append(temp).append(" second").append(if (temp > 1) "s" else "")
      }
      return res.toString
    }
    else {
      return "0 second"
    }
  }

  def diffDay(d1: Date, d2: Date): Int = {
    return (d2.getTime - d1.getTime).toInt / (24 * 60 * 60 * 1000)
  }

  def agoTime(d: Date): String = {
    val now: Date = new Date
    val duration: Long = now.getTime - d.getTime
    return millisToLongDHMS(duration)
  }
}
