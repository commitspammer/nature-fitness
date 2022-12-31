package com.naturefitness.springrestapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.Client;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.model.Trainer;
import com.naturefitness.springrestapi.model.Workout;
import com.naturefitness.springrestapi.repository.ClientRepository;
import com.naturefitness.springrestapi.repository.PersonalDataRepository;
import com.naturefitness.springrestapi.repository.TrainerRepository;
import com.naturefitness.springrestapi.repository.WorkoutRepository;
import com.naturefitness.springrestapi.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository repository;

	@Autowired
	private PersonalDataRepository dataRepository;

	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private WorkoutRepository workoutRepository;

	private Client validateAndSave(Client client) {
		if (client.getData() == null)
			throw new NullFieldException("Client personal data is null");
		if (client.getData().getName() == null)
			throw new NullFieldException("Client name is null");
		if (client.getData().getEmail() == null)
			throw new NullFieldException("Client email is null");
		if (client.getTrainers() == null)
			client.setTrainers(List.of());
		if (client.getWorkouts() == null)
			client.setWorkouts(List.of());
		if (repository.findAll()
			.stream()
			.filter(c -> c.getId() != client.getId())
			.anyMatch(c -> c.getData().getEmail().equals(client.getData().getEmail()))
			|| trainerRepository.findAll()
			.stream()
			.filter(c -> c.getId() != client.getId())
			.anyMatch(c -> c.getData().getEmail().equals(client.getData().getEmail()))
		)
			throw new NonUniqueFieldException("Client email is already used");
		if (client.getTrainers().size() > client.getTrainers().stream().distinct().count())
			throw new NonUniqueFieldException("Client may only link to a trainer once");
		dataRepository.save(client.getData());
		return repository.save(client);
	}

	@Override
	public Client create(Client client) {
		if (client.getId() == null)
			return validateAndSave(client);
		if (repository.existsById(client.getId())) {
			throw new EntityAlreadyExistsException("Client already exists");
		} else {
			return validateAndSave(client);
		}
	}

	@Override
	public Optional<Client> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Client> getAll() {
		return repository.findAll();
	}

	@Override
	public Client replace(Client client) {
		if (client.getId() == null)
			throw new NullFieldException("Client ID is null");
		if (repository.existsById(client.getId())) {
			return validateAndSave(client);
		} else {
			throw new EntityNotFoundException("Client doesn't exist");
		}
	}

	@Override
	public Client updateData(PersonalData data, Integer clientId) {
		return repository.findById(clientId)
			.map(c -> {
				if (data.getName() != null) c.getData().setName(data.getName());
				if (data.getEmail() != null) c.getData().setEmail(data.getEmail());
				if (data.getNumber() != null) c.getData().setNumber(data.getNumber());
				return validateAndSave(c);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public Client addTrainers(List<Integer> trainerIds, Integer clientId) {
		trainerIds.forEach(t -> {
			if (!trainerRepository.existsById(t))
				throw new EntityNotFoundException("Trainer " + t + " doesn't exist");
		});
		return repository.findById(clientId)
			.map(c -> {
				c.getTrainers().addAll(trainerIds.stream()
					.map(trainerRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(c);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public Client removeTrainers(List<Integer> trainerIds, Integer clientId) {
		return repository.findById(clientId)
			.map(c -> {
				c.getTrainers().removeAll(trainerIds.stream()
					.map(trainerRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(c);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public Client addWorkouts(List<Integer> workoutIds, Integer clientId) {
		workoutIds.forEach(w -> {
			if (!workoutRepository.existsById(w))
				throw new EntityNotFoundException("Workout " + w + " doesn't exist");
		});
		return repository.findById(clientId)
			.map(c -> {
				c.getWorkouts().addAll(workoutIds.stream()
					.map(workoutRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(c);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public Client removeWorkouts(List<Integer> workoutIds, Integer clientId) {
		return repository.findById(clientId)
			.map(c -> {
				c.getWorkouts().removeAll(workoutIds.stream()
					.map(workoutRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(c);
			})
			.orElseThrow(() -> new EntityNotFoundException("Client doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.findById(id)
			.ifPresent(c -> dataRepository.deleteById(c.getData().getId()));
		repository.deleteById(id);
	}

}
