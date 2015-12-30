package net.slipp.domain.qna

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Map
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import net.slipp.domain.user.SocialUser
import net.slipp.support.jpa.CreatedDateEntityListener
import net.slipp.support.jpa.HasCreatedDate
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.hibernate.annotations.GenericGenerator

object Attachment {
  val ATTACHMENT_DOWNLOAD_PREFIX: String = "/attachments"
  val IMAGE_EXTENSIONS: Array[String] = Array("jpg", "jpeg", "gif", "png")
}

@Entity
@Table(name = "attachment")
@EntityListeners(Array(classOf[CreatedDateEntityListener]))
class Attachment(i: String) extends HasCreatedDate {
  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private var id: String = i
  @ManyToOne
  @JoinColumn(name = "uploader", foreignKey = new ForeignKey(name = "fk_attachment_writer"))
  private var uploader: SocialUser = null

  @Column(name = "original_filename", length = 255, nullable = false, updatable = false)
  private var originalFilename: String = null

  @Column(name = "extension", length = 8, nullable = false, updatable = false)
  private var extension: String = null

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false, updatable = false)
  private var createdDate: Date = null

  def this() = this(null)

  def setId(id: String) {
    this.id = id
  }

  def getId: String = {
    return id
  }

  def setUploader(uploader: SocialUser) {
    this.uploader = uploader
  }

  def getUploader: SocialUser = {
    return uploader
  }

  def setOriginalFilename(originalFilename: String) {
    val tempExtension: String = FilenameUtils.getExtension(originalFilename)
    if (StringUtils.isBlank(tempExtension)) {
      throw new IllegalArgumentException(originalFilename + " 파일의 확장자를 판단 할 수 없음.")
    }
    this.originalFilename = originalFilename
    this.extension = tempExtension.toLowerCase
  }

  def getOriginalFilename: String = {
    return originalFilename
  }

  def setCreatedDate(createdDate: Date) {
    this.createdDate = createdDate
  }

  def getCreatedDate: Date = {
    return this.createdDate
  }

  def getExtension: String = {
    return extension
  }

  def getCreatedDateYearMonth: String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy/MM")
    return sdf.format(getCreatedDate)
  }

  def getFilePath(attachmentRootDir: String): String = {
    return String.format("/%s/%s/%s", attachmentRootDir, getCreatedDateYearMonth, getId)
  }

  def isImage: Boolean = {
    Attachment.IMAGE_EXTENSIONS.contains(getExtension)
  }

  def toMap: Map[String, AnyRef] = {
    val values: Map[String, AnyRef] = new HashMap[String, AnyRef]
    values.put("id", getId)
    values.put("originalFileName", getOriginalFilename)
    values.put("createdDate", getCreatedDate)
    return values
  }
}
