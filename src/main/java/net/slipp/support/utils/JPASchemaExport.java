package net.slipp.support.utils;
import java.util.HashMap;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@SuppressWarnings("deprecation")
public class JPASchemaExport {
    public static void main(String[] args) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		HashMap<String, String> props = new HashMap<String, String>();
		props.put("hibernate.format_sql", "true");
		Ejb3Configuration configured = cfg.configure("slipp.qna", props);
		SchemaExport se = new SchemaExport(configured.getHibernateConfiguration());
		se.setDelimiter(";");
		se.create(true, false);
	}
}
