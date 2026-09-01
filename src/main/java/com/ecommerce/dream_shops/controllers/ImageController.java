package com.ecommerce.dream_shops.controllers;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.responses.ApiResponse;
import com.ecommerce.dream_shops.service.Image.IImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
	private final IImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,
		@RequestParam  Long productId){

			try {
				List<ImageDto> imageDtos = imageService.saveImages(files, productId);
				return ResponseEntity.ok(new ApiResponse("Upload success", imageDtos));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Upload failed", e.getMessage()));
			}
	 }

	 public ResponseEntity<ApiResponse> downloadImage(@PathVariable  Long imageId){
		Image image = imageService.getImageById(imageId);
		ByteArrayResource resource_new = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
	 }

}
