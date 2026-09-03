package com.ecommerce.dream_shops.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dream_shops.exceptions.AlreadyExistsException;
import com.ecommerce.dream_shops.exceptions.CategoryNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.responses.ApiResponse;
import com.ecommerce.dream_shops.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
	private final ICategoryService categoryService;

	/** Returns all categories. */
	@GetMapping("/")
	public ResponseEntity<ApiResponse> getAllCategories() {
		try {
			List<Category> categories = categoryService.getAllCategories();
			return ResponseEntity.ok(new ApiResponse("Success", categories));
		} catch (Exception e) {
			if (e instanceof CategoryNotFoundException) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

	/** Creates a new category. */
	@PostMapping("/")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
		try {
			Category savedCategory = categoryService.addCategory(category);
			return ResponseEntity.ok(new ApiResponse("Success", savedCategory));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

	/** Returns a category by its ID. */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoryService.getCategoryById(id);
			return ResponseEntity.ok(new ApiResponse("Success", category));
		} catch (CategoryNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

	/** Returns a category by its name. */
	@GetMapping("/name/{name}")
	public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
		try {
			Category category = categoryService.getCategoryByName(name);
			return ResponseEntity.ok(new ApiResponse("Success", category));
		} catch (CategoryNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}

	/** Deletes a category by its ID. */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
		try {
			categoryService.deleteCategoryById(id);
			return ResponseEntity.ok(new ApiResponse("Success", null));
		} catch (CategoryNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}


	/** Update Category by its ID */
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
		
		try {
			Category updatedCategory = categoryService.updateCategory(id, category);
			return ResponseEntity.ok(new ApiResponse("Success", updatedCategory));
		} catch (CategoryNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
	}

}
