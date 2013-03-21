package net.slipp.support.web;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.util.Scanner;

public class WebServerListener implements Scanner.BulkListener {
	private static final Log LOG = LogFactory.getLog(WebServerListener.class);

	private WebAppContext webApp;
	private List<File> classpaths;

	public WebServerListener(WebAppContext webApp, List<File> classpaths) {
		super();
		this.webApp = webApp;
		this.classpaths = classpaths;
	}

	public void filesChanged(@SuppressWarnings("rawtypes") List changes) throws Exception {
		try {
			webApp.stop();
			webApp.setClassLoader(null);
			webApp.setServerClasses(new String[] { "org.mortbay.jetty.plus.jaas.", "org.mortbay.jetty." });
			WebAppClassLoader loader = new WebAppClassLoader(webApp);
			for (File each : classpaths) {
				loader.addClassPath(each.getCanonicalPath());
			}

			webApp.setClassLoader(loader);

			webApp.start();
		} catch (Exception e) {
			LOG.error("Error reconfiguring/restarting webapp after change in watched files", e);
		}
	}

}
