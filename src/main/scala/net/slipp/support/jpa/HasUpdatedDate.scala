package net.slipp.support.jpa

import java.util.Date

trait HasUpdatedDate {
  def setUpdatedDate(updatedDate: Date)

  def getUpdatedDate: Date
}
