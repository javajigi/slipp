package slipp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(value={PersistenceJPAConfig.class, InfraConfig.class, SecurityConfig.class, SpringSocialConfig.class})
@ImportResource(value={ "classpath:applicationContext.xml"})
@PropertySource("classpath:application-properties.xml")
public class ApplicationConfig {

}
