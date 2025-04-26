package com.tangrd.product.service.impl;

import com.tangrd.product.dto.PageResponse;
import com.tangrd.product.dto.ProductDTO;
import com.tangrd.product.dto.ProductSearchRequest;
import com.tangrd.product.entity.Product;
import com.tangrd.product.exception.ResourceAlreadyExistsException;
import com.tangrd.product.exception.ResourceNotFoundException;
import com.tangrd.product.repository.ProductRepository;
import com.tangrd.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductDTO> searchProduct(ProductSearchRequest request) {
    log.debug("Searching product, search term: {}, page: {}, size: {}, use fulltext: {}",
      request.getSearchTerm(), request.getPage(), request.getSize(), request.isUseFulltext());

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Product> productPage;

    if (request.getSearchTerm() == null || request.getSearchTerm().trim().isEmpty()) {
      productPage = productRepository.findAll(pageable);
    } else if (request.isUseFulltext()) {
      // Use MySQL full-text search for better performance
      productPage = productRepository.findByNameFullTextSearch(
        request.getSearchTerm(), pageable);
    } else {
      // Use LIKE search for more flexible matching
      productPage = productRepository.findByProductCodeOrNameContainingIgnoreCase(
        request.getSearchTerm(), pageable);
    }

    List<ProductDTO> productDTOs = productPage.getContent().stream()
      .map(this::convertToDTO)
      .collect(Collectors.toList());

    log.debug("Search product result: total records: {}, total pages: {}",
      productPage.getTotalElements(), productPage.getTotalPages());

    return new PageResponse<>(
      productDTOs,
      productPage.getTotalPages(),
      productPage.getTotalElements(),
      productPage.getNumber(),
      productPage.getSize()
    );
  }

  @Override
  @Transactional(readOnly = true)
  public ProductDTO getProductById(Long id) {
    log.debug("Fetching product by ID: {}", id);

    Product product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found, ID: " + id));

    return convertToDTO(product);
  }

  @Override
  @Transactional
  public ProductDTO createProduct(ProductDTO productDTO) {
    log.debug("Creating new product: {}", productDTO.getProductCode());

    if (productRepository.existsByProductCode(productDTO.getProductCode())) {
      log.warn("Failed to create product, product code already exists: {}", productDTO.getProductCode());
      throw new ResourceAlreadyExistsException("Product code already exists: " + productDTO.getProductCode());
    }

    Product product = convertToEntity(productDTO);
    Product savedProduct = productRepository.save(product);

    log.info("Product created successfully, ID: {}", savedProduct.getId());

    return convertToDTO(savedProduct);
  }

  @Override
  @Transactional
  public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
    log.debug("Updating product, ID: {}", id);

    Product existingProduct = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found, ID: " + id));

    if (!existingProduct.getProductCode().equals(productDTO.getProductCode()) &&
      productRepository.existsByProductCode(productDTO.getProductCode())) {
      log.warn("Failed to update product, product code already exists: {}", productDTO.getProductCode());
      throw new ResourceAlreadyExistsException("Product code already exists: " + productDTO.getProductCode());
    }

    existingProduct.setProductCode(productDTO.getProductCode());
    existingProduct.setName(productDTO.getName());
    existingProduct.setDescription(productDTO.getDescription());
    existingProduct.setPrice(productDTO.getPrice());
    existingProduct.setStock(productDTO.getStock());

    Product updatedProduct = productRepository.save(existingProduct);

    log.info("Product updated successfully, ID: {}", updatedProduct.getId());

    return convertToDTO(updatedProduct);
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    log.debug("Deleting product, ID: {}", id);

    if (!productRepository.existsById(id)) {
      log.warn("Failed to delete product, product not found, ID: {}", id);
      throw new ResourceNotFoundException("Product not found, ID: " + id);
    }

    productRepository.deleteById(id);

    log.info("Product deleted successfully, ID: {}", id);
  }

  private ProductDTO convertToDTO(Product product) {
    ProductDTO productDTO = new ProductDTO();
    BeanUtils.copyProperties(product, productDTO);
    return productDTO;
  }

  private Product convertToEntity(ProductDTO productDTO) {
    Product product = new Product();
    BeanUtils.copyProperties(productDTO, product);
    return product;
  }
} 
