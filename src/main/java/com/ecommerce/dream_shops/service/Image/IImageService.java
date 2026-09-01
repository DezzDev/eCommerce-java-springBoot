package com.ecommerce.dream_shops.service.Image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.dto.ImageDto;

public interface IImageService {
	Image getImageById(Long id);
	void deleteImageById(Long id);
	List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
	void updateImage(MultipartFile file, Long imageId);
}
