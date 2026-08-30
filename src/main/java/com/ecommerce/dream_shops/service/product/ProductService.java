package com.ecommerce.dream_shops.service.product;

import java.util.List;
import java.util.Optional;

import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.CategoryRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;
import com.ecommerce.dream_shops.request.AddProductRequest;
import com.ecommerce.dream_shops.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductService implements IProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public Product addProduct(AddProductRequest productRequest) {
		// check if category is found in the db
		// if yes, set it as the product category
		// if not, create a new category and set it as the product category
		Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
			.orElseGet(()-> {
				Category newCategory = new Category(productRequest.getCategory().getName());
				return categoryRepository.save(newCategory);
			});
		
		productRequest.setCategory(category);
		return productRepository.save(createProduct(productRequest, category));
	}

	private Product createProduct(AddProductRequest productRequest, Category category){ 
   return new Product(
		productRequest.getName(),
		productRequest.getBrand(),
		productRequest.getPrice(),
		productRequest.getInventory(),
		productRequest.getDescription(),
		category
	 );
	}

	@Override
	public Product getProductById(Long id) {
	 return productRepository.findById(id)
		.orElseThrow(()-> new ProductNotFoundException("Product not found"));
	}

	@Override
	public Product updateProduct(ProductUpdateRequest request, Long productId) {

		return productRepository.findById(productId)
			.map(existingProduct -> updateExistingProduct(existingProduct, request))
			.map(productRepository::save)
			.orElseThrow(()-> new ProductNotFoundException("Product not found"));
		
	}

	private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
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
				()-> { throw new ProductNotFoundException("Product not found"); }
			);;
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		return productRepository.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}

}
