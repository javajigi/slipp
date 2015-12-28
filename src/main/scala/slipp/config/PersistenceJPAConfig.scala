package slipp.config

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import net.slipp.support.jpa.SlippRepositoryFactoryBean
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.Repository
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
object PersistenceJPAConfig {
  @Bean def propertySourcesPlaceholderConfigurer: PropertySourcesPlaceholderConfigurer = {
    return new PropertySourcesPlaceholderConfigurer
  }
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = Array("net.slipp.repository"), repositoryFactoryBeanClass = classOf[SlippRepositoryFactoryBean[_ <: Repository[_, _], _, _ <: Serializable]])
class PersistenceJPAConfig {
  @Autowired private var env: Environment = null

  @Bean(destroyMethod = "close")
  @Primary
  @ConfigurationProperties(prefix = "datasource.primary") def dataSource: DataSource = {
    val dataSource: BasicDataSource = new BasicDataSource
    dataSource.setDriverClassName(env.getProperty("database.driverClassName"))
    dataSource.setUrl(env.getProperty("database.url"))
    dataSource.setUsername(env.getProperty("database.username"))
    dataSource.setPassword(env.getProperty("database.password"))
    dataSource.setValidationQuery(env.getProperty("database.validquery"))
    return dataSource
  }

  @Bean def entityManagerFactory: LocalContainerEntityManagerFactoryBean = {
    val em: LocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean
    em.setDataSource(dataSource)
    em.setPersistenceXmlLocation("classpath:META-INF/persistence.xml")
    val vendorAdapter: JpaVendorAdapter = new HibernateJpaVendorAdapter
    em.setJpaVendorAdapter(vendorAdapter)
    return em
  }

  @Bean def transactionManager(emf: EntityManagerFactory): PlatformTransactionManager = {
    return new JpaTransactionManager(emf)
  }
}