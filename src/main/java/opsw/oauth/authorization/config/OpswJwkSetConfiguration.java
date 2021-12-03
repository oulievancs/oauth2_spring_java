/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.authorization.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import opsw.oauth.jwt.config.OpswJwtComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 *
 * @author e.oulis
 */
@Configuration
@EnableAuthorizationServer
public class OpswJwkSetConfiguration extends AuthorizationServerConfigurerAdapter
{

  @Autowired
  DataSource dataSource;

  AuthenticationManager authenticationManager;

  @Autowired
  OpswJwtComponent keyPair;

  @Autowired
  private ClientDetailsService clientDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception
  {
    clients.jdbc(dataSource);
  }

  public OpswJwkSetConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception
  {

    this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
  }

  // ... client configuration, etc.
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
  {
    // @formatter:off
    endpoints
            .authenticationManager(this.authenticationManager)
            .accessTokenConverter(accessTokenConverter())
            .tokenStore(tokenStore());
    // @formatter:on
  }

  @Bean
  public UserApprovalHandler userApprovalHandler()
  {
    ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
    userApprovalHandler.setApprovalStore(approvalStore());
    userApprovalHandler.setClientDetailsService(this.clientDetailsService);
    userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
    return userApprovalHandler;
  }

  @Bean
  public ApprovalStore approvalStore()
  {
    return new JdbcApprovalStore(dataSource);
  }

  @Bean
  public TokenStore tokenStore()
  {
    JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
    tokenStore.setApprovalStore(approvalStore());
    return tokenStore;
  }

//  @Bean
//  public JwtAccessTokenConverter accessTokenConverter()
//  {
//    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//    converter.setKeyPair(new KeyPair(this.keyPair.jwtValidationKey(this.keyPair.keyStore()), this.keyPair.jwtSigningKey(this.keyPair.keyStore())));
//    return converter;
//  }
  @Bean
  public JwtAccessTokenConverter accessTokenConverter()
  {
    final RsaSigner signer = new RsaSigner(this.keyPair.jwtSigningKey(this.keyPair.keyStore()));

    JwtAccessTokenConverter converter = new JwtAccessTokenConverter()
    {
      private JsonParser objectMapper = JsonParserFactory.create();

      @Override
      protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
      {
        String content;
        try
        {
          content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        }
        catch (Exception ex)
        {
          throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("kid", OpswJwtComponent.VERIFIER_KEY_ID);
        String token = JwtHelper.encode(content, signer, headers).getEncoded();
        return token;
      }
    };
    converter.setSigner(signer);
    converter.setVerifier(new RsaVerifier(this.keyPair.jwtValidationKey(this.keyPair.keyStore())));
    return converter;
  }

  @Bean
  public JWKSet jwkSet()
  {
    RSAKey.Builder builder = new RSAKey.Builder(this.keyPair.jwtValidationKey(this.keyPair.keyStore()))
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(OpswJwtComponent.VERIFIER_KEY_ID);
    return new JWKSet(builder.build());
  }
}
