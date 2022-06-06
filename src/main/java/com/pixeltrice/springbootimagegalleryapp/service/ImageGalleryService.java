package com.pixeltrice.springbootimagegalleryapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixeltrice.springbootimagegalleryapp.entity.ImageGallery;
import com.pixeltrice.springbootimagegalleryapp.repository.ImageGalleryRepository;



@Service
public class ImageGalleryService {

	@Autowired
	private ImageGalleryRepository imageGalleryRepository;
	
	public void saveImage(ImageGallery imageGallery) {
		imageGalleryRepository.save(imageGallery);	
	}

	public List<ImageGallery> getAllActiveImages() {
		return imageGalleryRepository.findAll();
	}

	public Optional<ImageGallery> getImageById(Long id) {
		return imageGalleryRepository.findById(id);
	}
	public ImageGallery getImage(Long id) {
		return imageGalleryRepository.findById(id).get();
	}


	public ImageGallery update(ImageGallery imageGallery) {
		return imageGalleryRepository.save(imageGallery);
	}
	public void deleteById(Long id) {
		imageGalleryRepository.deleteById(id);
	}

//	public ImageGallery verify(Long id)
//	{
////		return imageGalleryRepository.findById(id);
//	}

//public void updateC(Long id) {
//	ImageGallery imageGallery = imageGalleryRepository.findId(id);
//	imageGallery.setStatus("verify");
//	imageGalleryRepository.save(imageGallery);
//}

	public void updatea(Long id, String status)
	{
		imageGalleryRepository.updatestatus(id,status);
	}

}

