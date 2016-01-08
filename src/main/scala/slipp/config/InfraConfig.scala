package slipp.config

import java.util.Properties
import java.util.concurrent.Executor
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

@Configuration
@EnableCaching
@EnableAsync class InfraConfig extends AsyncConfigurer {
  @Autowired private var env: Environment = _

  @Bean def mailSender: MailSender = {
    val mailSender: JavaMailSenderImpl = new JavaMailSenderImpl
    mailSender.setHost(env.getProperty("mail.server.host"))
    mailSender.setPort(env.getProperty("mail.server.port").toInt)
    mailSender.setUsername(env.getProperty("mail.server.username"))
    mailSender.setPassword(env.getProperty("mail.server.password"))
    mailSender.setJavaMailProperties(additionalMailProperties)
    mailSender
  }

  private def additionalMailProperties: Properties = {
    val properties: Properties = new Properties
    properties.setProperty("mail.smtp.auth", "true")
    properties.setProperty("mail.smtp.starttls.enable", "true")
    properties
  }

  @Bean def freemarkerConfiguration: FreeMarkerConfigurationFactoryBean = {
    val freemarkerConfiguration: FreeMarkerConfigurationFactoryBean = new FreeMarkerConfigurationFactoryBean
    freemarkerConfiguration.setTemplateLoaderPath("classpath:templates")
    freemarkerConfiguration.setDefaultEncoding("UTF-8")
    freemarkerConfiguration
  }

  @Bean def ehcache: EhCacheManagerFactoryBean = {
    val ehcache: EhCacheManagerFactoryBean = new EhCacheManagerFactoryBean
    ehcache.setConfigLocation(new ClassPathResource("ehcache.xml"))
    ehcache.setShared(true)
    ehcache
  }

  @Bean def cacheManager(cm: net.sf.ehcache.CacheManager): CacheManager = {
    val cacheManager: EhCacheCacheManager = new EhCacheCacheManager
    cacheManager.setCacheManager(cm)
    cacheManager
  }

  def getAsyncExecutor: Executor = {
    val executor: ThreadPoolTaskExecutor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(5)
    executor.setMaxPoolSize(10)
    executor.setThreadNamePrefix("SlippExecutor-")
    executor.initialize()
    executor
  }

  def getAsyncUncaughtExceptionHandler: AsyncUncaughtExceptionHandler = {
    null
  }
}