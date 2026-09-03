package com.ecommerce.dream_shops.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.exceptions.ImageNotFoundException;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.responses.ApiResponse;
import com.ecommerce.dream_shops.service.Image.IImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
	private final IImageService imageService;

	/** Saves one or more images for a product. */
	@PostMapping("/")
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

	 /** Downloads an image by its ID. */
	 @GetMapping("/{id}")
	 public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException{
		Image image = imageService.getImageById(id);
		ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" +  image.getFileName() + "\"" )
			.body(resource);
	}

	/** Replaces an existing image by its ID. */
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateImage(@PathVariable Long id, @RequestBody MultipartFile file){
		try{
			Image image = imageService.getImageById(id);
			if(image != null){
				imageService.updateImage(file, id);
				return ResponseEntity.ok(new ApiResponse("Update success", null));

			}
			
		}catch(ImageNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage(), null));

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse("Update failed", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/** Deletes an image by its ID. */
	@DeleteMapping ("/{id}")
	public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id){
		try{
			Image image = imageService.getImageById(id);
			if(image != null){
				imageService.deleteImageById(id);
				return ResponseEntity.ok(new ApiResponse("Delete success", null));
			}
			
		}catch(ImageNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage(), null));

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
