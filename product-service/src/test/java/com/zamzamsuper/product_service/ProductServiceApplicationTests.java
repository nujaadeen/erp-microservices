package com.zamzamsuper.product_service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import com.mongodb.assertions.Assertions;
import com.zamzamsuper.product_service.dto.BarcodeRequest;
import com.zamzamsuper.product_service.dto.ProductRequest;
import com.zamzamsuper.product_service.enums.Unit;
import com.zamzamsuper.product_service.repository.ProductRepository;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	
	@Container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.2.2"));
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestsString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
			.contentType(MediaType.APPLICATION_JSON)
			.content(productRequestsString))
		.andExpect(status().isCreated());
		Assertions.assertTrue(productRepository.findAll().size() == 1);
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 13")
				.sku("IP13-001")
				.category("Electronics")
				.taxRate(new BigDecimal("12.5")) // example tax rate
				.baseUnit(Unit.PIECE)
				.barcodes(List.of(
						BarcodeRequest.builder()
								.code("111111")
								.unit(Unit.PIECE)
								.conversionFactor(1)
								.build(),
						BarcodeRequest.builder()
								.code("222222")
								.unit(Unit.BOX)
								.conversionFactor(10)
								.build()
				))
				.build();
	}
}
