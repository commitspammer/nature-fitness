package com.naturefitness.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.model.Client;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.rest.dto.ClientCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.ClientCreationDTO;
import com.naturefitness.springrestapi.rest.dto.ClientDTO;
import com.naturefitness.springrestapi.rest.dto.PersonalDataDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	@Autowired
	private ClientService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer create(@RequestBody ClientCreationDTO dto) {
		return service.create(Mapper.toClient(dto)).getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ClientCompleteDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toCompleteDTO)
			.collect(Collectors.toList());
	}

	// @GetMapping
	// @ResponseStatus(HttpStatus.OK)
	// public List<ClientDTO> findAll() {
	// 	return service.getAll()
	// 		.stream()
	// 		.map(Mapper::toDTO)
	// 		.collect(Collectors.toList());
	// }

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ClientCompleteDTO findById(@PathVariable Integer id) {
		return service.getById(id)
			.map(Mapper::toCompleteDTO)
			.orElseThrow(() -> new EntityNotFoundException("Client not found"));
	}

//	@PutMapping("{id}")
//	@ResponseStatus(HttpStatus.OK)
//	public Integer replace(@PathVariable Integer id, @RequestBody ClientDTO dto) {
//		dto.setId(id);
//		return service.replace(Mapper.toClient(dto)).getId();
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

	@PostMapping("/{id}/trainers")
	@ResponseStatus(HttpStatus.OK)
	public ClientDTO addTrainers(@PathVariable Integer id, @RequestBody List<Integer> trainers) {
		return Mapper.toDTO(service.addTrainers(trainers, id));
	}

	@DeleteMapping("/{id}/trainers")
	@ResponseStatus(HttpStatus.OK)
	public ClientDTO removeTrainers(@PathVariable Integer id, @RequestBody List<Integer> trainers) {
		return Mapper.toDTO(service.removeTrainers(trainers, id));
	}

	@PostMapping("/{id}/workouts")
	@ResponseStatus(HttpStatus.OK)
	public ClientDTO addWorkouts(@PathVariable Integer id, @RequestBody List<Integer> workouts) {
		return Mapper.toDTO(service.addWorkouts(workouts, id));
	}

	@DeleteMapping("/{id}/workouts")
	@ResponseStatus(HttpStatus.OK)
	public ClientDTO removeWorkouts(@PathVariable Integer id, @RequestBody List<Integer> workouts) {
		return Mapper.toDTO(service.removeWorkouts(workouts, id));
	}

	

}
