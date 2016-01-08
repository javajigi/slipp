package net.slipp.web.qna

import java.io.File
import java.io.IOException
import java.util.Map

import javax.annotation.Resource
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import net.slipp.domain.qna.Attachment
import net.slipp.domain.user.SocialUser
import net.slipp.service.qna.AttachmentService
import net.slipp.support.web.ServletDownloadManager
import net.slipp.support.web.argumentresolver.LoginUser

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest

@Controller
@RequestMapping(Array("/attachments"))
class AttachmentController(
  @Resource(name = "attachmentService") attachmentService: AttachmentService,
  @Resource(name = "servletDownloadManager") servletDownloadManager: ServletDownloadManager) {
  private val AttachmentFileInputName = "file"
  private val ExpireMillis = 31556926000L // 1year

  @RequestMapping(value = Array(""), method = Array(RequestMethod.POST))
  @ResponseBody def upload(@LoginUser loginUser: SocialUser, request: MultipartRequest) = {
    val multipartFile = request.getFile(AttachmentFileInputName)
    val attachment = attachmentService.add(multipartFile, loginUser)
    attachment.toMap
  }

  @RequestMapping(Array("/{attachmentId}"))
  def downloadById(request: HttpServletRequest, response: HttpServletResponse,
                   @PathVariable("attachmentId") attachmentId: String): Unit = {
    val attachment = attachmentService.getById(attachmentId)
    val attachmentFile = attachmentService.getDestinationFile(attachment)
    servletDownloadManager.download(request, response, attachmentFile, attachment.getOriginalFilename, ExpireMillis)
  }

  def this() = this(null, null)
}