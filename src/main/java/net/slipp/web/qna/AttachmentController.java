package net.slipp.web.qna;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.slipp.domain.qna.Attachment;
import net.slipp.domain.user.SocialUser;
import net.slipp.service.qna.AttachmentService;
import net.slipp.support.web.ServletDownloadManager;
import net.slipp.support.web.argumentresolver.LoginUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {
	public static final String ATTACHMENT_FILE_INPUT_NAME = "file";
	
	public static final long EXPIRE_MILLIS = 31556926000L; // 1year

	@Resource(name = "attachmentService")
	private AttachmentService attachmentService;

	@Resource(name = "servletDownloadManager")
	private ServletDownloadManager servletDownloadManager;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> upload(@LoginUser SocialUser loginUser, MultipartRequest request) throws IOException {
		MultipartFile multipartFile = request.getFile(ATTACHMENT_FILE_INPUT_NAME);
		Attachment attachment = attachmentService.add(multipartFile, loginUser);

		return attachment.toMap();
	}

	@RequestMapping(value = "/{attachmentId}", method = RequestMethod.GET)
	public String downloadById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("attachmentId") String attachmentId) throws ServletException, IOException {
		Attachment attachment = attachmentService.getById(attachmentId);
		File attachmentFile = attachmentService.getDestinationFile(attachment);
		servletDownloadManager.download(request, response, attachmentFile, attachment.getOriginalFilename(), EXPIRE_MILLIS);
		return null;
	}
}
