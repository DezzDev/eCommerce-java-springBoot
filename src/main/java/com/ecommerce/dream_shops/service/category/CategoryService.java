package com.ecommerce.dream_shops.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.exceptions.CategoryNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name); 
			
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addCategory'");
	}

	@Override
	public Category updateCategory(Category category) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateCategory'");
	}

	@Override
	public void deleteCategoryById(Long id) {
		categoryRepository.findById(id)
			.ifPresentOrElse(categoryRepository::delete, 
				() -> { throw new CategoryNotFoundException("Category not found"); });
	}

}
