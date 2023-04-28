package com.app.space.system.controler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.space.system.entity.Item;
import com.app.space.system.entity.ItemDetails;
import com.app.space.system.service.SystemFileService;

@RestController
@RequestMapping("/api")
public class SystemFileController {

	@Autowired
	SystemFileService systemFileService;

	@PostMapping("/create")
	public ResponseEntity<Item> createItem(@RequestBody Item item) {
		/* for creating folder and space */
		Item _item = systemFileService.createItem(item);
		return new ResponseEntity<>(_item, HttpStatus.CREATED);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("itemDetails") String itemDetailsStr) {
		String message = "";
		try {
			/* for creating file */
			message = systemFileService.uploadFile(file, itemDetailsStr);
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@GetMapping("/getFileMetaData/{id}")
	public ResponseEntity<?> getFileMetaData(@PathVariable Integer id) {
		Item _item = systemFileService.getFileMetaData(id);
		if (_item != null) {
			return ResponseEntity.ok().header(HttpHeaders.ACCEPT).body(_item);

		} else {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION).body("item not found ");

		}
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> getFile(@PathVariable Integer id) {
		ItemDetails itemDetails = systemFileService.getFile(id);
		if (itemDetails != null) {

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + itemDetails.getItem().getName() + "\"")
					.body(itemDetails.getFile());
		} else {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION).body("item not found ");

		}

	}
}
