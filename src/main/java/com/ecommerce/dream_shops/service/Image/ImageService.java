package com.ecommerce.dream_shops.service.Image;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.exceptions.ImageNotFoundException;
import com.ecommerce.dream_shops.model.Image;
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
	public Image saveImage(MultipartFile file, Long productId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'saveImage'");
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
