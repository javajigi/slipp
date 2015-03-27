package net.slipp.domain.qna;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.slipp.domain.user.SocialUser;
import net.slipp.support.jpa.CreatedDateEntityListener;
import net.slipp.support.jpa.HasCreatedDate;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "attachment")
@EntityListeners(CreatedDateEntityListener.class)
public class Attachment implements HasCreatedDate {
	public static final String ATTACHMENT_DOWNLOAD_PREFIX = "/attachments";

	public static final String[] IMAGE_EXTENSIONS = { "jpg", "jpeg", "gif", "png" };

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name = "fk_attachment_writer"))
	private SocialUser uploader;
	
	@Column(name = "original_filename", length = 255, nullable = false, updatable = false)
	private String originalFilename;

	@Column(name = "extension", length = 8, nullable = false, updatable = false)
	private String extension;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;

	public Attachment() {
		// no op;
	}

	public Attachment(String id) {
		setId(id);
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setUploader(SocialUser uploader) {
		this.uploader = uploader;
	}
	
	public SocialUser getUploader() {
		return uploader;
	}

	public void setOriginalFilename(String originalFilename) {
		String tempExtension = FilenameUtils.getExtension(originalFilename);
		if (StringUtils.isBlank(tempExtension)) {
			throw new IllegalArgumentException(originalFilename + " 파일의 확장자를 판단 할 수 없음.");
		}

		this.originalFilename = originalFilename;
		this.extension = tempExtension.toLowerCase();
	}
	
	public String getOriginalFilename() {
		return originalFilename;
	}
	
	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public String getExtension() {
		return extension;
	}
	
	public String getCreatedDateYearMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		return sdf.format(getCreatedDate());
	}
	
	public String getFilePath(String attachmentRootDir) {
		return String.format("/%s/%s/%s", attachmentRootDir, getCreatedDateYearMonth(), getId());
	}
	
	public boolean isImage() {
		if (ArrayUtils.contains(IMAGE_EXTENSIONS, getExtension())) {
			return true;
		}
		return false;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("id", getId());
		values.put("originalFileName", getOriginalFilename());
		values.put("createdDate", getCreatedDate());
		return values;
	}
}
