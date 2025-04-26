package com.tangrd.product.oauth2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "oauth-service", url = "${oauth.service.url}")
public interface OAuthClient {

  @PostMapping("/oauth/check_token")
  TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);

} 
