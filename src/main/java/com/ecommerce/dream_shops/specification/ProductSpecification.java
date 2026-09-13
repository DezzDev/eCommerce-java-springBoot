package com.ecommerce.dream_shops.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Product;

import jakarta.persistence.criteria.Join;

public class ProductSpecification {

	public static Specification<Product> hasBrand(String brand) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand"), brand);
	}

	public static Specification<Product> hasName(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(
				criteriaBuilder.lower(root.get("name")),
				"%" + name.toLowerCase() + "%");
	}

	public static Specification<Product> hasCategory(String category) {
		return (root, query, criteriaBuilder) -> {
			
			Join<Product, Category> categoryJoin = root.join("category");

			return criteriaBuilder.equal(categoryJoin.get("name"), category);
		};
	}
}
