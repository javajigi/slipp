package slipp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(value={PersistenceJPAConfig.class})
@ImportResource(value={ "classpath:infrastructure.xml",
						"classpath:applicationContext-social.xml", 
						"classpath:applicationContext-cache.xml",
						"classpath:applicationContext-profile.xml",
						"classpath:applicationContext.xml"})
public class ApplicationConfig {

}
