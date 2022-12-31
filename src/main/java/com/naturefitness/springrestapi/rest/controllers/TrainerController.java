package com.naturefitness.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.model.Trainer;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.rest.dto.TrainerCreationDTO;
import com.naturefitness.springrestapi.rest.dto.TrainerDTO;
import com.naturefitness.springrestapi.rest.dto.PersonalDataDTO;
import com.naturefitness.springrestapi.rest.dto.TrainerCompleteDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.TrainerService;

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
@RequestMapping("/api/trainers")
public class TrainerController {

	@Autowired
	private TrainerService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer create(@RequestBody TrainerCreationDTO dto) {
		return service.create(Mapper.toTrainer(dto)).getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<TrainerCompleteDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toCompleteDTO)
			.collect(Collectors.toList());
	}

	// @GetMapping
	// @ResponseStatus(HttpStatus.OK)
	// public List<TrainerDTO> findAll() {
	// 	return service.getAll()
	// 		.stream()
	// 		.map(Mapper::toDTO)
	// 		.collect(Collectors.toList());
	// }

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public TrainerCompleteDTO findById(@PathVariable Integer id) {
		return service.getById(id)
			.map(Mapper::toCompleteDTO)
			.orElseThrow(() -> new EntityNotFoundException("Trainer not found"));
	}

//	@PutMapping("{id}")
//	@ResponseStatus(HttpStatus.OK)
//	public Integer replace(@PathVariable Integer id, @RequestBody TrainerDTO dto) {
//		dto.setId(id);
//		return service.replace(Mapper.toTrainer(dto)).getId();
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

	@PostMapping("/{id}/clients")
	@ResponseStatus(HttpStatus.OK)
	public TrainerDTO addTrainers(@PathVariable Integer id, @RequestBody List<Integer> trainers) {
		return Mapper.toDTO(service.addClients(trainers, id));
	}

	@DeleteMapping("/{id}/clients")
	@ResponseStatus(HttpStatus.OK)
	public TrainerDTO removeTrainers(@PathVariable Integer id, @RequestBody List<Integer> trainers) {
		return Mapper.toDTO(service.removeClients(trainers, id));
	}
}

