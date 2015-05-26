package net.slipp.support.jpa

import java.util.Date

import javax.persistence.Column
import javax.persistence.Temporal
import javax.persistence.TemporalType

trait NHasCreatedDate {
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)  
  val createdDate = new Date
}