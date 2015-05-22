package net.slipp.support.jpa;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.slipp.domain.qna.Answer;
import net.slipp.domain.qna.Question;
import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaGenerator {
	private Configuration cfg;
	
	public SchemaGenerator(String packageName) throws Exception {
		cfg = new Configuration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", "net.slipp.support.jpa.Mysql5BitBooleanDialect");

		cfg.addAnnotatedClass(SocialUser.class);
		cfg.addAnnotatedClass(Question.class);
		cfg.addAnnotatedClass(Answer.class);
		cfg.addAnnotatedClass(Tag.class);
//		for (Class clazz : getClasses(packageName)) {
//			cfg.addAnnotatedClass(clazz);
//		}
	}

	private List<Class> getClasses(String packageName) throws Exception {
		File directory = null;
		try {
			ClassLoader cld = getClassLoader();
			URL resource = getResource(packageName, cld);
			directory = new File(resource.getFile());
		} catch (NullPointerException ex) {
			throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
		}
		return collectClasses(packageName, directory);
	}

	private ClassLoader getClassLoader() throws ClassNotFoundException {
		ClassLoader cld = Thread.currentThread().getContextClassLoader();
		if (cld == null) {
			throw new ClassNotFoundException("Can't get class loader.");
		}
		return cld;
	}

	private URL getResource(String packageName, ClassLoader cld) throws ClassNotFoundException {
		String path = packageName.replace('.', '/');
		URL resource = cld.getResource(path);
		if (resource == null) {
			throw new ClassNotFoundException("No resource for " + path);
		}
		return resource;
	}

	private List collectClasses(String packageName, File directory) throws ClassNotFoundException {
		List classes = new ArrayList<>();
		if (directory.exists()) {
			String[] files = directory.list();
			for (String file : files) {
				if (file.endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
				}
			}
		} else {
			throw new ClassNotFoundException(packageName + " is not a valid package");
		}
		return classes;
	}
	
	public void generate() {
		SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setOutputFile("create_schema.sql");
        export.setFormat(true);
        export.execute(true, false, false, false);
    }
}
