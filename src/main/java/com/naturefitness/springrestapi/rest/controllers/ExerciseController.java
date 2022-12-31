package com.naturefitness.springrestapi.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.model.Exercise;
import com.naturefitness.springrestapi.rest.dto.ExerciseCreationDTO;
import com.naturefitness.springrestapi.rest.dto.ExerciseDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.ExerciseService;

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
@RequestMapping("/api/exercises")
public class ExerciseController {

	@Autowired
	private ExerciseService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer create(@RequestBody ExerciseCreationDTO dto) {
		return service.create(Mapper.toExercise(dto)).getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ExerciseDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toDTO)
			.collect(Collectors.toList());
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ExerciseDTO findById(@PathVariable Integer id) {
		return service.getById(id)
			.map(Mapper::toDTO)
			.orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer replace(@PathVariable Integer id, @RequestBody ExerciseDTO dto) {
		dto.setId(id);
		return service.replace(Mapper.toExercise(dto)).getId();
	}

	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer update(@PathVariable Integer id, @RequestBody ExerciseDTO dto) {
		dto.setId(id);
		return service.update(Mapper.toExercise(dto)).getId();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
