package com.naturefitness.springrestapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.Workout;
import com.naturefitness.springrestapi.repository.WorkoutItemRepository;
import com.naturefitness.springrestapi.repository.WorkoutRepository;
import com.naturefitness.springrestapi.service.WorkoutService;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkoutServiceImpl implements WorkoutService {

	@Autowired
	private WorkoutRepository repository;

	@Autowired
	private WorkoutItemRepository itemRepository;

	private Workout validateAndSave(Workout workout) {
		if (workout.getTitle() == null)
			throw new NullFieldException("Workout title is null");
		if (workout.getDate() == null)
			throw new NullFieldException("Workout date is null");
		if (repository.findAll()
			.stream()
			.filter(e -> e.getId() != workout.getId())
			.anyMatch(e -> e.getTitle().equals(workout.getTitle()))
		)
			throw new NonUniqueFieldException("Workout title is already used");
		return repository.save(workout);
	}

	@Override
	public Workout create(Workout workout) {
		if (workout.getId() == null)
			return validateAndSave(workout);
		if (repository.existsById(workout.getId())) {
			throw new EntityAlreadyExistsException("Workout already exists");
		} else {
			return validateAndSave(workout);
		}
	}

	@Override
	public Optional<Workout> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Workout> getAll() {
		return repository.findAll();
	}

	@Override
	public Workout replace(Workout workout) {
		if (workout.getId() == null)
			throw new NullFieldException("Workout ID is null");
		if (repository.existsById(workout.getId())) {
			return validateAndSave(workout);
		} else {
			throw new EntityNotFoundException("Workout doesn't exist");
		}
	}

	@Override
	public Workout update(Workout workout) {
		if (workout.getId() == null)
			throw new NullFieldException("Workout ID is null");
		return repository.findById(workout.getId())
			.map(e -> {
				if (workout.getTitle() != null) e.setTitle(workout.getTitle());
				if (workout.getDate() != null) e.setDate(workout.getDate());
				return validateAndSave(e);
			})
			.orElseThrow(() -> new EntityNotFoundException("Workout doesn't exist"));
	}

	@Override
	public Workout addItems(List<Integer> itemIds, Integer workoutId) {
		itemIds.forEach(i -> {
			if (!itemRepository.existsById(i))
			throw new EntityNotFoundException("WorkoutItem " + i + " doesn't exist");
		});

		return repository.findById(workoutId)
			.map(w -> {
				w.getItems().addAll(itemIds.stream()
					.map(itemRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(w);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public Workout removeItems(List<Integer> itemIds, Integer workoutId) {
		return repository.findById(workoutId)
			.map(w -> { w.getItems()
				.removeAll(itemIds.stream()
				.map(itemRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList())
			);
				return validateAndSave(w);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}


	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

}
