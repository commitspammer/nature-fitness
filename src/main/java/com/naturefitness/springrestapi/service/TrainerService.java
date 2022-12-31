package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.Trainer;
import com.naturefitness.springrestapi.model.PersonalData;

public interface TrainerService {

	Trainer create(Trainer trainer);
	Optional<Trainer> getById(Integer id);
	List<Trainer> getAll();
	Trainer replace(Trainer trainer);
	Trainer updateData(PersonalData data, Integer trainerId);
	Trainer addClients(List<Integer> clientIds, Integer trainerId);
	Trainer removeClients(List<Integer> clientIds, Integer trainerId);
	void deleteById(Integer id);

}
