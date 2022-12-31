package com.naturefitness.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.model.Workout;
import com.naturefitness.springrestapi.rest.dto.WorkoutCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutCreationDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.WorkoutService;

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
@RequestMapping("/api/workouts")
public class WorkoutController {

	@Autowired
	private WorkoutService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer create(@RequestBody WorkoutCreationDTO dto) {
		return service.create(Mapper.toWorkout(dto)).getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<WorkoutCompleteDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toCompleteDTO)
			.collect(Collectors.toList());
	}

	// @GetMapping
	// @ResponseStatus(HttpStatus.OK)
	// public List<WorkoutDTO> findAll() {
	// 	return service.getAll()
	// 		.stream()
	// 		.map(Mapper::toDTO)
	// 		.collect(Collectors.toList());
	// }

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public WorkoutDTO findById(@PathVariable Integer id) {
		return service.getById(id)
			.map(Mapper::toDTO)
			.orElseThrow(() -> new EntityNotFoundException("Workout not found"));
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer replace(@PathVariable Integer id, @RequestBody WorkoutDTO dto) {
		dto.setId(id);
		return service.replace(Mapper.toWorkout(dto)).getId();
	}

	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer update(@PathVariable Integer id, @RequestBody WorkoutDTO dto) {
		dto.setId(id);
		return service.update(Mapper.toWorkout(dto)).getId();
	}

	//NOT FUNCTIONAL YET
	@PostMapping("/{id}/items")
	@ResponseStatus(HttpStatus.OK)
	public WorkoutDTO addTrainers(@PathVariable Integer id, @RequestBody List<Integer> items) {
		System.out.println(items);
		return Mapper.toDTO(service.addItems(items, id));
	}

	@DeleteMapping("/{id}/items")
	@ResponseStatus(HttpStatus.OK)
	public WorkoutDTO removeTrainers(@PathVariable Integer id, @RequestBody List<Integer> items) {
		return Mapper.toDTO(service.removeItems(items, id));
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
