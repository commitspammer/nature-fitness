package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.WorkoutItem;

public interface WorkoutItemService{

	WorkoutItem create(WorkoutItem item);
	Optional<WorkoutItem> getById(Integer id);
	List<WorkoutItem> getAll();
	WorkoutItem replace(WorkoutItem item);
	WorkoutItem update(WorkoutItem item);
	void deleteById(Integer id);

}
