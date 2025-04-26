package com.tangrd.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {

  private boolean active;
  private String clientId;
  private List<String> scope;
  private String username;
  private List<String> authorities;
} 
