package com.ecommerce.dream_shops.service.category;

import java.util.List;

import com.ecommerce.dream_shops.dto.CategoryDto;
import com.ecommerce.dream_shops.dto.request.CategoryAddRequest;
import com.ecommerce.dream_shops.dto.request.CategoryUpdateRequest;

public interface ICategoryService {
	CategoryDto getCategoryById(Long id);
	CategoryDto getCategoryByName(String name);
	List<CategoryDto> getAllCategories();
	CategoryDto addCategory(CategoryAddRequest category);
	CategoryDto updateCategory(Long id, CategoryUpdateRequest category);
	void deleteCategoryById(Long id);
}
