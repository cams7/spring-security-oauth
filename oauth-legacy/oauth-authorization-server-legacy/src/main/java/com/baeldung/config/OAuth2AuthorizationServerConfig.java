package com.baeldung.config;

import java.security.KeyPair;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
// import org.springframework.context.annotation.PropertySource;
// import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;
// import org.springframework.jdbc.datasource.init.DataSourceInitializer;
// import org.springframework.jdbc.datasource.init.DatabasePopulator;
// import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
// @PropertySource({"classpath:persistence.properties"})
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  private static final String KEY_STORE = "keystore200619.jks";
  private static final String KEY_STORE_PASSWORD = "abc123";
  private static final String KEY_PAIR_ALIAS = "oauth2_200619";
  private static final String KEY_PAIR_PASSWORD = "abc123";

  // @Autowired
  // private Environment env;

  // @Value("classpath:schema.sql")
  // private Resource schemaScript;

  // @Value("classpath:data.sql")
  // private Resource dataScript;

  // JDBC token store configuration

  // @Bean
  // public DataSource dataSource() {
  // final DriverManagerDataSource dataSource = new DriverManagerDataSource();
  // dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
  // dataSource.setUrl(env.getProperty("jdbc.url"));
  // dataSource.setUsername(env.getProperty("jdbc.user"));
  // dataSource.setPassword(env.getProperty("jdbc.pass"));
  // return dataSource;
  // }

  @Autowired
  private DataSource dataSource;

  // @Bean
  // public DataSourceInitializer dataSourceInitializer() {
  // final DataSourceInitializer initializer = new DataSourceInitializer();
  // initializer.setDataSource(dataSource);
  // initializer.setDatabasePopulator(databasePopulator());
  // return initializer;
  // }

  // private DatabasePopulator databasePopulator() {
  // final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
  // populator.addScript(schemaScript);
  // populator.addScript(dataScript);
  // return populator;
  // }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new CustomTokenEnhancer();
  }

  @Autowired
  private TokenEnhancer tokenEnhancer;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    // converter.setSigningKey("123");

    KeyPair keyPair = keyPair(keyStoreKeyFactory());
    converter.setKeyPair(keyPair);
    return converter;
  }

  @Autowired
  private JwtAccessTokenConverter accessTokenConverter;

  @Bean
  public TokenStore tokenStore() {
    // return new JdbcTokenStore(dataSource);
    return new JwtTokenStore(accessTokenConverter);
  }

  @Autowired
  private TokenStore tokenStore;

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore);
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Override
  public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, accessTokenConverter));
    endpoints.tokenStore(tokenStore).tokenEnhancer(tokenEnhancerChain).authenticationManager(
        authenticationManager)/* .accessTokenConverter(accessTokenConverter) */;
  }

  private static KeyStoreKeyFactory keyStoreKeyFactory() {
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(KEY_STORE),
        KEY_STORE_PASSWORD.toCharArray());
    return keyStoreKeyFactory;
  }

  private static KeyPair keyPair(KeyStoreKeyFactory keyStoreKeyFactory) {
    KeyPair keyPair = keyStoreKeyFactory.getKeyPair(KEY_PAIR_ALIAS, KEY_PAIR_PASSWORD.toCharArray());
    return keyPair;
  }

}
