package pers.zhengxiaojie.application.config.spring_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pers.zhengxiaojie.application.config.spring_security.employee.*;
import pers.zhengxiaojie.application.config.spring_security.token.TokenFilter;
import pers.zhengxiaojie.application.config.spring_security.verificationCode.VerificationCodeFilter;

import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private EmployeeAuthenticationProvider employeeAuthenticationProvider;

  @Autowired
  private EmployeeLoginSuccessHandler employeeLoginSuccessHandler;

  @Autowired
  private EmployeeLoginFailureHandler employeeLoginFailureHandler;

  @Autowired
  private EmployeeAccessDeniedHandler employeeAccessDeniedHandler;

  @Autowired
  private EmployeeLogoutSuccessHandler logoutSuccessHandler;

  @Autowired
  private EmployeeAuthenticationEntryPoint employeeAuthenticationEntryPoint;

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    ProviderManager manager = new ProviderManager(Collections.singletonList((employeeAuthenticationProvider)));

    return manager;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
//      允许跨域访问
      .cors()
//      关闭CSRF
      .and()
      .csrf()
        .disable()
//      关闭Session
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//      放行某些请求
      .and()
        .authorizeRequests()
        .antMatchers("/employee/avatar","/employee/reset_password/*/*/*","/department/**","/menu","/employee/login", "/employee/logout", "/verification_code", "/email/**", "/employee/t").permitAll()
        .anyRequest().authenticated()
//      自定义用户登录处理
      .and()
      .formLogin()
      .loginProcessingUrl("/employee/login")
//      自定义用户退出处理
      .and()
      .logout()
        .logoutSuccessHandler(logoutSuccessHandler)
//      自定义异常处理逻辑
      .and()
      .exceptionHandling()
        .authenticationEntryPoint(employeeAuthenticationEntryPoint)
        .accessDeniedHandler(employeeAccessDeniedHandler);

    http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(verificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(employeeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  TokenFilter tokenFilter() throws Exception {
    TokenFilter tokenFilter = new TokenFilter();

    return tokenFilter;
  }

  @Bean
  VerificationCodeFilter verificationCodeFilter() {
    VerificationCodeFilter verificationCodeFilter = new VerificationCodeFilter();

    return verificationCodeFilter;
  }

  @Bean
  EmployeeAuthenticationFilter employeeAuthenticationFilter() throws Exception {
    EmployeeAuthenticationFilter employeeAuthenticationFilter = new EmployeeAuthenticationFilter();

    employeeAuthenticationFilter.setAuthenticationSuccessHandler(employeeLoginSuccessHandler);
    employeeAuthenticationFilter.setAuthenticationFailureHandler(employeeLoginFailureHandler);
    employeeAuthenticationFilter.setAuthenticationManager(authenticationManager());

    return employeeAuthenticationFilter;
  }

//  此处配置是否合理？
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/employee/login", "/employee/logout", "/verification_code", "/email");
  }
}
