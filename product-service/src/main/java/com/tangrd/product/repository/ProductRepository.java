package com.tangrd.product.repository;

import com.tangrd.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p WHERE " +
    "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  Page<Product> findByProductCodeOrNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
  
  @Query(value = "SELECT * FROM product WHERE MATCH(name) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE)",
         nativeQuery = true)
  Page<Product> findByNameFullTextSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

  boolean existsByProductCode(String productCode);

  boolean existsByProductCodeAndIdNot(String productCode, Long id);
} 
