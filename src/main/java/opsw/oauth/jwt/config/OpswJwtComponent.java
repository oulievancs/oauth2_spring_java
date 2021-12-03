/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.jwt.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import opsw.core.logging.OpswLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author e.oulis
 */
@Component
public class OpswJwtComponent
{
  public static final String VERIFIER_KEY_ID = new String(Base64.encode(KeyGenerators.secureRandom(32).generateKey()));

  @Value("${app.security.jwt.keystore-location}")
  private String keyStorePath;

  @Value("${app.security.jwt.keystore-password}")
  private String keyStorePassword;

  @Value("${app.security.jwt.key-alias}")
  private String keyAlias;

  @Value("${app.security.jwt.private-key-passphrase}")
  private String privateKeyPassphrase;

  @Bean
  public KeyStore keyStore()
  {
    try
    {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
      keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
      return keyStore;
    }
    catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e)
    {
      OpswLogger.getLogger().error("Unable to load keystore: {}" + keyStorePath, e);
    }

    throw new IllegalArgumentException("Unable to load keystore from keystore {" + keyStorePath + "}.");
  }

  @Bean
  public RSAPrivateKey jwtSigningKey(KeyStore keyStore)
  {
    try
    {
      Key key = keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
      if (key instanceof RSAPrivateKey)
      {
        return (RSAPrivateKey) key;
      }
    }
    catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e)
    {
      OpswLogger.getLogger().error("Unable to load private key from keystore: {}" + keyStorePath, e);
    }

    throw new IllegalArgumentException("Unable to load private key from keystore {" + keyStorePath + ", " + keyAlias + "}.");
  }

  @Bean
  public RSAPublicKey jwtValidationKey(KeyStore keyStore)
  {
    try
    {
      Certificate certificate = keyStore.getCertificate(keyAlias);
      PublicKey publicKey = certificate.getPublicKey();

      if (publicKey instanceof RSAPublicKey)
      {
        return (RSAPublicKey) publicKey;
      }
    }
    catch (KeyStoreException e)
    {
      OpswLogger.getLogger().error("Unable to load private key from keystore: {}" + keyStorePath, e);
    }

    throw new IllegalArgumentException("Unable to load RSA public key from keystore {" + keyStorePath + "}.");
  }

  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey)
  {
    return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
  }
}
