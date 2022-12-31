package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.PersonalData;

public interface PersonalDataService {

	PersonalData create(PersonalData personalData);
	Optional<PersonalData> getById(Integer id);
	List<PersonalData> getAll();
	PersonalData replace(PersonalData personalData);
	PersonalData update(PersonalData personalData);
	void deleteById(Integer id);

}
