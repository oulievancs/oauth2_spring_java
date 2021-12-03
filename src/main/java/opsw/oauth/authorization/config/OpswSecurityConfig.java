/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.authorization.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author e.oulis
 */
@EnableWebSecurity
public class OpswSecurityConfig extends WebSecurityConfigurerAdapter
{

  private final PasswordEncoder passwordEncoder;

  @Autowired
  DataSource ds;

  @Override
  @Bean(BeanIds.USER_DETAILS_SERVICE)
  public UserDetailsService userDetailsServiceBean() throws Exception
  {
    return super.userDetailsServiceBean();
  }

  public OpswSecurityConfig(PasswordEncoder passwordEncoder)
  {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception
  {

    // BCryptPasswordEncoder(4) is used for users.password column
    JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> cfg = auth.jdbcAuthentication()
            .passwordEncoder(this.passwordEncoder).dataSource(ds);

    cfg.getUserDetailsService().setEnableGroups(true);
    cfg.getUserDetailsService().setEnableAuthorities(true);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http
            .authorizeRequests()
            .antMatchers("/.well-known/jwks.json").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin();
  }
}
