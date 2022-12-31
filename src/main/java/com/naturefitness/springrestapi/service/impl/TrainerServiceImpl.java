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
import com.naturefitness.springrestapi.repository.ClientRepository;
import com.naturefitness.springrestapi.repository.PersonalDataRepository;
import com.naturefitness.springrestapi.repository.TrainerRepository;
import com.naturefitness.springrestapi.service.TrainerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository repository;

	@Autowired
	private PersonalDataRepository dataRepository;

	@Autowired
	private ClientRepository clientRepository;

	private Trainer validateAndSave(Trainer trainer) {
		if (trainer.getData() == null)
			throw new NullFieldException("Trainer personal data is null");
		if (trainer.getData().getName() == null)
			throw new NullFieldException("Trainer name is null");
		if (trainer.getData().getEmail() == null)
			throw new NullFieldException("Trainer email is null");
		if (trainer.getClients() == null)
			trainer.setClients(List.of());
		if (repository.findAll()
			.stream()
			.filter(t -> t.getId() != trainer.getId())
			.anyMatch(t -> t.getData().getEmail().equals(trainer.getData().getEmail()))
			|| clientRepository.findAll()
			.stream()
			.filter(t -> t.getId() != trainer.getId())
			.anyMatch(t -> t.getData().getEmail().equals(trainer.getData().getEmail()))
		)
			throw new NonUniqueFieldException("Trainer email is already used");
		if (trainer.getClients().size() > trainer.getClients().stream().distinct().count())
			throw new NonUniqueFieldException("Trainer may only link to a client once");
		dataRepository.save(trainer.getData());
		return repository.save(trainer);
	}

	@Override
	public Trainer create(Trainer trainer) {
		if (trainer.getId() == null)
			return validateAndSave(trainer);
		if (repository.existsById(trainer.getId())) {
			throw new EntityAlreadyExistsException("Trainer already exists");
		} else {
			return validateAndSave(trainer);
		}
	}

	@Override
	public Optional<Trainer> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Trainer> getAll() {
		return repository.findAll();
	}

	@Override
	public Trainer replace(Trainer trainer) {
		if (trainer.getId() == null)
			throw new NullFieldException("Trainer ID is null");
		if (repository.existsById(trainer.getId())) {
			return validateAndSave(trainer);
		} else {
			throw new EntityNotFoundException("Trainer doesn't exist");
		}
	}

	@Override
	public Trainer updateData(PersonalData data, Integer trainerId) {
		return repository.findById(trainerId)
			.map(t -> {
				if (data.getName() != null) t.getData().setName(data.getName());
				if (data.getEmail() != null) t.getData().setEmail(data.getEmail());
				if (data.getNumber() != null) t.getData().setNumber(data.getNumber());
				return validateAndSave(t);
			})
			.orElseThrow(() -> new EntityNotFoundException("Trainer doesn't exist"));
	}

	@Override
	public Trainer addClients(List<Integer> clientIds, Integer trainerId) {
		clientIds.forEach(c -> {
			if (!clientRepository.existsById(c))
				throw new EntityNotFoundException("Client " + c + " doesn't exist");
		});
		return repository.findById(trainerId)
			.map(t -> {
				t.getClients().addAll(clientIds.stream()
					.map(clientRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(t);
			})
			.orElseThrow(() -> new EntityNotFoundException("Trainer doesn't exist"));
	}

	@Override
	public Trainer removeClients(List<Integer> clientIds, Integer trainerId) {
		return repository.findById(trainerId)
			.map(t -> {
				t.getClients().removeAll(clientIds.stream()
					.map(clientRepository::findById)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList())
				);
				return validateAndSave(t);
			})
			.orElseThrow(() -> new EntityNotFoundException("Trainer doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.findById(id)
			.ifPresent(t -> dataRepository.deleteById(t.getData().getId()));
		repository.deleteById(id);
	}

}
