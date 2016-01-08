package net.slipp.domain.tag

import java.util.Set
import javax.persistence.{Column, Embedded, Entity, FetchType, ForeignKey, GeneratedValue, GenerationType, Id, JoinColumn, ManyToMany, OneToOne}

import com.google.common.collect.Sets
import net.slipp.domain.qna.Question
import net.slipp.support.jpa.DomainModel
import org.hibernate.annotations.{Cache, CacheConcurrencyStrategy}

object Tag {
  def pooledTag(name: String): Tag = {
    return pooledTag(name, null)
  }

  def pooledTag(name: String, parent: Tag): Tag = {
    return new Tag(name.toLowerCase, parent, null)
  }

  def newTag(name: String): Tag = {
    return new Tag(name.toLowerCase, null, null)
  }

  def groupedTag(name: String, groupId: String): Tag = {
    return new Tag(name.toLowerCase, null, new TagInfo(groupId, name))
  }
}

@Entity
@Cache(region = "tag", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Tag(n: String, p: Tag, info: TagInfo) {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private var tagId: Long = _

  @Column(name = "name", length = 50, unique = true, nullable = false)
  private var name: String = n

  private var taggedCount: Int = 0

  @Column(name = "pooled", nullable = false)
  private var pooled: Boolean = true

  @OneToOne
  @JoinColumn(foreignKey = new ForeignKey(name = "fk_tag_parent_id"))
  private var parent: Tag = p

  @Embedded
  private var tagInfo: TagInfo = info

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
  private var questions: Set[Question] = Sets.newHashSet[Question]

  def this() = this(null, null, null)

  def getTagId: Long = {
    return tagId
  }

  def setTagId(tagId: Long) {
    this.tagId = tagId
  }

  def getName: String = {
    return name
  }

  def setName(name: String) {
    this.name = name
  }

  def setTaggedCount(taggedCount: Int) {
    this.taggedCount = taggedCount
  }

  def getTaggedCount: Int = {
    return taggedCount
  }

  def isPooled: Boolean = {
    return pooled
  }

  def isTagged: Boolean = {
    return taggedCount > 0
  }

  def tagged() {
    taggedCount += 1
  }

  def deTagged() {
    taggedCount -= 1
  }

  def getParent: Tag = {
    return this.parent
  }

  def getTagInfo: TagInfo = {
    return tagInfo
  }

  def getGroupId: String = {
    if (tagInfo == null) {
      return null
    }
    return tagInfo.getGroupId
  }

  private def isRootTag: Boolean = {
    return parent == null
  }

  def movePooled(parent: Tag) {
    this.pooled = true
    this.parent = parent
    import scala.collection.JavaConversions._
    for (each <- questions) {
      each.tagsToDenormalizedTags
    }
  }

  def moveGroupTag(groupId: String): Tag = {
    this.pooled = true
    this.tagInfo = new TagInfo(groupId, this.name)
    return this
  }

  def isConnectGroup: Boolean = {
    if (tagInfo == null) {
      return false
    }
    return tagInfo.isConnectGroup
  }

  /**
    * Root 태그인 경우 자기 자신, 자식 태그인 경우 부모 태그를 반환한다.
    *
    * @return
    */
  def getRevisedTag: Tag = {
    if (isRootTag) {
      return this
    }
    return this.parent
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Tag]

  override def equals(other: Any): Boolean = other match {
    case that: Tag =>
      (that canEqual this) &&
        tagId == that.tagId &&
        name == that.name
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(tagId, name)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Tag($tagId, $name, $taggedCount, $pooled, $parent, $tagInfo)"
}
