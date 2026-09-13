package com.ecommerce.dream_shops.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.dto.CategoryDto;
import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.dto.request.ProductAddRequest;
import com.ecommerce.dream_shops.dto.request.ProductUpdateRequest;
import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.CategoryRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import com.ecommerce.dream_shops.specification.ProductSpecification;
import com.ecommerce.dream_shops.utils.ProductSearchCriteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	private ProductDto productToProductDto(Product product) {
		return new ProductDto(
			product.getId(),
			product.getName(),
			product.getBrand(),
			product.getPrice(),
			product.getInventory(),
			product.getDescription(),
			new CategoryDto(
			product.getCategory().getId(),
			product.getCategory().getName()),
			product.getImages()
				.stream()
				.map(image -> new ImageDto(
						image.getId(),
						image.getFileName(),
						image.getFileType(),
						image.getDownloadUrl()
				))
				.toList()
			);
		}

	@Override
	public ProductDto addProduct(ProductAddRequest productRequest) {
		// check if category is found in the db
		// if yes, set it as the product category
		// if not, create a new category and set it as the product category
		Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
				.orElseGet(() -> {
					Category newCategory = new Category(productRequest.getCategory().getName());
					return categoryRepository.save(newCategory);
				});

		productRequest.setCategory(category);
		Product product = productRepository.save(createProduct(productRequest, category));

		return productToProductDto(product);
	}

	private Product createProduct(ProductAddRequest productRequest, Category category) {
		return new Product(
				productRequest.getName(),
				productRequest.getBrand(),
				productRequest.getPrice(),
				productRequest.getInventory(),
				productRequest.getDescription(),
				category);
	}

	@Override
	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		return productToProductDto(product);
	}

	@Override
	public ProductDto updateProduct(Long productId, ProductUpdateRequest request) {
		Product product = productRepository.findById(productId)
			.map(existingProduct -> updateExistingProduct(existingProduct, request))
			.map(productRepository::save)
			.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		return productToProductDto(product);

	}

	private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());

		Category category = categoryRepository.findByName(request.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
	}

	@Override
	public void deleteProduct(Long id) {
		productRepository.findById(id)
			.ifPresentOrElse(productRepository::delete,
				() -> {
					throw new ProductNotFoundException("Product not found");
				});

	}

	@Override
	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream()
			.map((product) -> {
				return productToProductDto(product);
			})
			.toList();
	}

	@Override
	public List<ProductDto> searchProducts(ProductSearchCriteria criteria) {

		Specification<Product> specification = Specification.allOf();

		if (criteria.brand() != null && !criteria.brand().isBlank()) {
			specification = specification.and(
					ProductSpecification.hasBrand(criteria.brand()));
		}

		if (criteria.name() != null && !criteria.name().isBlank()) {
			specification = specification.and(
					ProductSpecification.hasName(criteria.name()));
		}

		if (criteria.category() != null && !criteria.category().isBlank()) {
			specification = specification.and(
					ProductSpecification.hasCategory(criteria.category()));
		}

		return productRepository.findAll(specification)
				.stream()
				.map(this::productToProductDto)
				.toList();
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}

}
