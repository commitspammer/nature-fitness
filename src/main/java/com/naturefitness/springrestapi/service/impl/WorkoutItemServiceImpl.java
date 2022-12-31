package com.naturefitness.springrestapi.service.impl;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.WorkoutItem;
import com.naturefitness.springrestapi.repository.WorkoutItemRepository;
import com.naturefitness.springrestapi.service.WorkoutItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkoutItemServiceImpl implements WorkoutItemService {

	@Autowired
	private WorkoutItemRepository repository;

	private WorkoutItem validateAndSave(WorkoutItem workoutItem) { //TODO ADD MORE VALIDATION
		if (workoutItem.getDuration() == null && workoutItem.getReps() == null)
			throw new NullFieldException("WorkoutItem duration and reps are both null");
		if (workoutItem.getDuration() != null && workoutItem.getReps() != null)
			throw new NullFieldException("WorkoutItem duration and reps can't both be non-null"); //FIXME I SHOULDNT BE NULLFIELDEXCEPTION
		/*
		if (workoutItem.getDuration() == null)
			throw new NullFieldException("WorkoutItem duration is null");
		if (workoutItem.getReps() == null)
			throw new NullFieldException("WorkoutItem reps is null");
		if (repository.findAll()
			.stream()
			.filter(e -> e.getId() != workoutItem.getId())
			.anyMatch(e -> e.getDuration().equals(workoutItem.getDuration()))
		)
			throw new NonUniqueFieldException("WorkoutItem duration is already used");
			*/
		return repository.save(workoutItem);
	}

	@Override
	public WorkoutItem create(WorkoutItem workoutItem) {
		if (workoutItem.getId() == null)
			return validateAndSave(workoutItem);
		if (repository.existsById(workoutItem.getId())) {
			throw new EntityAlreadyExistsException("WorkoutItem already exists");
		} else {
			return validateAndSave(workoutItem);
		}
	}

	@Override
	public Optional<WorkoutItem> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<WorkoutItem> getAll() {
		return repository.findAll();
	}

	@Override
	public WorkoutItem replace(WorkoutItem workoutItem) {
		if (workoutItem.getId() == null)
			throw new NullFieldException("WorkoutItem ID is null");
		if (repository.existsById(workoutItem.getId())) {
			return validateAndSave(workoutItem);
		} else {
			throw new EntityNotFoundException("WorkoutItem doesn't exist");
		}
	}

	@Override
	public WorkoutItem update(WorkoutItem workoutItem) { //TODO ADD REMAINING (PATCHABLE) FIELDS
		if (workoutItem.getId() == null)
			throw new NullFieldException("WorkoutItem ID is null");
		return repository.findById(workoutItem.getId())
			.map(e -> {
				if (workoutItem.getDuration() != null) e.setDuration(workoutItem.getDuration());
				if (workoutItem.getReps() != null) e.setReps(workoutItem.getReps());
				return validateAndSave(e);
			})
			.orElseThrow(() -> new EntityNotFoundException("WorkoutItem doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

}
