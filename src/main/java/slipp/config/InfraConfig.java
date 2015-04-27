package slipp.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
@EnableCaching
public class InfraConfig {
	@Autowired
	private Environment env;
	
	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("mail.server.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("mail.server.port")));
		mailSender.setUsername(env.getProperty("mail.server.username"));
		mailSender.setPassword(env.getProperty("mail.server.password"));
		mailSender.setJavaMailProperties(additionalMailProperties());
		return mailSender;
	}
	
	private Properties additionalMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		return properties;
	}
	
	@Bean
	public FreeMarkerConfigurationFactoryBean freemarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean freemarkerConfiguration = new FreeMarkerConfigurationFactoryBean();
		freemarkerConfiguration.setTemplateLoaderPath("classpath:templates");
		freemarkerConfiguration.setDefaultEncoding("UTF-8");
		return freemarkerConfiguration;
	}
	
	@Bean
	public EhCacheManagerFactoryBean ehcache() {
		EhCacheManagerFactoryBean ehcache = new EhCacheManagerFactoryBean();
		ehcache.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ehcache.setShared(true);
		return ehcache;
	}
	
	@Bean
	public CacheManager cacheManager(net.sf.ehcache.CacheManager cm) {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(cm);
		return cacheManager;
	}
}