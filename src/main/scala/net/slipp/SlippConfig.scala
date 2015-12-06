package net.slipp

import java.util.Properties
import javax.sql.DataSource

import net.slipp.domain.wiki.WikiDao
import net.slipp.support.utils.ConvenientProperties
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation._
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import slipp.config.{InfraConfig, PersistenceJPAConfig, SpringSecurityConfig, SpringSocialConfig}

@Configuration
@Import(Array(classOf[EmbeddedServletContainerAutoConfiguration], classOf[PersistenceJPAConfig], classOf[InfraConfig],
  classOf[SpringSecurityConfig], classOf[SpringSocialConfig], classOf[SlippServletContextInitializer]))
@PropertySource(Array("classpath:application-properties.xml"))
@ComponentScan(basePackages = Array("net.slipp.service"))
class SlippConfig {
  @Autowired
  private var env: Environment = null

  @Bean def wikiDao: WikiDao = {
    val wikiDao: WikiDao = new WikiDao
    wikiDao.setDataSource(wikiDataSource)
    return wikiDao
  }

  @Bean(destroyMethod = "close")
  @ConfigurationProperties(prefix="datasource.secondary")
  def wikiDataSource: DataSource = {
    val dataSource: BasicDataSource = new BasicDataSource
    dataSource.setDriverClassName(env.getProperty("wiki.database.driverClassName"))
    dataSource.setUrl(env.getProperty("wiki.database.url"))
    dataSource.setUsername(env.getProperty("wiki.database.username"))
    dataSource.setPassword(env.getProperty("wiki.database.password"))
    dataSource
  }

  @Bean
  def applicationProperties() = {
    val properties = new Properties()
    properties.load(new ClassPathResource("application-properties.xml").getInputStream())
    new ConvenientProperties(properties)
  }
}
