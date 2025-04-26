package com.tangrd.product.controller;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

  @GetMapping("/userinfo")
  public ResponseEntity<UserInfo> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
    log.info("Getting user information, user: {}", jwt.getSubject());

    return ResponseEntity.ok(
      UserInfo.builder()
        .sub(jwt.getSubject())
        .name((String) jwt.getClaims().getOrDefault("name", "unknown"))
        .authorities(jwt.getClaims().get("authorities"))
        .scope(jwt.getClaims().get("scope"))
        .build()
    );
  }

  @GetMapping("/test")
  public ResponseEntity<String> testAuth() {
    log.info("Test authentication API called");
    return ResponseEntity.ok("Authentication successful! You have been verified via OAuth2.0 and can access protected resources");
  }

  @Data
  @Builder
  static class UserInfo {
    private String sub;
    private String name;
    private Object authorities;
    private Object scope;
  }
} 
