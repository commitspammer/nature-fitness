package com.naturefitness.springrestapi.service.impl;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.Exercise;
import com.naturefitness.springrestapi.repository.ExerciseRepository;
import com.naturefitness.springrestapi.service.ExerciseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {

	@Autowired
	private ExerciseRepository repository;

	private Exercise validateAndSave(Exercise exercise) {
		if (exercise.getName() == null)
			throw new NullFieldException("Exercise name is null");
		if (exercise.getDescription() == null)
			throw new NullFieldException("Exercise description is null");
		if (repository.findAll()
			.stream()
			.filter(e -> e.getId() != exercise.getId())
			.anyMatch(e -> e.getName().equals(exercise.getName()))
		)
			throw new NonUniqueFieldException("Exercise name is already used");
		return repository.save(exercise);
	}

	@Override
	public Exercise create(Exercise exercise) {
		if (exercise.getId() == null)
			return validateAndSave(exercise);
		if (repository.existsById(exercise.getId())) {
			throw new EntityAlreadyExistsException("Exercise already exists");
		} else {
			return validateAndSave(exercise);
		}
	}

	@Override
	public Optional<Exercise> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Exercise> getAll() {
		return repository.findAll();
	}

	@Override
	public Exercise replace(Exercise exercise) {
		if (exercise.getId() == null)
			throw new NullFieldException("Exercise ID is null");
		if (repository.existsById(exercise.getId())) {
			return validateAndSave(exercise);
		} else {
			throw new EntityNotFoundException("Exercise doesn't exist");
		}
	}

	@Override
	public Exercise update(Exercise exercise) {
		if (exercise.getId() == null)
			throw new NullFieldException("Exercise ID is null");
		return repository.findById(exercise.getId())
			.map(e -> {
				if (exercise.getName() != null) e.setName(exercise.getName());
				if (exercise.getDescription() != null) e.setDescription(exercise.getDescription());
				return validateAndSave(e);
			})
			.orElseThrow(() -> new EntityNotFoundException("Exercise doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

}
