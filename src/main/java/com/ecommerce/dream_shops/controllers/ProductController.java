package com.ecommerce.dream_shops.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.dto.request.ProductAddRequest;
import com.ecommerce.dream_shops.dto.request.ProductUpdateRequest;
import com.ecommerce.dream_shops.dto.responses.ApiResponse;
import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.service.product.IProductService;
import com.ecommerce.dream_shops.utils.ProductSearchCriteria;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
	private final IProductService productService;

	/**
	 * Returns all products.
	 * @return ResponseEntity<ApiResponse> a list of all products
	 */
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllProducts(){
		
			List<ProductDto> products = productService.getAllProducts();
			return ResponseEntity.ok(new ApiResponse("Success", products));
		
	}

	/**
	 * Returns a product by its ID.
	 * @param id
	 * @return ResponseEntity<ApiResponse> the product with the given ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
		try {
			ProductDto product = productService.getProductById(id);
			return ResponseEntity.ok(new ApiResponse("Success", product));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	/**
	 * Creates a new product.
	 * @param product
	 * @return ResponseEntity<ApiResponse> the created product
	 */
	@PostMapping("/")
	public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductAddRequest product) {
		
		try {
			ProductDto createdProduct = productService.addProduct(product);
			return ResponseEntity.ok(new ApiResponse("Success", createdProduct));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse(e.getMessage(), null));
		}
	}

	/**
	 * Updates an existing product.
	 * @param id
	 * @param product
	 * @return ResponseEntity<ApiResponse> the updated product
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest product) {
		try {
			ProductDto updatedProduct = productService.updateProduct(id, product);
			return ResponseEntity.ok(new ApiResponse("Success", updatedProduct));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage(), null));
		} 
	}

	/**
	 * Deletes a product by its ID.
	 * @param id
	 * @return ResponseEntity<ApiResponse> a message indicating the result of the deletion
	 */
	@DeleteMapping ("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok(new ApiResponse("Success", null));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse> searchProducts(@ModelAttribute ProductSearchCriteria criteria) {
		
		List<ProductDto> products = productService.searchProducts(criteria);
		return ResponseEntity.ok(new ApiResponse("Success", products));
	}
	
	
} 
