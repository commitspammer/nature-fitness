package com.naturefitness.springrestapi.rest.controllers;

import com.naturefitness.springrestapi.model.WorkoutItem;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemCreationDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemDTO;
import com.naturefitness.springrestapi.service.ExerciseService;
import com.naturefitness.springrestapi.service.WorkoutItemService;
import com.naturefitness.springrestapi.rest.mappers.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

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

@RestController
@RequestMapping("/api/workoutitems")
public class WorkoutItemController {
    @Autowired
    private WorkoutItemService service;
    
    @Autowired
    private ExerciseService exerciseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@RequestBody WorkoutItemCreationDTO dto) {
        return service.create(Mapper.toWorkoutItem(dto, (i) -> exerciseService.getById(i).orElseThrow(() -> new EntityNotFoundException("Exercise not found!")))).getId();
    }

    @GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<WorkoutItemCompleteDTO> findAll() {
		return service.getAll()
			.stream()
			.map(Mapper::toCompleteDTO)
			.collect(Collectors.toList());
	}

    @GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public WorkoutItemCompleteDTO findById(@PathVariable Integer id) {
		return service
            .getById(id)
			.map(Mapper::toCompleteDTO)
			.orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
	}

    @PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer replace(@PathVariable Integer id, @RequestBody WorkoutItemDTO dto) {
		dto.setId(id);
		return service.replace(Mapper.toWorkoutItem(dto, (i) -> exerciseService.getById(i).orElseThrow(() -> new EntityNotFoundException("Exercise not found!")))).getId();
	}

    @PatchMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Integer update(@PathVariable Integer id, @RequestBody WorkoutItemDTO dto) {
		dto.setId(id);
		return service.update(Mapper.toWorkoutItem(dto, (i) -> exerciseService.getById(i).orElseThrow(() -> new EntityNotFoundException("Exercise not found!")))).getId();
	}

    @DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Integer id) {
		service.deleteById(id);
	}
}
