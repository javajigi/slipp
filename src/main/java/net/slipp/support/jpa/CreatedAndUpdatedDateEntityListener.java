package net.slipp.support.jpa;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class CreatedAndUpdatedDateEntityListener {

	@PrePersist
	public void prePersist(HasCreatedAndUpdatedDate hcud) {
		Date currentDate = getCurrentDate();
		hcud.setCreatedDate(currentDate);
		hcud.setUpdatedDate(currentDate);
	}

	private Date getCurrentDate() {
		return new Date();
	}

	@PreUpdate
	public void preUpdate(HasCreatedAndUpdatedDate hcud) {
		Date currentDate = getCurrentDate();
		hcud.setUpdatedDate(currentDate);
	}
}
