package com.ecommerce.dream_shops.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data 
public class ProductDto {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private String description;
	private CategoryDto category;
	private List<ImageDto> images;

	public ProductDto(Long id, String name, String brand, BigDecimal price, int inventory, String description,
			CategoryDto category) {
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.inventory = inventory;
		this.description = description;
		this.category = category;
	}
}
