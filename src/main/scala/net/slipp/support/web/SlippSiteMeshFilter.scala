package net.slipp.support.web

import org.sitemesh.builder.SiteMeshFilterBuilder
import org.sitemesh.config.ConfigurableSiteMeshFilter

class SlippSiteMeshFilter extends ConfigurableSiteMeshFilter {
  override def applyCustomConfiguration(builder: SiteMeshFilterBuilder): Unit = {
    builder
      .addDecoratorPath("/*", "/WEB-INF/jsp/decorators/default.jsp")
      .addDecoratorPath("/admin/*", "/WEB-INF/jsp/decorators/admin_default.jsp")
      .addExcludedPath("/ajax/*")
      .addExcludedPath("/api/*")
      .addExcludedPath("/popup/*")
      .addExcludedPath("/blank")
      .addExcludedPath("/attachments*")
      .addExcludedPath("/rss*")
      .addExcludedPath("/wikis/preview*")
      .addExcludedPath("/robots.txt")
      .addExcludedPath("/google528d99e4fd7e1630.html")
  }
}
