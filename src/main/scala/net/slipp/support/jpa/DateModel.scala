package net.slipp.support.jpa

import java.util.Date

trait DateModel {
  val created = new Date
  var updated = new Date

  /**
   * This method should be called when domain itself is changed.
   * For example, you can call this when a note is added to a notebook.
   *
   * In following situation, you should not call this.
   *
   * 1. Sub domains(1:1 or 1:N) is changed.
   * 2. De-normalized value is changed.
   */
  def markAsUpdated() = updated = new Date()
}