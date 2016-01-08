package net.slipp.support.wiki

import org.pegdown.Extensions
import org.pegdown.PegDownProcessor

object WikiContents {
  def parse(contents: String): String = {
    return new PegDownProcessor(Extensions.FENCED_CODE_BLOCKS).markdownToHtml(contents)
  }
}

