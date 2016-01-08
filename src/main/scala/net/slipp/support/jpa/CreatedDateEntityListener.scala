package net.slipp.support.jpa

import java.util.Date
import javax.persistence.PrePersist

class CreatedDateEntityListener {
  @PrePersist
  def prePersist(hcd: HasCreatedDate) {
    hcd.setCreatedDate(new Date)
  }
}
