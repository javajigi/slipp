package net.slipp.support.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.util.Scanner;

public class WebServer {
	Integer port;
	String webapp;
	String webappConfiguration;
	List<File> classpaths;
	Server server;
	WebAppContext webApp;

	public WebServer(Integer port, String webapp, String webappConfiguration, String... requiredClasspaths) {
		this.port = port;
		this.webapp = webapp;
		this.webappConfiguration = webappConfiguration;
		this.classpaths = new ArrayList<File>();
		for (String each : requiredClasspaths) {
			classpaths.add(new File(each));
		}
	}

	public void start() throws Exception {
		server = new Server();

		prepareServerConfiguration();
		prepareHttpConnector();
		prepareWebAppContext();
		prepareReloadable();

		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

	private void prepareServerConfiguration() {
		server.setStopAtShutdown(true);
	}

	private void prepareReloadable() {
		Scanner scanner = new Scanner();
		scanner.setReportExistingFilesOnStartup(false);
		scanner.setScanInterval(3);
		scanner.setScanDirs(classpaths);
		scanner.setRecursive(true);
		scanner.addListener(new WebServerListener(webApp, classpaths));
		scanner.start();
	}

	private void prepareWebAppContext() throws IOException {
		webApp = new WebAppContext(webapp, "/");
		webApp.setDefaultsDescriptor(webappConfiguration);
		server.addHandler(webApp);

		DefaultHandler defaultHandler = new DefaultHandler();
		defaultHandler.setServeIcon(false);
		server.addHandler(defaultHandler);
	}

	private void prepareHttpConnector() {
		SelectChannelConnector httpConnector = new SelectChannelConnector();
		httpConnector.setForwarded(true);
		httpConnector.setPort(port);
		server.addConnector(httpConnector);
	}

	@Override
	public String toString() {
		return "WebServer [classpaths=" + classpaths + ", port=" + port + "]";
	}
}
