package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.Admin;
import com.naturefitness.springrestapi.model.PersonalData;

public interface AdminService {

	Admin create(Admin admin);
	Optional<Admin> getById(Integer id);
	List<Admin> getAll();
	Admin replace(Admin admin);
	Admin updateData(PersonalData data, Integer adminId);
	void deleteById(Integer id);

}
