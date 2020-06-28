package com.baeldung.config;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

// @Configuration
// @EnableResourceServer
public class OAuth2ResourceServerConfigJwt extends ResourceServerConfigurerAdapter {

  @Autowired
  private CustomAccessTokenConverter customAccessTokenConverter;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(customAccessTokenConverter);

    // converter.setSigningKey("123");
    final Resource resource = new ClassPathResource("public.txt");
    try {
      @SuppressWarnings("deprecation")
      String publicKey = IOUtils.toString(resource.getInputStream());
      converter.setVerifierKey(publicKey);
      return converter;
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Autowired
  private JwtAccessTokenConverter accessTokenConverter;

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter);
  }

  @Autowired
  private TokenStore tokenStore;

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore);
    return defaultTokenServices;
  }

  @Autowired
  private DefaultTokenServices tokenServices;

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      .and()
      .authorizeRequests()
        .anyRequest().permitAll();
    // @formatter:on                
  }

  @Override
  public void configure(final ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices);
  }

}
