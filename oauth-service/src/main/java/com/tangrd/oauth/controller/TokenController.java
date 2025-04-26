package com.tangrd.oauth.controller;

import com.tangrd.oauth.dto.TokenValidationRequest;
import com.tangrd.oauth.dto.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Slf4j
@RequiredArgsConstructor
public class TokenController {

  private final JwtDecoder jwtDecoder;

  @PostMapping("/check_token")
  public TokenValidationResponse checkToken(@RequestBody TokenValidationRequest request) {
    log.debug("Checking token: {}", request.getToken().substring(0, 10) + "...");

    TokenValidationResponse response = new TokenValidationResponse();

    try {
      var jwt = jwtDecoder.decode(request.getToken());
      response.setActive(true);

      Map<String, Object> claims = jwt.getClaims();

      response.setClientId((String) claims.getOrDefault("client_id", ""));
      response.setUsername((String) claims.getOrDefault("sub", ""));

      // Extract scope and authorities from JWT
      Object scope = claims.get("scope");
      if (scope instanceof String) {
        response.setScope(Collections.singletonList((String) scope));
      } else if (scope instanceof String[]) {
        response.setScope(java.util.Arrays.asList((String[]) scope));
      }

      Object authorities = claims.get("authorities");
      if (authorities instanceof String) {
        response.setAuthorities(Collections.singletonList((String) authorities));
      } else if (authorities instanceof String[]) {
        response.setAuthorities(java.util.Arrays.asList((String[]) authorities));
      }

      log.info("Token validation successful, user: {}", response.getUsername());

    } catch (JwtException e) {
      log.warn("Token validation failed: {}", e.getMessage());
      response.setActive(false);
    }

    return response;
  }
} 
