package net.slipp.repository.attachment;

import net.slipp.domain.attachment.Attachment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String>{

}
