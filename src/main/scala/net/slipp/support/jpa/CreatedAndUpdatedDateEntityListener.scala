package net.slipp.support.jpa

import java.util.Date
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

class CreatedAndUpdatedDateEntityListener {
  @PrePersist def prePersist(hcud: HasCreatedAndUpdatedDate) {
    val currentDate: Date = getCurrentDate
    hcud.setCreatedDate(currentDate)
    hcud.setUpdatedDate(currentDate)
  }

  private def getCurrentDate: Date = {
    return new Date
  }

  @PreUpdate def preUpdate(hcud: HasCreatedAndUpdatedDate) {
    val currentDate: Date = getCurrentDate
    hcud.setUpdatedDate(currentDate)
  }
}
