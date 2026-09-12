package com.ecommerce.dream_shops.dto.request;

import java.math.BigDecimal;

import com.ecommerce.dream_shops.model.Category;
import lombok.Data;

@Data
public class ProductUpdateRequest {
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private String description;
	private Category category;
}
