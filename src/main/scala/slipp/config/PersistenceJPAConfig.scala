package slipp.config

import javax.persistence.EntityManagerFactory

import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration, Primary, PropertySource}
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.orm.jpa.{JpaTransactionManager, LocalContainerEntityManagerFactoryBean}
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
object PersistenceJPAConfig {
  @Bean def propertySourcesPlaceholderConfigurer = {
    new PropertySourcesPlaceholderConfigurer
  }
}

@Configuration
@PropertySource(Array("classpath:application-properties.xml"))
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = Array("net.slipp.repository"))
class PersistenceJPAConfig {
  @Autowired var env: Environment = _

  @Bean(destroyMethod = "close")
  @Primary
  @ConfigurationProperties(prefix = "datasource.primary") def dataSource = {
    val dataSource: BasicDataSource = new BasicDataSource
    dataSource.setDriverClassName(env.getProperty("database.driverClassName"))
    dataSource.setUrl(env.getProperty("database.url"))
    dataSource.setUsername(env.getProperty("database.username"))
    dataSource.setPassword(env.getProperty("database.password"))
    dataSource.setValidationQuery(env.getProperty("database.validquery"))
    dataSource
  }

  @Bean def entityManagerFactory = {
    val em: LocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean
    em.setDataSource(dataSource)
    em.setPersistenceXmlLocation("classpath:META-INF/persistence.xml")
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter)
    em
  }

  @Bean def transactionManager(emf: EntityManagerFactory) = {
    new JpaTransactionManager(emf)
  }
}