package slipp.config;

import com.google.common.collect.Lists;
import net.slipp.service.user.FixedPasswordGenerator;
import net.slipp.service.user.PasswordGenerator;
import net.slipp.service.user.RandomPasswordGenerator;
import net.slipp.social.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment env = null;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
            .authorizeRequests()
            .antMatchers("/favicon.ico").permitAll();

        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/questions/form").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/questions").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/questions/*/form").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/questions/*/tagged").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/questions/*/answers/*/form").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PUT, "/questions/*/answers/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/questions/*/answers/*/like").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/questions/*/answers/*/dislike").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/questions/*/connect/facebook").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PUT, "/questions").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, "/questions/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/smalltalks/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/questions/*/detagged/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/questions/*/like").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/questions/*/answers/*/like").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/questions/*/answers/*/dislike").access("hasRole('ROLE_USER')")
                .antMatchers("/questions/*/answers/*/to").access("hasRole('ROLE_ADMINISTRATOR')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMINISTRATOR')")
                .antMatchers("/migrations/**").access("hasRole('ROLE_ADMINISTRATOR')")
                .antMatchers("/mails/**").access("hasRole('ROLE_ADMINISTRATOR')")
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/users/login")
                .loginProcessingUrl("/users/authenticate")
                .usernameParameter("authenticationId")
                .passwordParameter("authenticationPassword")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/users/login?login_error=1")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/users/logout")
                .permitAll()
                .and()
            .rememberMe()
                .rememberMeServices(slippRememberMeServices())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(springSocialSecurityEntryPoint());

        http.anonymous();

        http.addFilterBefore(slippSecurityAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = Lists.newArrayList(rememberMeAuthenticationProvider(), daoAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    @Conditional(ProductionPhase.class)
    public PasswordGenerator randomPasswordGenerator() {
        return new RandomPasswordGenerator();
    }

    @Bean
    @Conditional(DevelopmentPhase.class)
    public PasswordGenerator fixedPasswordGenerator() {
        return new FixedPasswordGenerator();
    }

    @Bean
    public AutoLoginAuthenticator autoLoginAuthenticator() {
        return new AutoLoginAuthenticator();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Sha256ToBCryptPasswordEncoder passwordEncoder = new Sha256ToBCryptPasswordEncoder();
        passwordEncoder.setBcryptPasswordEncoder(new BCryptPasswordEncoder());
        passwordEncoder.setSha256PasswordEncoder(new ShaPasswordEncoder(256));
        return passwordEncoder;
    }

    @Bean
    public SlippUserDetailsService slippUserDetailsService() {
        SlippUserDetailsService userDetailsService = new SlippUserDetailsService();
        userDetailsService.setAdminUsers("자바지기:eclipse4j:진우");
        return userDetailsService;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler();
        authenticationSuccessHandler.setDefaultTargetUrl("/");
        authenticationSuccessHandler.setTargetUrlParameter("redirect");
        return authenticationSuccessHandler;
    }

    private SlippTokenBasedRememberMeServices defaultRememberMeServices() {
        SlippTokenBasedRememberMeServices rememberMeServices = new SlippTokenBasedRememberMeServices(env.getProperty("slipp.remember.token.key"), slippUserDetailsService());
        rememberMeServices.setTokenValiditySeconds(1209600);
        return rememberMeServices;
    }

    @Bean
    public RememberMeServices slippRememberMeServices() {
        return defaultRememberMeServices();
    }

    @Bean
    public RememberMeServices springSocialSecurityRememberMeServices() {
        SlippTokenBasedRememberMeServices rememberMeServices = defaultRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public SlippSecurityAuthenticationFilter slippSecurityAuthenticationFilter() {
        SlippSecurityAuthenticationFilter securityAuthenticationFilter = new SlippSecurityAuthenticationFilter();
        securityAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        securityAuthenticationFilter.setRememberMeServices(springSocialSecurityRememberMeServices());
        return securityAuthenticationFilter;
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(env.getProperty("slipp.remember.token.key"));
    }

    @Bean
    public SlippDaoAuthenticationProvider daoAuthenticationProvider() {
        SlippDaoAuthenticationProvider daoAuthenticationProvider = new SlippDaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(slippUserDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint springSocialSecurityEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/users/login");
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List voters = Lists.newArrayList(new RoleVoter(), new AuthenticatedVoter());
        return new UnanimousBased(voters);
    }
}
