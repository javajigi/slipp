package slipp.config

import java.io.IOException
import java.util.Properties
import javax.sql.DataSource

import net.slipp.domain.wiki.WikiDao
import net.slipp.support.utils.ConvenientProperties
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation._
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Controller

@Configuration
@Import(value = Array(classOf[PersistenceJPAConfig], classOf[InfraConfig], classOf[SpringSecurityConfig], classOf[SpringSocialConfig]))
@PropertySource(Array("classpath:application-properties.xml"))
@ComponentScan(basePackages = Array("net.slipp.service"), excludeFilters = Array(new ComponentScan.Filter(`type` = FilterType.ANNOTATION, value = Array(classOf[Controller]))))
class ApplicationConfig {
  @Autowired private var env: Environment = _

  @Bean def wikiDao: WikiDao = {
    val wikiDao: WikiDao = new WikiDao
    wikiDao.setDataSource(wikiDataSource)
    wikiDao
  }

  @Bean(destroyMethod = "close") def wikiDataSource: DataSource = {
    val dataSource: BasicDataSource = new BasicDataSource
    dataSource.setDriverClassName(env.getProperty("wiki.database.driverClassName"))
    dataSource.setUrl(env.getProperty("wiki.database.url"))
    dataSource.setUsername(env.getProperty("wiki.database.username"))
    dataSource.setPassword(env.getProperty("wiki.database.password"))
    dataSource
  }

  @Bean
  @throws(classOf[IOException])
  def applicationProperties: ConvenientProperties = {
    val properties: Properties = new Properties
    properties.load(new ClassPathResource("application-properties.xml").getInputStream)
    new ConvenientProperties(properties)
  }
}