package slipp.config

import net.slipp.service.user.{FixedPasswordGenerator, PasswordGenerator, RandomPasswordGenerator}
import net.slipp.social.security._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Conditional, Configuration}
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.vote.{AuthenticatedVoter, RoleVoter, UnanimousBased}
import org.springframework.security.authentication.encoding.ShaPasswordEncoder
import org.springframework.security.authentication.{AuthenticationManager, ProviderManager, RememberMeAuthenticationProvider}
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.authentication.{AuthenticationSuccessHandler, LoginUrlAuthenticationEntryPoint, RememberMeServices, SimpleUrlAuthenticationSuccessHandler}

import scala.collection.JavaConversions._

@Configuration
@EnableWebSecurity class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired private var env: Environment = null

  @throws(classOf[Exception])
  protected override def configure(http: HttpSecurity) {
    http.csrf.disable
    http.authorizeRequests.antMatchers("/favicon.ico").permitAll

    http.headers().frameOptions().disable()

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
        .antMatchers("/questions/*/answers/*/to").access("hasRole('ROLE_USER')")
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
        .successHandler(authenticationSuccessHandler)
        .failureUrl("/users/login?login_error=1")
        .permitAll()
        .and()
      .logout()
        .logoutUrl("/users/logout")
        .permitAll()
        .and()
      .rememberMe()
        .rememberMeServices(slippRememberMeServices)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(springSocialSecurityEntryPoint)

    http.anonymous

    http.addFilterBefore(slippSecurityAuthenticationFilter, classOf[BasicAuthenticationFilter])
  }

  @Bean override def authenticationManager: AuthenticationManager = {
    val providers = List(rememberMeAuthenticationProvider, daoAuthenticationProvider)
    new ProviderManager(providers)
  }

  @Bean
  @Conditional(Array(classOf[ProductionPhase])) def randomPasswordGenerator: PasswordGenerator = {
    new RandomPasswordGenerator
  }

  @Bean
  @Conditional(Array(classOf[DevelopmentPhase])) def fixedPasswordGenerator: PasswordGenerator = {
    new FixedPasswordGenerator
  }

  @Bean def autoLoginAuthenticator: AutoLoginAuthenticator = {
    new AutoLoginAuthenticator
  }

  @Bean def passwordEncoder: PasswordEncoder = {
    val passwordEncoder: Sha256ToBCryptPasswordEncoder = new Sha256ToBCryptPasswordEncoder
    passwordEncoder.setBcryptPasswordEncoder(new BCryptPasswordEncoder)
    passwordEncoder.setSha256PasswordEncoder(new ShaPasswordEncoder(256))
    passwordEncoder
  }

  @Bean def slippUserDetailsService: SlippUserDetailsService = {
    val userDetailsService: SlippUserDetailsService = new SlippUserDetailsService
    userDetailsService.setAdminUsers("자바지기")
    userDetailsService
  }

  @Bean def authenticationSuccessHandler: AuthenticationSuccessHandler = {
    val authenticationSuccessHandler: SimpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler
    authenticationSuccessHandler.setDefaultTargetUrl("/")
    authenticationSuccessHandler.setTargetUrlParameter("redirect")
    authenticationSuccessHandler
  }

  private def defaultRememberMeServices: SlippTokenBasedRememberMeServices = {
    val rememberMeServices: SlippTokenBasedRememberMeServices = new SlippTokenBasedRememberMeServices(env.getProperty("slipp.remember.token.key"), slippUserDetailsService)
    rememberMeServices.setTokenValiditySeconds(1209600)
    rememberMeServices
  }

  @Bean def slippRememberMeServices: RememberMeServices = {
    defaultRememberMeServices
  }

  @Bean def springSocialSecurityRememberMeServices: RememberMeServices = {
    val rememberMeServices: SlippTokenBasedRememberMeServices = defaultRememberMeServices
    rememberMeServices.setAlwaysRemember(true)
    rememberMeServices
  }

  @Bean def slippSecurityAuthenticationFilter: SlippSecurityAuthenticationFilter = {
    val securityAuthenticationFilter: SlippSecurityAuthenticationFilter = new SlippSecurityAuthenticationFilter
    securityAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
    securityAuthenticationFilter.setRememberMeServices(springSocialSecurityRememberMeServices)
    securityAuthenticationFilter
  }

  @Bean def rememberMeAuthenticationProvider: RememberMeAuthenticationProvider = {
    new RememberMeAuthenticationProvider(env.getProperty("slipp.remember.token.key"))
  }

  @Bean def daoAuthenticationProvider: SlippDaoAuthenticationProvider = {
    val daoAuthenticationProvider: SlippDaoAuthenticationProvider = new SlippDaoAuthenticationProvider
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)
    daoAuthenticationProvider.setUserDetailsService(slippUserDetailsService)
    daoAuthenticationProvider
  }

  @Bean def springSocialSecurityEntryPoint: LoginUrlAuthenticationEntryPoint = {
    new LoginUrlAuthenticationEntryPoint("/users/login")
  }

  @Bean def accessDecisionManager: AccessDecisionManager = {
    val voters = List(new RoleVoter, new AuthenticatedVoter)
    new UnanimousBased(voters)
  }
}
