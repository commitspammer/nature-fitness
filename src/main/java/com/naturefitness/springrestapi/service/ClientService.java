package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.Client;
import com.naturefitness.springrestapi.model.PersonalData;

public interface ClientService {

	Client create(Client client);
	Optional<Client> getById(Integer id);
	List<Client> getAll();
	Client replace(Client client);
	Client updateData(PersonalData data, Integer clientId);
	Client addTrainers(List<Integer> trainerIds, Integer clientId);
	Client removeTrainers(List<Integer> trainerIds, Integer clientId);
	Client addWorkouts(List<Integer> workoutIds, Integer clientId);
	Client removeWorkouts(List<Integer> workoutIds, Integer clientId);
	void deleteById(Integer id);

}
