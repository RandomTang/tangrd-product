package com.tangrd.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  private Long id;

  @NotBlank(message = "Product code cannot be empty")
  @Size(max = 50, message = "Product code cannot exceed 50 characters")
  private String productCode;

  @NotBlank(message = "Product name cannot be empty")
  @Size(max = 100, message = "Product name cannot exceed 100 characters")
  private String name;

  @Size(max = 500, message = "Product description cannot exceed 500 characters")
  private String description;

  @NotNull(message = "Product price cannot be empty")
  @Positive(message = "Product price must be positive")
  private BigDecimal price;

  @NotNull(message = "Stock quantity cannot be empty")
  @Positive(message = "Stock quantity must be positive")
  private Integer stock;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
} 
