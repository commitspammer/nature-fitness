package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.Workout;

public interface WorkoutService {

	Workout create(Workout workout);
	Optional<Workout> getById(Integer id);
	List<Workout> getAll();
	Workout replace(Workout workout);
	Workout update(Workout workout);
	
	//NOT FUNCTIONAL YET
	Workout addItems(List<Integer> itemIds, Integer workoutId);
	Workout removeItems(List<Integer> itemIds, Integer workoutId);

	void deleteById(Integer id);

}
