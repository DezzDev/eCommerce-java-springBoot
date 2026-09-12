package com.ecommerce.dream_shops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.dto.CategoryDto;
import com.ecommerce.dream_shops.dto.request.CategoryAddRequest;
import com.ecommerce.dream_shops.dto.request.CategoryUpdateRequest;
import com.ecommerce.dream_shops.exceptions.AlreadyExistsException;
import com.ecommerce.dream_shops.exceptions.CategoryNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;

	private CategoryDto mapCategoryToDto(Category category) {
		return new CategoryDto(category.getId(), category.getName());
	}
	private Category mapDtoToCategory(CategoryDto categoryDto) {
		Category category = new Category();
		category.setId(categoryDto.getId());
		category.setName(categoryDto.getName());
		return category;
	}

	@Override
	public CategoryDto getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		return mapCategoryToDto(category);
	}

	@Override
	public CategoryDto getCategoryByName(String name) {
		Category category =  categoryRepository.findByName(name);
		if (category == null) {
			throw new CategoryNotFoundException("Category not found");
		}
		return mapCategoryToDto(category);

	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream()
				.map(this::mapCategoryToDto)
				.toList();
	}

	@Override
	public CategoryDto addCategory(CategoryAddRequest category) {
		Category categoryEntity = new Category(
			category.getName()
		);
		 return Optional.ofNullable(categoryEntity)
			.filter( c -> !categoryRepository.existsByName(c.getName()))
			.map(c -> {
				Category savedCategory = categoryRepository.save(c);
				return mapCategoryToDto(savedCategory);
			})
			.orElseThrow(()-> new AlreadyExistsException(category.getName() + "Category already exists"));
	}

	@Override
	public CategoryDto updateCategory(Long id, CategoryUpdateRequest category) {

		CategoryDto categoryDto = getCategoryById(id);
		Category categoryEntity = mapDtoToCategory(categoryDto);

		return Optional.ofNullable(categoryEntity)
			.map(oldCategory -> {
				oldCategory.setName(category.getName());
				Category categoryUpdated =  categoryRepository.save(oldCategory);
				return mapCategoryToDto(categoryUpdated);
	})
			.orElseThrow(()-> new CategoryNotFoundException("Category  not found"));
	}

	@Override
	public void deleteCategoryById(Long id) {
		categoryRepository.findById(id)
				.ifPresentOrElse(categoryRepository::delete,
						() -> {
							throw new CategoryNotFoundException("Category not found");
						});
	}

}
