package net.slipp.support.web

import java.io.File

import org.apache.catalina.connector.Connector
import org.apache.catalina.startup.Tomcat

object WebServerLauncher {
  @throws(classOf[Exception])
  def main(args: Array[String]) {
    val webappDirLocation: String = "webapp/"
    val tomcat: Tomcat = new Tomcat
    var webPort: String = System.getenv("PORT")
    if (webPort == null || webPort.isEmpty) {
      webPort = "8080"
    }
    tomcat.setPort(Integer.valueOf(webPort))
    val connector: Connector = tomcat.getConnector
    connector.setURIEncoding("UTF-8")
    tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath)
    System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath)
    tomcat.start
    tomcat.getServer.await
  }
}
