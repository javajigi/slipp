package net.slipp.support.utils;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class JPASchemaExport {
	// http://blog.iprofs.nl/2013/01/29/hibernate4-schema-generation-ddl-from-annotated-entities/ 
	// 위 문서 참고해 maven 연동
    public static void main(String[] args) throws Exception {
    	Configuration configuration = new Configuration();
    	configuration.setProperty(Environment.DIALECT, "net.slipp.support.jpa.Mysql5BitBooleanDialect");
    	configuration.setProperty("hibernate.hbm2ddl.auto", "create");
    	
    	JPASchemaExport schemaExport = new JPASchemaExport();
    	for (Class clazz : schemaExport.getClasses("net.slipp.domain.qna")) {
    		configuration.addAnnotatedClass(clazz);
        }

    	SchemaExport se = new SchemaExport(configuration);
		se.setDelimiter(";");
//		se.setOutputFile("ddl_slipp.sql");
		se.setFormat(true);
		se.create(true, false);
	}
    
	private List<Class> getClasses(String packageName) throws Exception {
        File directory = null;
        try {
            ClassLoader cld = getClassLoader();
            URL resource = getResource(packageName, cld);
            directory = new File(resource.getFile());
        } catch (NullPointerException ex) {
            throw new ClassNotFoundException(packageName + " (" + directory
                    + ") does not appear to be a valid package");
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
     
    @SuppressWarnings("rawtypes")
	private List collectClasses(String packageName, File directory) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (directory.exists()) {
            String[] files = directory.list();
            for (String file : files) {
            	System.out.println(file);
                if (file.endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(packageName + '.'
                            + file.substring(0, file.length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(packageName
                    + " is not a valid package");
        }
        return classes;
    }
}
