package com.baeldung.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class JwtCustomHeadersAccessTokenConverter extends JwtAccessTokenConverter {

  private final static JsonParser objectMapper;

  private final Map<String, String> customHeaders;
  private final RsaSigner signer;

  static {
    objectMapper = JsonParserFactory.create();
  }

  public JwtCustomHeadersAccessTokenConverter(final Map<String, String> customHeaders, final KeyPair keyPair) {
    super();
    super.setKeyPair(keyPair);
    this.customHeaders = customHeaders;
    this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
  }

  @Override
  protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    try {
      String content = objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken,
          authentication));
      String token = JwtHelper.encode(content, signer, customHeaders).getEncoded();
      return token;
    } catch (RuntimeException ex) {
      throw new IllegalStateException("Cannot convert access token to JSON", ex);
    }

  }

}
