package com.app.space.system.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import com.app.space.system.repository.ItemDetailsRepository;
import com.app.space.system.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SystemFileService {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ItemDetailsRepository itemDetailsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	public Item createItem(Item item) {
		/* for creating folder and space */
		Item _item = itemRepository.save(item);
		return _item;
	}

	public String uploadFile(MultipartFile file, String itemDetailsStr) throws IOException {

		/* for creating file */
		ItemDetails itemDetails = objectMapper.readValue(itemDetailsStr, ItemDetails.class);
		Item _item = itemRepository.save(itemDetails.getItem());
		itemDetails.setItem(_item);
		itemDetails.setFile(file.getBytes());
		itemDetailsRepository.save(itemDetails);

		String message = "Uploaded the file successfully: " + file.getOriginalFilename();
		return message;

	}

	public Item getFileMetaData(Integer id) {
		Optional<Item> _item = itemRepository.findById(id);
		if (_item.isPresent()) {
			return _item.get();

		} else {
			return null;

		}
	}

	public ItemDetails getFile(Integer id) {
		Optional<ItemDetails> itemDetails = itemDetailsRepository.findById(id);
		if (itemDetails.isPresent()) {
			return itemDetails.get();
		} else {
			return null;

		}

	}
}
