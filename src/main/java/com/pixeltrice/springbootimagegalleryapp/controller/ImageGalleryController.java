package com.pixeltrice.springbootimagegalleryapp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pixeltrice.springbootimagegalleryapp.entity.ImageGallery;
import com.pixeltrice.springbootimagegalleryapp.service.ImageGalleryService;


@Controller
public class ImageGalleryController {
	
	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private ImageGalleryService imageGalleryService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = {"/", "/home"})
	public String addProductPage() {
		return "index";
	}

	@PostMapping("/image/saveImageDetails")
	public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("no_hp") String no_hp, @RequestParam("file_id") String file_id, Model model, HttpServletRequest request
			,@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2, @RequestParam("similarity") float similarity) {
		try {
			//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
//			String[] names = username.split(",");
//			String[] descriptions = description.split(",");
			Date createDate = new Date();
			log.info("Name: " + username+" "+filePath);
			log.info("password: " + password);
			log.info("no_hp: " + no_hp);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			ImageGallery imageGallery = new ImageGallery();

			imageGallery.setData(imageData);
			imageGallery.setData2(file2.getBytes());
			imageGallery.setFileName(fileName);
			imageGallery.setFileType(file.getContentType());
			imageGallery.setFile_id(file_id);
			imageGallery.setSimilarity(similarity);
			imageGallery.setPath(filePath);
			imageGallery.setUsername(username);
			imageGallery.setPassword(password);
			imageGallery.setNo_hp(no_hp);

			imageGalleryService.saveImage(imageGallery);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));

			return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/image/display/{type}/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id,@PathVariable("type") String type, HttpServletResponse response, Optional<ImageGallery> imageGallery)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		imageGallery = imageGalleryService.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		if(type.equalsIgnoreCase("data1")){
			response.getOutputStream().write(imageGallery.get().getData());
			response.getOutputStream().close();
		}else{
			response.getOutputStream().write(imageGallery.get().getData2());
			response.getOutputStream().close();
		}
	}

	@GetMapping("/image/show")
	String show(Model map) {
		List<ImageGallery> images = imageGalleryService.getAllActiveImages();
		map.addAttribute("images", images);
		return "images";
	}

	@GetMapping("/image/edit/imageDetails/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("image", imageGalleryService.getImage(id));
		return "imagedetails";
	}

	@PostMapping("/image/imageDetails/{id}")
	public String editStudentForm(@PathVariable Long id,@ModelAttribute("image") ImageGallery imageGallery, Model model, @RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2 ) throws IOException {

		ImageGallery imageGallery1 = imageGalleryService.getImage(id);

		imageGallery1.setId(id);
		imageGallery1.setUsername(imageGallery.getUsername());
		imageGallery1.setPassword(imageGallery.getPassword());
		imageGallery1.setNo_hp(imageGallery.getNo_hp());
		imageGallery1.setFile_id(imageGallery.getFile_id());
		imageGallery1.setSimilarity(imageGallery.getSimilarity());

		imageGallery1.setData(file.getBytes());
		imageGallery1.setData2(file2.getBytes());

		imageGalleryService.update(imageGallery1);

		  return "redirect:/image/show";
	}

	@GetMapping("/image/show/{id}")
	public String delete(@PathVariable Long id) {
		imageGalleryService.deleteById(id);
		return "redirect:/image/show";
	}
//	@PostMapping("/image/{id}")
//	public String updateStudent(@PathVariable Long id,
//								@ModelAttribute("images") ImageGallery imageGallery,
//								Model model) {
//
//		// get student from database by id
//		ImageGallery exist = imageGalleryService.getImage(id);
//		exist.setId(id);
//		exist.setFile_id(imageGallery.getFile_id());
//		exist.setFileName(imageGallery.getFileName());
//		exist.setSimilarity(imageGallery.getSimilarity());
//
//		// save updated student object
//		imageGalleryService.update(imageGallery);
//		return "redirect:imagedetails";
//	}
//

//	@GetMapping("/image/imageDetails")
//	String showProductDetails(@RequestParam("id") Long id, Optional<ImageGallery> imageGallery, Model model) {
//		try {
//			log.info("Id :: " + id);
//			if (id != 0) {
//				imageGallery = imageGalleryService.getImageById(id);
//
//				log.info("products :: " + imageGallery);
//				if (imageGallery.isPresent()) {
////					model.addAttribute("id", imageGallery.get().getId());
////					model.addAttribute("name", imageGallery.get().getUsername());
////					model.addAttribute("password", imageGallery.get().getPassword());
////					model.addAttribute("no_hp", imageGallery.get().getNo_hp());
////					model.addAttribute("file_id", imageGallery.get().getFile_id());
////					model.addAttribute("similarity", imageGallery.get().getSimilarity());
//
//					ImageGallery imageGallery1 = imageGalleryService.getImage(id);
//					imageGallery1.setId(id);
//					imageGallery1.setUsername(imageGallery1.getUsername());
//					imageGallery1.setPassword(imageGallery1.getPassword());
//					imageGallery1.setNo_hp(imageGallery1.getNo_hp());
//					imageGallery1.setFile_id(imageGallery1.getFile_id());
//					imageGallery1.setSimilarity(imageGallery1.getSimilarity());
//
//					return "imagedetails";
//				}
//				return "redirect:/home";
//			}
//		return "redirect:/home";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "redirect:/home";
//		}
//	}


}	

