package net.slipp.domain.memo

import java.util
import javax.persistence._
import scala.collection.JavaConversions._

import org.hibernate.annotations.BatchSize

// @Entity(name = "memoBook")
class MemoBook(u: User) extends DomainModel with DateModel {
  @Id
  @GeneratedValue
  var id: Long = _

  // @OneToOne(cascade = Array(CascadeType.PERSIST), optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  val user = u

  @BatchSize(size = 30)
  // @OneToMany(mappedBy = "memoBook", cascade = Array(CascadeType.PERSIST, CascadeType.REMOVE), fetch = FetchType.LAZY)
  val memos: util.List[Memo] = new util.ArrayList[Memo]

  def addMemo(memo: String) = memos.add(new Memo(this, memo))

  def count = memos.size

  def firstMemo = memos.head

  def lastMemo = memos.last

  def this() = this(null)
}
