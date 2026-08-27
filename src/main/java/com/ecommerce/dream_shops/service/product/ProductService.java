package com.ecommerce.dream_shops.service.product;

import java.util.List;

import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductService implements IProductService {

	private final ProductRepository productRepository;

	@Override
	public Product addProduct(Product product) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
	}

	@Override
	public Product getProductById(Long id) {
	 return productRepository.findById(id)
		.orElseThrow(()-> new ProductNotFoundException("Product not found"));
	}

	@Override
	public void updateProduct(Product product, Long productId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
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
