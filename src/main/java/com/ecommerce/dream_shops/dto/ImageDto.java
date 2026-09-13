package com.ecommerce.dream_shops.dto;

import lombok.AllArgsConstructor;

import java.sql.Blob;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ImageDto {
	private Long id;
	private String fileName;
	private String fileType;
	private Blob image;
	private String downloadUrl;
	private ProductDto product;

	public ImageDto(Long id, String fileName, String fileType, String downloadUrl) {
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.downloadUrl = downloadUrl;
	}
}
