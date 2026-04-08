package com.zamzamsuper.product_service.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zamzamsuper.product_service.dto.BarcodeRequest;
import com.zamzamsuper.product_service.dto.BarcodeResponse;
import com.zamzamsuper.product_service.dto.BulkUploadError;
import com.zamzamsuper.product_service.dto.BulkUploadResponse;
import com.zamzamsuper.product_service.dto.ProductRequest;
import com.zamzamsuper.product_service.dto.ProductResponse;
import com.zamzamsuper.product_service.enums.Unit;
import com.zamzamsuper.product_service.model.Barcode;
import com.zamzamsuper.product_service.model.Product;
import com.zamzamsuper.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating product with SKU: {}", productRequest.getSku());

        // Validate duplicate SKU
        productRepository.findBySku(productRequest.getSku())
            .ifPresent(p -> {
                throw new RuntimeException("SKU already exists");
            });

        // Validate duplicate SKU
        productRepository.findByName(productRequest.getName())
            .ifPresent(p -> {
                throw new RuntimeException("Name already exists");
            });

        // Validate duplicate barcodes in request
        validateBarcodes(productRequest.getBarcodes());

        // Convert request DTO -> entity
        Product product = mapToEntity(productRequest);
        
        // Save
        Product savedProduct = productRepository.save(product);

        // Convert Entity → Response DTO
        return mapToResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList(); 
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories()
            .stream()
            .map(String::toLowerCase)
            .distinct()
            .toList();
    }

    public List<ProductResponse> getAllProductsByCategory(String category) {
        return productRepository.findAllByCategory(category.toLowerCase())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        log.info("Updating product with SKU: {}", productRequest.getSku());

        // Validate exitance of the product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setCategory(productRequest.getCategory().toLowerCase());
        product.setTaxRate(productRequest.getTaxRate());
        product.setBaseUnit(productRequest.getBaseUnit());

        // update barcodes if needed
        product.setBarcodes(mapBarcodes(productRequest.getBarcodes()));

        productRepository.save(product);

        // Convert Entity → Response DTO
        return mapToResponse(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public BulkUploadResponse bulkUploadExcel(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new RuntimeException("Only .xlsx files are supported");
        }

        List<Product> validProducts = new ArrayList<>();
        List<BulkUploadError> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);

                try {
                    ProductRequest productRequest = mapExcelRow(row);

                    // Validate
                    validateBarcodes(productRequest.getBarcodes());

                    if (productRepository.findBySku(productRequest.getSku()).isPresent()) {
                        throw new RuntimeException("Duplicate SKU");
                    }

                    if (productRepository.findByName(productRequest.getName()).isPresent()) {
                        throw new RuntimeException("Duplicate Name");
                    }

                    Product product = mapToEntity(productRequest);
                    validProducts.add(product);

                } catch (RuntimeException e) {
                    errors.add(BulkUploadError.builder()
                            .rowNumber(i + 1)
                            .name(getCellValue(row, 0))
                            .error(e.getMessage())
                            .build());
                }
            }

            // ⚡ Batch insert (FAST)
            productRepository.saveAll(validProducts);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file", e);
        }

        // 📄 Generate error report
        String fileName = generateErrorReport(errors);

        return BulkUploadResponse.builder()
                .successCount(validProducts.size())
                .failureCount(errors.size())
                .errors(errors)
                .reportDownloadUrl("/api/product/upload/report/" + fileName)
                .build();
    }

    public ResponseEntity<Resource> downloadReport(String fileName) {
        try {
            Path path = Paths.get("uploads/" + fileName);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found");
        }
    }

    // Convert BarcodeRequest → Barcode
    private List<Barcode> mapBarcodes(List<BarcodeRequest> barcodeRequests) {
        return barcodeRequests.stream()
                .map(b -> Barcode.builder()
                        .code(b.getCode())
                        .unit(b.getUnit())
                        .conversionFactor(b.getConversionFactor())
                        .build())
                .collect(Collectors.toList());
    }

    // Validate duplicate barcode codes
    private void validateBarcodes(List<BarcodeRequest> barcodes) {
        Set<String> codes = new HashSet<>();

        for (BarcodeRequest b : barcodes) {
            if (!codes.add(b.getCode())) {
                throw new RuntimeException("Duplicate barcode: " + b.getCode());
            }
        }
    }

    // Convert Entity → Response DTO
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .category(product.getCategory())
                .taxRate(product.getTaxRate())
                .baseUnit(product.getBaseUnit())
                .barcodes(product.getBarcodes().stream()
                        .map(b -> BarcodeResponse.builder()
                                .code(b.getCode())
                                .unit(b.getUnit())
                                .conversionFactor(b.getConversionFactor())
                                .build())
                        .toList())
                .build();
    }

    // Convert request DTO -> entity
    private Product mapToEntity(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .sku(productRequest.getSku())
                .category(productRequest.getCategory().toLowerCase())
                .taxRate(productRequest.getTaxRate())
                .baseUnit(productRequest.getBaseUnit())
                .barcodes(mapBarcodes(productRequest.getBarcodes()))
                .build();
    }

    private ProductRequest mapExcelRow(Row row) {
        return ProductRequest.builder()
                .name(getCellValue(row, 0))
                .sku(getCellValue(row, 1))
                .category(getCellValue(row, 2))
                .taxRate(new BigDecimal(getCellValue(row, 3)))
                .baseUnit(Unit.valueOf(getCellValue(row, 4).toUpperCase()))
                .barcodes(parseBarcodes(getCellValue(row, 5)))
                .build();
    }

    private List<BarcodeRequest> parseBarcodes(String barcodesCell) {
        if (barcodesCell == null || barcodesCell.isEmpty()) return List.of();

        String[] barcodeStrings = barcodesCell.split(",");
        List<BarcodeRequest> barcodes = new ArrayList<>();

        for (String b : barcodeStrings) {
            String[] parts = b.split(":"); // code:unit:conversion
            if (parts.length == 3) {
                BarcodeRequest barcode = BarcodeRequest.builder()
                        .code(parts[0].trim())
                        .unit(Unit.valueOf(parts[1].trim()))
                        .conversionFactor(Integer.parseInt(parts[2].trim()))
                        .build();
                barcodes.add(barcode);
            }
        }

        return barcodes;
    }

    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell != null ? cell.toString().trim() : "";
    }

    private String generateErrorReport(List<BulkUploadError> errors) {
        if (errors.isEmpty()) return null;

        String fileName = "error_report_" + System.currentTimeMillis() + ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Errors");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Row");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Error");

            int rowIdx = 1;
            for (BulkUploadError err : errors) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(err.getRowNumber());
                row.createCell(1).setCellValue(err.getName());
                row.createCell(2).setCellValue(err.getError());
            }

            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());

            try (OutputStream os = Files.newOutputStream(path)) {
                workbook.write(os);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate error report", e);
        }

        return fileName;
    }
}
