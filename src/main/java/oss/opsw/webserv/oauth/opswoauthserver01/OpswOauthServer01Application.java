/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oss.opsw.webserv.oauth.opswoauthserver01;

import javax.naming.NamingException;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import opsw.oauth.opswweb.config.OpswWebRestContextListener;
import opsw.oauth.authorization.config.OpswJwkSetConfiguration;
import opsw.oauth.authorization.config.OpswSecurityConfig;
import opsw.oauth.controller.OpswJwtSetEndpoint;
import opsw.oauth.jwt.config.OpswJwtComponent;
import opsw.oauth.security.config.OpswSecurityConfigCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;

/**
 *
 * @author e.oulis
 */
@SpringBootApplication
@ComponentScan(basePackageClasses =
{
  OpswJwtComponent.class,
  OpswSecurityConfigCredentials.class,
  OpswJwtSetEndpoint.class,
  OpswJwkSetConfiguration.class
})
//@ConfigurationPropertiesScan(basePackageClasses = {
//  OpswJwkSetConfiguration.class,
//  OpswSecurityConfig.class
//})
public class OpswOauthServer01Application extends SpringBootServletInitializer
{

  @Autowired
  private Environment env;

  public static void main(String[] args)
  {
    SpringApplication.run(OpswOauthServer01Application.class, args);
  }

  @Bean
  public DataSource dataSource() throws NamingException
  {
    System.out.println("ENV Resource: " + env.getProperty("jdbc.url"));
    return (DataSource) new JndiTemplate().lookup(env.getProperty("jdbc.url"));
  }

  /**
   * Πληροφορίες:
   * https://stackoverflow.com/questions/26678208/spring-boot-shutdown-hook/26678606
   * .
   */
  @NotNull
  @Bean
  ServletListenerRegistrationBean<ServletContextListener> myServletListener()
  {
    ServletListenerRegistrationBean<ServletContextListener> srb
            = new ServletListenerRegistrationBean<>();
    srb.setListener(new OpswWebRestContextListener());
    return srb;
  }
}
