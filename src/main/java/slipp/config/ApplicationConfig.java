package slipp.config;

import net.slipp.domain.wiki.WikiDao;
import net.slipp.support.utils.ConvenientProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Import(value = {PersistenceJPAConfig.class, InfraConfig.class, SpringSecurityConfig.class, SpringSocialConfig.class})
@ImportResource(value = {"classpath:applicationContext.xml"})
@PropertySource("classpath:application-properties.xml")
@ComponentScan(basePackages = "net.slipp.service", excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class))
public class ApplicationConfig {
    @Autowired
    private Environment env;

    @Bean
    public WikiDao wikiDao() {
        WikiDao wikiDao = new WikiDao();
        wikiDao.setDataSource(wikiDataSource());
        return wikiDao;
    }

    @Bean(destroyMethod = "close")
    public DataSource wikiDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("wiki.database.driverClassName"));
        dataSource.setUrl(env.getProperty("wiki.database.url"));
        dataSource.setUsername(env.getProperty("wiki.database.username"));
        dataSource.setPassword(env.getProperty("wiki.database.password"));
        return dataSource;
    }

    @Bean
    public ConvenientProperties applicationProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("application-properties.xml").getInputStream());
        return new ConvenientProperties(properties);
    }
}
