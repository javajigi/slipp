package slipp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(value={PersistenceJPAConfig.class, InfraConfig.class})
@ImportResource(value={ "classpath:applicationContext-social.xml",
						"classpath:applicationContext-profile.xml",
						"classpath:applicationContext.xml"})
@PropertySource("classpath:application-properties.xml")
public class ApplicationConfig {

}
