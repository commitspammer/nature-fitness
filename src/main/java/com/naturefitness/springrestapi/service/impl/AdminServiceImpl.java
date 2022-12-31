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
import com.naturefitness.springrestapi.model.Admin;
import com.naturefitness.springrestapi.repository.ClientRepository;
import com.naturefitness.springrestapi.repository.PersonalDataRepository;
import com.naturefitness.springrestapi.repository.AdminRepository;
import com.naturefitness.springrestapi.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository repository;

	@Autowired
	private PersonalDataRepository dataRepository;

	@Autowired
	private ClientRepository clientRepository;

	private Admin validateAndSave(Admin admin) {
		if (admin.getData() == null)
			throw new NullFieldException("Admin personal data is null");
		if (admin.getData().getName() == null)
			throw new NullFieldException("Admin name is null");
		if (admin.getData().getEmail() == null)
			throw new NullFieldException("Admin email is null");
		if (repository.findAll()
			.stream()
			.filter(t -> t.getId() != admin.getId())
			.anyMatch(t -> t.getData().getEmail().equals(admin.getData().getEmail()))
			|| clientRepository.findAll()
			.stream()
			.filter(t -> t.getId() != admin.getId())
			.anyMatch(t -> t.getData().getEmail().equals(admin.getData().getEmail()))
		)
			throw new NonUniqueFieldException("Admin email is already used");
		dataRepository.save(admin.getData());
		return repository.save(admin);
	}

	@Override
	public Admin create(Admin admin) {
		if (admin.getId() == null)
			return validateAndSave(admin);
		if (repository.existsById(admin.getId())) {
			throw new EntityAlreadyExistsException("Admin already exists");
		} else {
			return validateAndSave(admin);
		}
	}

	@Override
	public Optional<Admin> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Admin> getAll() {
		return repository.findAll();
	}

	@Override
	public Admin replace(Admin admin) {
		if (admin.getId() == null)
			throw new NullFieldException("Admin ID is null");
		if (repository.existsById(admin.getId())) {
			return validateAndSave(admin);
		} else {
			throw new EntityNotFoundException("Admin doesn't exist");
		}
	}

	@Override
	public Admin updateData(PersonalData data, Integer adminId) {
		return repository.findById(adminId)
			.map(t -> {
				if (data.getName() != null) t.getData().setName(data.getName());
				if (data.getEmail() != null) t.getData().setEmail(data.getEmail());
				if (data.getNumber() != null) t.getData().setNumber(data.getNumber());
				return validateAndSave(t);
			})
			.orElseThrow(() -> new EntityNotFoundException("Admin doesn't exist"));
	}

	@Override
	public void deleteById(Integer id) {
		repository.findById(id)
			.ifPresent(t -> dataRepository.deleteById(t.getData().getId()));
		repository.deleteById(id);
	}

}
