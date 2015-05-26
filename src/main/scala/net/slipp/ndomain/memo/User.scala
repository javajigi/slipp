package net.slipp.ndomain.memo

import java.util.Date
import javax.persistence.Entity
import javax.persistence.Id

import org.hibernate.annotations.BatchSize
import net.slipp.support.jpa.DomainModel

@Entity(name = "user")
class User(i: String, p: String, n: String) extends DomainModel {
  @Id
  var id: String = i
  val name = n
  val password = p

  val created = new Date()
  val updated = new Date()

  def this() = this("", "", "")
}
