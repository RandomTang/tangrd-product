package com.tangrd.product.service;

import com.tangrd.product.dto.PageResponse;
import com.tangrd.product.dto.ProductDTO;
import com.tangrd.product.dto.ProductSearchRequest;

public interface ProductService {

  PageResponse<ProductDTO> searchProduct(ProductSearchRequest request);

  ProductDTO getProductById(Long id);

  ProductDTO createProduct(ProductDTO productDTO);

  ProductDTO updateProduct(Long id, ProductDTO productDTO);

  void deleteProduct(Long id);
} 
