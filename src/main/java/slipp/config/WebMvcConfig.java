package slipp.config;

import net.slipp.social.security.FixedProviderSignInController;
import net.slipp.social.security.SlippSecuritySignUpController;
import net.slipp.support.utils.ConvenientProperties;
import net.slipp.support.web.GlobalRequestAttributesInterceptor;
import net.slipp.support.web.ServletDownloadManager;
import net.slipp.support.web.argumentresolver.LoginUserHandlerMethodArgumentResolver;
import net.slipp.support.web.servletcontext.interceptor.GlobalServletApplicationContextAttributeSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "net.slipp.web",
        includeFilters = @ComponentScan.Filter(Controller.class))
@PropertySource("classpath:application-properties.xml")
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private Environment env;

    @Autowired
    private ConvenientProperties applicationProperties;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private SignInAdapter signInAdapter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalRequestAttributesInterceptor());
    }

    @Bean
    public HandlerInterceptor globalRequestAttributesInterceptor() {
        return new GlobalRequestAttributesInterceptor();
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(2000000);
        return resolver;
    }

    @Bean
    public GlobalServletApplicationContextAttributeSetter aplicationContextAttributeSetter() {
        GlobalServletApplicationContextAttributeSetter attributeSetter = new GlobalServletApplicationContextAttributeSetter();
        attributeSetter.setApplicationProperties(applicationProperties);
        return attributeSetter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/static_resources/");
    }

    @Bean
    public ServletDownloadManager servletDownloadManager() {
        return new ServletDownloadManager();
    }

    @Bean
    public ProviderSignInController signInController() {
        FixedProviderSignInController signInController =
                new FixedProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        signInController.setSignInUrl("/signup");
        signInController.setPostSignInUrl("/authenticate");
        signInController.setApplicationUrl(env.getProperty("application.url"));
        return signInController;
    }

    @Bean
    public SlippSecuritySignUpController signUpController() {
        return new SlippSecuritySignUpController();
    }

    @Bean
    public WebBindingInitializer webBindingInitializer() {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setValidator(new LocalValidatorFactoryBean());
        return initializer;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
