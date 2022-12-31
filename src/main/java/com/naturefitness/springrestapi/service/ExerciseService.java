package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.Exercise;

public interface ExerciseService {

	Exercise create(Exercise exercise);
	Optional<Exercise> getById(Integer id);
	List<Exercise> getAll();
	Exercise replace(Exercise exercise);
	Exercise update(Exercise exercise);
	void deleteById(Integer id);

}
