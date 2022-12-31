package com.naturefitness.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.model.Admin;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.rest.dto.AdminCreationDTO;
import com.naturefitness.springrestapi.rest.dto.AdminDTO;
import com.naturefitness.springrestapi.rest.dto.PersonalDataDTO;
import com.naturefitness.springrestapi.rest.dto.AdminCompleteDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

	@Autowired
	private AdminService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer create(@RequestBody AdminCreationDTO dto) {
		return service.create(Mapper.toAdmin(dto)).getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<AdminCompleteDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toCompleteDTO)
			.collect(Collectors.toList());
	}

	// @GetMapping
	// @ResponseStatus(HttpStatus.OK)
	// public List<AdminDTO> findAll() {
	// 	return service.getAll()
	// 		.stream()
	// 		.map(Mapper::toDTO)
	// 		.collect(Collectors.toList());
	// }

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public AdminCompleteDTO findById(@PathVariable Integer id) {
		return service.getById(id)
			.map(Mapper::toCompleteDTO)
			.orElseThrow(() -> new EntityNotFoundException("Admin not found"));
	}

//	@PutMapping("{id}")
//	@ResponseStatus(HttpStatus.OK)
//	public Integer replace(@PathVariable Integer id, @RequestBody AdminDTO dto) {
//		dto.setId(id);
//		return service.replace(Mapper.toAdmin(dto)).getId();
//	}

	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer updateData(@PathVariable Integer id, @RequestBody PersonalDataDTO dto) {
		return service.updateData(Mapper.toPersonalData(dto), id).getId();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Integer id) {
		service.deleteById(id);
	}
}

