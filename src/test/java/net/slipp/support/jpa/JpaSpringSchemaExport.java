package net.slipp.support.jpa;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class JpaSpringSchemaExport {

	private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

	private String destination;

	private boolean create = true;

	private boolean format = true;

	public void export() {
		Configuration cfg = new Configuration();

		SchemaExport schemaExport = new SchemaExport(cfg);
		if (getDestination() == null) {
			setDestination(getClass().getResource("/").getFile() + "schema-export.sql");
		}
		schemaExport.setOutputFile(getDestination());
		schemaExport.setFormat(isFormat());
		schemaExport.execute(true, false, false, isCreate());
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public boolean isFormat() {
		return format;
	}

	public void setFormat(boolean format) {
		this.format = format;
	}

	public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean() {
		return localContainerEntityManagerFactoryBean;
	}

	public void setLocalContainerEntityManagerFactoryBean(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
		this.localContainerEntityManagerFactoryBean = localContainerEntityManagerFactoryBean;
	}
}
