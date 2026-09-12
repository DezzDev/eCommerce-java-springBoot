package com.ecommerce.dream_shops.dto;


import lombok.Data;

@Data 
public class CategoryDto {
	private Long id;
	private String name;

	public CategoryDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
