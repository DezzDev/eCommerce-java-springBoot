package com.ecommerce.dream_shops.service.product;

import java.util.List;

import com.ecommerce.dream_shops.dto.request.ProductAddRequest;
import com.ecommerce.dream_shops.dto.request.ProductUpdateRequest;
import com.ecommerce.dream_shops.dto.ProductDto;
import com.ecommerce.dream_shops.utils.ProductSearchCriteria;
public interface IProductService {

	ProductDto addProduct(ProductAddRequest product);	
	ProductDto getProductById(Long id);	
	ProductDto updateProduct(Long productId, ProductUpdateRequest request);	
	void deleteProduct(Long id);
	List<ProductDto> getAllProducts();
	List<ProductDto> searchProducts(ProductSearchCriteria criteria);
	Long countProductsByBrandAndName(String brand, String name);


}
