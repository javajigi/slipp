package net.slipp.domain.tag

import javax.persistence._

import net.slipp.support.jpa.{DomainModel, NHasCreatedDate}

@Entity(name="TaggedHistory")
@Table(indexes = Array(
  new Index(name = "idx_tagged_history_tag", columnList="tag_id"),
  new Index(name = "idx_tagged_history_question", columnList="question_id")))
class TaggedHistory(t: Long, q: Long, u: Long, tType: TaggedType) extends DomainModel with NHasCreatedDate {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var historyId: Long = _
  
  @Column(name = "tag_id", nullable = false, updatable = false)
  val tagId = t
  
  @Column(name = "question_id", nullable = false, updatable = false)
  val questionId = q
  
  @Column(name = "user_id", nullable = false, updatable = false)
  val userId = u

  @Enumerated(EnumType.STRING)
  @Column(name = "tagged_type", nullable = false, updatable = false, columnDefinition = "enum('TAGGED','DETAGGED')")
  val taggedType = tType
  
  def this() = this(0L, 0L, 0L, null)
}