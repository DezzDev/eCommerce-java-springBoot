package com.ecommerce.dream_shops.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.request.AddProductRequest;
import com.ecommerce.dream_shops.request.UpdateProductRequest;
import com.ecommerce.dream_shops.responses.ApiResponse;
import com.ecommerce.dream_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
		
			List<Product> products = productService.getAllProducts();
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
			Product product = productService.getProductById(id);
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
	public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {
		
		try {
			Product createdProduct = productService.addProduct(product);
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
	@PostMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest product) {
		try {
			Product updatedProduct = productService.updateProduct(id, product);
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
	
} 
