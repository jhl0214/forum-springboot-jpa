package com.forum.forum.service;

import com.forum.forum.domain.Image;
import com.forum.forum.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    /**
     * Add image
     */
    @Transactional
    public Long addImage(Image image) {
        imageRepository.save(image);
        return image.getId();
    }

    /**
     * Get images
     */
    public Image findImageById(Long imageId) {
        return imageRepository.find(imageId);
    }

    public List<Image> findImagesByPostId(Long postId) {
        return imageRepository.findByPostId(postId);
    }

    public List<Image> findAllImages() {
        return imageRepository.findAll();
    }
    
}
