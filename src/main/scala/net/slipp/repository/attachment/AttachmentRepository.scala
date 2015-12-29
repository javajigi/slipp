package net.slipp.repository.attachment

import net.slipp.domain.qna.Attachment
import org.springframework.data.jpa.repository.JpaRepository

trait AttachmentRepository extends JpaRepository[Attachment, String]
