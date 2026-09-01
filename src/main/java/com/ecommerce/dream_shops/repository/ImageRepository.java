package com.ecommerce.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.dream_shops.model.Image;

/**
 * ImageRepository
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

}
