package com.ecommerce.dream_shops.service.product;

import java.util.List;

import com.ecommerce.dream_shops.model.Product;

public interface IProductService {

	Product addProduct(Product product);
	
	Product getProductById(Long id);
	
	void updateProduct(Product product, Long productId);
	
	void deleteProduct(Long id);

	List<Product> getAllProducts();
	List<Product> getProductsByCategory(String category);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String brand, String name);
	Long countProductsByBrandAndName(String brand, String name);


}
