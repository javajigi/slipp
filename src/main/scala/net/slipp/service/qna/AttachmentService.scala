package net.slipp.service.qna

import java.io.File
import javax.annotation.Resource

import net.slipp.domain.qna.Attachment
import net.slipp.domain.user.SocialUser
import net.slipp.repository.attachment.AttachmentRepository
import net.slipp.support.utils.SlippFileUtils
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import scala.util.{Failure, Success, Try}

@Service("attachmentService") class AttachmentService {
  private val logger: Logger = LoggerFactory.getLogger(classOf[AttachmentService])

  @Autowired var env: Environment = _
  @Resource(name = "attachmentRepository") private var attachmentRepository: AttachmentRepository = _

  def add(multipartFile: MultipartFile, uploader: SocialUser): Attachment = {
    if (multipartFile.isEmpty) {
      logger.info("attachment is empty. multipartFile:{}", multipartFile.getOriginalFilename)
      return null
    }
    val attachment: Attachment = persistAttachment(multipartFile, uploader)
    transferToAttachmentDir(multipartFile, attachment)
    return attachment
  }

  private def persistAttachment(multipartFile: MultipartFile, uploader: SocialUser) = {
    val attachment = new Attachment
    attachment.setOriginalFilename(multipartFile.getOriginalFilename)
    attachment.setUploader(uploader)
    attachmentRepository.save(attachment)
  }

  private def transferToAttachmentDir(multipartFile: MultipartFile, attachment: Attachment): File = {
    val destFile: File = getDestinationFile(attachment)
    try {
      SlippFileUtils.forceMkParentDir(destFile)
      multipartFile.transferTo(destFile)
    } catch {
      case ex: Exception => {
        throw new IllegalArgumentException(destFile + "로 첨부파일 옮기다 오류 발생")
      }
    }
    destFile
  }

  def getDestinationFile(attachment: Attachment) = {
    new File(attachment.getFilePath(env.getProperty("attachment.root.dir")))
  }

  def getById(attachmentId: String) = {
    attachmentRepository.findOne(attachmentId)
  }
}
