package com.ecommerce.dream_shops.service.product;

import java.util.List;

import com.ecommerce.dream_shops.dto.request.AddProductRequest;
import com.ecommerce.dream_shops.dto.request.UpdateProductRequest;
import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.utils.ProductSearchCriteria;
public interface IProductService {

	ProductDto addProduct(AddProductRequest product);	
	ProductDto getProductById(Long id);	
	ProductDto updateProduct(Long productId, UpdateProductRequest request);	
	void deleteProduct(Long id);
	List<ProductDto> getAllProducts();
	List<ProductDto> searchProducts(ProductSearchCriteria criteria);
	Long countProductsByBrandAndName(String brand, String name);


}
