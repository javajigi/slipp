package net.slipp.support.jpa

import java.util.Date
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

class UpdatedDateEntityListener {
  @PreUpdate
  @PrePersist
  def prePersistAndUpdate(hud: HasUpdatedDate) {
    hud.setUpdatedDate(new Date)
  }
}
