package net.slipp.ndomain.memo

import javax.persistence._

import net.slipp.support.jpa.DomainModel
import net.slipp.support.jpa.DateModel

// @Entity(name = "memo")
class Memo(b: MemoBook, t: String) extends DomainModel with DateModel {
  @Id
  @GeneratedValue
  var id: Long = _

  // @ManyToOne(cascade = Array(), fetch = FetchType.LAZY)
  @JoinColumn(name = "memoBookId")
  val memoBook = b

  val text = t

  def memoBookId = memoBook.id

  def userId = memoBook.user.id

  def this() = this(null, "")
}
