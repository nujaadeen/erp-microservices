package com.zamzamsuper.product_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.zamzamsuper.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

    Optional<Product> findBySku(String sku);
    
    Optional<Product> findByName(String sku);

    @Aggregation("{ $group: { _id: '$category' } }")
    List<String> findAllCategories();
    List<Product> findAllByCategory(String category);

    // TBD
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    List<Product> findByCategoryIgnoreCase(String category);
}
