package net.slipp.support.utils

import java.security.MessageDigest

object MD5Util {
  def hex(array: Array[Byte]) = {
    val sb = new StringBuilder
    (0 until array.length).foreach(i => {
      sb.append(Integer.toHexString((array(i) & 0xFF) | 0x100).substring(1, 3))
    })
    sb.toString
  }

  def md5Hex(message: String) = {
      val md: MessageDigest = MessageDigest.getInstance("MD5")
      hex(md.digest(message.getBytes("CP1252")))
  }
}

