package com.ecommerce.dream_shops.service.Image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.exceptions.ImageNotFoundException;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.ImageRepository;
import com.ecommerce.dream_shops.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ImageService implements IImageService {

	private final ImageRepository imageRepository;
	private final ProductService productService;
	 
	@Override
	public Image getImageById(Long id) {
		return imageRepository
			.findById(id)
			.orElseThrow(() -> new ImageNotFoundException("Image not found")); 
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository
		.findById(id)
		.ifPresentOrElse(
			imageRepository::delete,
			 ()-> {throw new ImageNotFoundException("Image not found");});
	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {

		 Product product= productService.getProductById(productId);
		 List<ImageDto> savedImageDtos = new ArrayList<>();

		 for(MultipartFile file : files){
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);

				String buildDownloadUrl = "/api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl + image.getId();
				image.setDownloadUrl(downloadUrl);
				Image savedImage = imageRepository.save(image);

				// Update the download URL after saving the image to get the generated ID
				savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
				imageRepository.save(savedImage);

				ImageDto imageDto = new ImageDto();
				imageDto.setImageId(savedImage.getId());
				imageDto.setImageName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDtos.add(imageDto);

			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		 }
		 return savedImageDtos;
		 
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);

		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
