package net.slipp

import java.util.Properties

import net.slipp.support.utils.ConvenientProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation._
import org.springframework.core.io.ClassPathResource
import slipp.config.{SpringSocialConfig, SpringSecurityConfig, InfraConfig, PersistenceJPAConfig}

@Configuration
@EnableAutoConfiguration
@Import(Array(classOf[InfraConfig], classOf[SpringSecurityConfig], classOf[SpringSocialConfig]))
@PropertySource(Array("classpath:application-properties.xml"))
@ComponentScan(basePackages = Array("net.slipp"))
class SlippConfig {
  @Bean
  def applicationProperties() = {
    val properties = new Properties()
    properties.load(new ClassPathResource("application-properties.xml").getInputStream())
    new ConvenientProperties(properties)
  }
}
