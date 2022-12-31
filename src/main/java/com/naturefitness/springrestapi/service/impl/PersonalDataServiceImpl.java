package com.naturefitness.springrestapi.service.impl;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.repository.PersonalDataRepository;
import com.naturefitness.springrestapi.service.PersonalDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalDataServiceImpl implements PersonalDataService {

	@Autowired
	private PersonalDataRepository repository;

	private PersonalData validateAndSave(PersonalData personalData) {
		if (personalData.getName() == null)
			throw new NullFieldException("PersonalData name is null");
		if (personalData.getEmail() == null)
			throw new NullFieldException("PersonalData email is null");
		if (personalData.getNumber() == null)
			throw new NullFieldException("PersonalData number is null");
		if (repository.findAll()
			.stream()
			.filter(e -> e.getId() != personalData.getId())
			.anyMatch(e -> e.getEmail().equals(personalData.getEmail()))
		)
			throw new NonUniqueFieldException("PersonalData email is already used");
		return repository.save(personalData);
	}

	@Override
	public PersonalData create(PersonalData personalData) {
		if (personalData.getId() == null)
			return validateAndSave(personalData);
		if (repository.existsById(personalData.getId())) {
			throw new EntityAlreadyExistsException("PersonalData already exists");
		} else {
			return validateAndSave(personalData);
		}
	}

	@Override
	public Optional<PersonalData> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<PersonalData> getAll() {
		return repository.findAll();
	}

	@Override
	public PersonalData replace(PersonalData personalData) {
		if (personalData.getId() == null)
			throw new NullFieldException("PersonalData ID is null");
		if (repository.existsById(personalData.getId())) {
			return validateAndSave(personalData);
		} else {
			throw new EntityNotFoundException("PersonalData doesn't exist");
		}
	}

	@Override
	public PersonalData update(PersonalData personalData) {
		if (personalData.getId() == null)
			throw new NullFieldException("PersonalData ID is null");
		return repository.findById(personalData.getId())
			.map(e -> {
				if (personalData.getName() != null) e.setName(personalData.getName());
				if (personalData.getEmail() != null) e.setEmail(personalData.getEmail());
				if (personalData.getNumber() != null) e.setEmail(personalData.getNumber());
				return validateAndSave(e);
			})
			.orElseThrow(() -> new EntityNotFoundException("PersonalData doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

}
