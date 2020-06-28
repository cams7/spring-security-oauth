package com.baeldung.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.DelegatingJwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Bean
  public JwtClaimsSetVerifier issuerClaimVerifier() {
    try {
      return new IssuerClaimVerifier(new URL("http://localhost:8081"));
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Autowired
  private JwtClaimsSetVerifier issuerClaimVerifier;

  @Bean
  public JwtClaimsSetVerifier customJwtClaimVerifier() {
    return new CustomClaimVerifier();
  }

  @Autowired
  private JwtClaimsSetVerifier customJwtClaimVerifier;

  @Bean
  public JwtClaimsSetVerifier jwtClaimsSetVerifier() {
    return new DelegatingJwtClaimsSetVerifier(Arrays.asList(issuerClaimVerifier, customJwtClaimVerifier));
  }

  @Autowired
  private JwtClaimsSetVerifier jwtClaimsSetVerifier;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    // converter.setSigningKey("123");
    converter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier);

    final Resource resource = new ClassPathResource("public.txt");
    try {
      String publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
      converter.setVerifierKey(publicKey);
      return converter;
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Autowired
  private JwtAccessTokenConverter accessTokenConverter;

  // JWT token store

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
        //.antMatchers("/swagger*", "/v2/**").access("#oauth2.hasScope('read')")
        .anyRequest().permitAll();
    // @formatter:on
  }

  @Override
  public void configure(final ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices);
  }

}
