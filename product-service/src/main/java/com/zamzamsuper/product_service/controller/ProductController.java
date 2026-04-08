package com.zamzamsuper.product_service.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zamzamsuper.product_service.dto.BulkUploadResponse;
import com.zamzamsuper.product_service.dto.ProductRequest;
import com.zamzamsuper.product_service.dto.ProductResponse;
import com.zamzamsuper.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsByCategory(
            @RequestParam(required = false) String category) {

        if (category != null) {
            return ResponseEntity.ok(productService.getAllProductsByCategory(category));
        }

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest productRequest) {

        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<BulkUploadResponse> uploadProducts(
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(productService.bulkUploadExcel(file));
    }

    @GetMapping("/upload/report/{fileName}")
    public ResponseEntity<Resource> downloadReport(@PathVariable String fileName) {
        return productService.downloadReport(fileName);
    }
}
