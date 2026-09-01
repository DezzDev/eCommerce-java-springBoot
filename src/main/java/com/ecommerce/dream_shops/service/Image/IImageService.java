package com.ecommerce.dream_shops.service.Image;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.model.Image;

public interface IImageService {
	Image getImageById(Long id);
	void deleteImageById(Long id);
	Image saveImage(MultipartFile file, Long productId);
	void updateImage(MultipartFile file, Long imageId);
}
