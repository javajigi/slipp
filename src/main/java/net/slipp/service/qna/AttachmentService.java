package net.slipp.service.qna;

import java.io.File;

import javax.annotation.Resource;

import net.slipp.domain.qna.Attachment;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.attachment.AttachmentRepository;
import net.slipp.support.utils.SlippFileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("attachmentService")
public class AttachmentService {
	private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

	@Value("#{applicationProperties['attachment.root.dir']}")
	private String attachmentRootDir;

	@Resource(name = "attachmentRepository")
	private AttachmentRepository attachmentRepository;
	
	public Attachment add(MultipartFile multipartFile, SocialUser uploader) {
		if (multipartFile.isEmpty()) {
			logger.info("attachment is empty. multipartFile:{}", multipartFile.getOriginalFilename());
			return null;
		}

		Attachment attachment = persistAttachment(multipartFile, uploader);
		transferToAttachmentDir(multipartFile, attachment);

		return attachment;
	}

	private Attachment persistAttachment(MultipartFile multipartFile, SocialUser uploader) {
		Attachment attachment = new Attachment();
		attachment.setOriginalFilename(multipartFile.getOriginalFilename());
		attachment.setUploader(uploader);
		attachmentRepository.save(attachment);
		return attachment;
	}

	private File transferToAttachmentDir(MultipartFile multipartFile, Attachment attachment) {
		File destFile = getDestinationFile(attachment);

		try {
			SlippFileUtils.forceMkParentDir(destFile);
			multipartFile.transferTo(destFile);
		} catch (Exception ex) {
			throw new IllegalArgumentException(destFile + "로 첨부파일 옮기다 오류 발생");
		}
		return destFile;
	}

	public File getDestinationFile(Attachment attachment) {
		return new File(attachment.getFilePath(attachmentRootDir));
	}

	public Attachment getById(String attachmentId) {
		return attachmentRepository.findOne(attachmentId);
	}
}
