package net.slipp.support.jpa;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UpdatedDateEntityListener {

	@PreUpdate
	@PrePersist
	public void prePersistAndUpdate(HasUpdatedDate hud) {
		hud.setUpdatedDate(new Date());
	}
}
