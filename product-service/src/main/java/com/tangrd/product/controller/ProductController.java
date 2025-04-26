package com.tangrd.product.controller;

import com.tangrd.product.dto.PageResponse;
import com.tangrd.product.dto.ProductDTO;
import com.tangrd.product.dto.ProductSearchRequest;
import com.tangrd.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService ProductService;

  @Autowired
  public ProductController(ProductService ProductService) {
    this.ProductService = ProductService;
  }

  @PostMapping("/search")
  public ResponseEntity<PageResponse<ProductDTO>> searchproduct(@RequestBody ProductSearchRequest request) {
    PageResponse<ProductDTO> pageResponse = ProductService.searchProduct(request);
    return new ResponseEntity<>(pageResponse, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
    ProductDTO productDTO = ProductService.getProductById(id);
    return new ResponseEntity<>(productDTO, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    ProductDTO createdProduct = ProductService.createProduct(productDTO);
    return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
    ProductDTO updatedProduct = ProductService.updateProduct(id, productDTO);
    return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    ProductService.deleteProduct(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
} 
