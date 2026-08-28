package com.ecommerce.dream_shops.request;

import java.math.BigDecimal;

import com.ecommerce.dream_shops.model.Category;

import lombok.Data;

// we can use @Data here because is not entity direct to database, 
// so we can use toString method without circular reference
@Data
public class AddProductRequest {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private String description;
	private Category category;
}
