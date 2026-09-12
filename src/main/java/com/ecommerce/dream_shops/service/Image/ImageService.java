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
import com.ecommerce.dream_shops.exceptions.ProductNotFoundException;
import com.ecommerce.dream_shops.model.Category;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.ImageRepository;
import com.ecommerce.dream_shops.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ImageService implements IImageService {

	private final ImageRepository imageRepository;
	private final ProductRepository productRepository;

	private ImageDto imageToImageDto(Image image) {
		ImageDto imageDto = new ImageDto();
		imageDto.setId(image.getId());
		imageDto.setFileName(image.getFileName());
		imageDto.setDownloadUrl(image.getDownloadUrl());
		return imageDto;
	}
	 
	@Override
	public ImageDto getImageById(Long id) {
		Image image = imageRepository
			.findById(id)
			.orElseThrow(() -> new ImageNotFoundException("Image not found")); 
		
		return imageToImageDto(image);
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

		 Product product = productRepository.findById(productId)
			.orElseThrow(()-> new ProductNotFoundException("Product not found"));

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
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
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
		ImageDto imageDto = getImageById(imageId);
		Image image = new Image();
		try {
			image.setId(imageDto.getId());
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			image.setDownloadUrl(imageDto.getDownloadUrl());
			image.setProduct(
				new Product(
					imageDto.getProduct().getId(),
					imageDto.getProduct().getName(),
					imageDto.getProduct().getBrand(),
					imageDto.getProduct().getPrice(),
					imageDto.getProduct().getInventory(),
					imageDto.getProduct().getDescription(),
					new Category(
						imageDto.getProduct().getCategory().getId(),
						imageDto.getProduct().getCategory().getName()
					),
					imageDto.getProduct().getImages().stream()
						.map(imgDto -> {
							Image img = new Image();
							img.setId(imgDto.getId());
							img.setFileName(imgDto.getFileName());
							img.setFileType(imgDto.getFileType());
							img.setImage(imgDto.getImage());
							img.setDownloadUrl(imgDto.getDownloadUrl());
							return img;
						})
						.toList()
				)
			);
			imageRepository.save(image);

		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
