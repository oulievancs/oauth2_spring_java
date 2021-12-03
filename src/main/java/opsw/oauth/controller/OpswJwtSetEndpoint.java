/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author e.oulis
 */
@RestController
public class OpswJwtSetEndpoint
{

	@Autowired
	JWKSet jwkSet;

  @GetMapping(value = "/.well-known/jwks.json", produces = "application/json; charset=UTF-8")
  @ResponseBody
  public Map<String, Object> getKey(Principal principal)
  {
    return this.jwkSet.toJSONObject();
  }
}
