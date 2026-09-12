package com.ecommerce.dream_shops.dto;

import java.sql.Blob;

import lombok.Data;

@Data 
public class ImageDto {
	private Long id;
	private String fileName;
	private String fileType;
	private Blob image;
	private String downloadUrl;
	private ProductDto product;
}
