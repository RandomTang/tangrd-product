package com.tangrd.product.dto;

import lombok.Data;

@Data
public class ProductSearchRequest {

  private String searchTerm;
  private int page = 0;
  private int size = 10;
  private boolean useFulltext = false;

  public void setSize(int size) {
    // Limit to maximum 20 records per page
    this.size = (size <= 20) ? size : 20;
  }
} 
