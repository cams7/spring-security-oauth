package com.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired
  public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
    // @formatter:off
	auth.inMemoryAuthentication()
	  .withUser("john")
	    .password(passwordEncoder.encode("123"))
	    .roles("USER")
	  .and()
	  .withUser("tom")
	    .password(passwordEncoder.encode("111"))
	    .roles("ADMIN")
	  .and()
	  .withUser("user1")
	    .password(passwordEncoder.encode("pass"))
	    .roles("USER")
	  .and()
	  .withUser("admin")
	    .password(passwordEncoder.encode("nimda"))
	    .roles("ADMIN")
	  .and()
	  .withUser("department1")
        .password(passwordEncoder.encode("abc123"))
        .roles("READ_DEPARTMENT")
      .and()
	  .withUser("employee1")
	    .password(passwordEncoder.encode("abc123"))
	    .roles("READ_EMPLOYEE");
    }// @formatter:on

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
	http
	  .csrf().disable()
	  .headers().frameOptions().sameOrigin()
      .and()
      .requestMatchers()            
      .antMatchers("/oauth/authorize*", "/login*", "/logout*","/oauth/token/revokeById/**", "/tokens/**")
      .and()
	  .authorizeRequests()
	  //.antMatchers("/login*").permitAll()
	  .antMatchers("/oauth/token/revokeById/**", "/tokens/**", "/h2/**").permitAll()
	  .anyRequest().authenticated()
	  .and()
	  .formLogin().permitAll();
//	  .and()
//      .logout()
//        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//        .logoutSuccessUrl("/login")
//        .invalidateHttpSession(true)
//        .deleteCookies("JSESSIONID");
	// @formatter:on
  }

}
