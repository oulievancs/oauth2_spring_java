/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author e.oulis
 */
@Component
public class OpswSecurityConfigCredentials
{

  @Bean("userPasswordEncoder")
  public PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }
}
