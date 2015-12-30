package net.slipp.support.jpa

import java.util.Date

trait HasCreatedDate {
  def setCreatedDate(createdDate: Date)

  def getCreatedDate: Date
}
