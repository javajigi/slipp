package net.slipp.support.web;

public class WebServerLauncher {
	public static WebServer createDefaultWebServer(int port) {
		final String webapp = "webapp";
		final String webappConfiguration = "src/main/resources/jetty-web.xml";
		final String targetClasses = "target/classes";
		final String webXml = webapp + "/WEB-INF/web.xml";
		final String springXml = webapp + "/WEB-INF/spring-servlet.xml";
		final String sitemeshXml = webapp + "/WEB-INF/decorators.xml";

		WebServer server = new WebServer(port, webapp, webappConfiguration, targetClasses, webXml, springXml,
				sitemeshXml);
		return server;
	}

	public static void main(String[] args) throws Exception {
		int port = getPort(args);
		createDefaultWebServer(port).start();
	}

	private static int getPort(String[] args) {
		int port = 8080;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		return port;
	}
}
