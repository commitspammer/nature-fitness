package com.naturefitness.springrestapi.rest.mappers;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.LinkedList;

import org.hibernate.loader.ColumnEntityAliases;

import com.naturefitness.springrestapi.model.Admin;
import com.naturefitness.springrestapi.model.Client;
import com.naturefitness.springrestapi.model.Exercise;
import com.naturefitness.springrestapi.model.PersonalData;
import com.naturefitness.springrestapi.model.SpringUser;
import com.naturefitness.springrestapi.model.Trainer;
import com.naturefitness.springrestapi.model.Workout;
import com.naturefitness.springrestapi.model.WorkoutItem;
import com.naturefitness.springrestapi.rest.dto.AdminCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.AdminCreationDTO;
import com.naturefitness.springrestapi.rest.dto.AdminDTO;
import com.naturefitness.springrestapi.rest.dto.ClientCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.ClientCreationDTO;
import com.naturefitness.springrestapi.rest.dto.ClientDTO;
import com.naturefitness.springrestapi.rest.dto.ExerciseCreationDTO;
import com.naturefitness.springrestapi.rest.dto.ExerciseDTO;
import com.naturefitness.springrestapi.rest.dto.PersonalDataDTO;
import com.naturefitness.springrestapi.rest.dto.TrainerCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.TrainerCreationDTO;
import com.naturefitness.springrestapi.rest.dto.TrainerDTO;
import com.naturefitness.springrestapi.rest.dto.UserCreationDTO;
import com.naturefitness.springrestapi.rest.dto.UserDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutCreationDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemCreationDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemCompleteDTO;
import com.naturefitness.springrestapi.rest.dto.WorkoutItemDTO;

public class Mapper {

	/* Exercise */
	
	public static Exercise toExercise(ExerciseDTO dto) {
		return Exercise.builder()
			.id(dto.getId())
			.name(dto.getName())
			.description(dto.getDescription())
			.build();
	}

	public static Exercise toExercise(ExerciseCreationDTO dto) {
		return Exercise.builder()
			.name(dto.getName())
			.description(dto.getDescription())
			.build();
	}

	public static ExerciseDTO toDTO(Exercise e) {
		return ExerciseDTO.builder()
			.id(e.getId())
			.name(e.getName())
			.description(e.getDescription())
			.build();
	}

	/* Workout */

	public static Workout toWorkout(WorkoutDTO dto) {
		return Workout.builder()
			.id(dto.getId())
			.title(dto.getTitle())
			.date(dto.getDate())
			.build();
	}

	public static Workout toWorkout(WorkoutCreationDTO dto) {
		return Workout.builder()
			.title(dto.getTitle())
			.date(dto.getDate())
			.build();
	}

	public static WorkoutDTO toDTO(Workout e) {
		return WorkoutDTO.builder()
			.id(e.getId())
			.title(e.getTitle())
			.date(e.getDate())
			.items(e.getItems()
				.stream()
				.map(t -> t.getId())
				.collect(Collectors.toList())
			)
			.build();
	}

	public static WorkoutCompleteDTO toCompleteDTO(Workout e) {
		return WorkoutCompleteDTO.builder()
			.id(e.getId())
			.title(e.getTitle())
			.date(e.getDate())
			.items(e.getItems()
				.stream()
				.map(t -> toCompleteDTO(t))
				.collect(Collectors.toList())
			)
			.build();
	}

	/* WorkoutItem */

	public static WorkoutItem toWorkoutItem(WorkoutItemCreationDTO dto, Function<Integer,Exercise> idToExercise) {
		return WorkoutItem.builder()
			.duration(dto.getDuration())
            .reps(dto.getReps())
            .priority(dto.getPriority())
            .exercise(idToExercise.apply(dto.getExerciseId()))
			.build();
	}
	
	public static WorkoutItem toWorkoutItem(WorkoutItemDTO dto, Function<Integer,Exercise> idToExercise) {
		return WorkoutItem.builder()
			.id(dto.getId())
			.duration(dto.getDuration())
            .reps(dto.getReps())
            .priority(dto.getPriority())
            .exercise(idToExercise.apply(dto.getExerciseId()))
			.build();
	}

	public static WorkoutItemCompleteDTO toCompleteDTO(WorkoutItem item) { 
		return WorkoutItemCompleteDTO.builder()
			.id(item.getId())
			.duration(item.getDuration())
            .reps(item.getReps())
            .priority(item.getPriority())
            .exercise(toDTO(item.getExercise()))
			.build();
	}

	/* PersonalData */

	public static PersonalDataDTO toDTO(PersonalData d) {
		return PersonalDataDTO.builder()
			.name(d.getName())
			.email(d.getEmail())
			.number(d.getNumber())
			.build();
	}

	public static PersonalData toPersonalData(PersonalDataDTO dto) {
		return PersonalData.builder()
			.name(dto.getName())
			.email(dto.getEmail())
			.number(dto.getNumber())
			.build();
	}

	/* Client */

	public static Client toClient(ClientCreationDTO dto) {
		return Client.builder()
			.data(PersonalData.builder()
				.name(dto.getData().getName())
				.email(dto.getData().getEmail())
				.number(dto.getData().getNumber())
				.build()
			)
			.build();
	}

	public static ClientDTO toDTO(Client c) {
		return ClientDTO.builder()
			.id(c.getId())
			.data(Mapper.toDTO(c.getData()))
			.trainers(c.getTrainers()
				.stream()
				.map(t -> t.getId())
				.collect(Collectors.toList())
			)
			.workouts(c.getWorkouts()
				.stream()
				.map(w -> w.getId())
				.collect(Collectors.toList())
			)
			.build();
	}

	public static ClientCompleteDTO toCompleteDTO(Client c) { 
		return ClientCompleteDTO.builder()
			.id(c.getId())
			.data(Mapper.toDTO(c.getData()))
			.trainers(c.getTrainers()
				.stream()
				.map(t -> toDTO(t))
				.collect(Collectors.toList())
			)
			.workouts(c.getWorkouts()
				.stream()
				.map(w -> toCompleteDTO(w))
				.collect(Collectors.toList())
			)
			.build();
	}

	/* Trainer */

	public static Trainer toTrainer(TrainerCreationDTO dto) {
		return Trainer.builder()
			.data(PersonalData.builder()
				.name(dto.getData().getName())
				.email(dto.getData().getEmail())
				.number(dto.getData().getNumber())
				.build()
			)
			.build();
	}

	public static TrainerDTO toDTO(Trainer t) {
		return TrainerDTO.builder()
			.id(t.getId())
			.data(Mapper.toDTO(t.getData()))
			.clients(t.getClients()
				.stream()
				.map(c -> c.getId())
				.collect(Collectors.toList())
				)	
			.build();
	}

	public static TrainerCompleteDTO toCompleteDTO(Trainer t) { 
		return TrainerCompleteDTO.builder()
			.id(t.getId())
			.data(Mapper.toDTO(t.getData()))
			.clients(t.getClients()
				.stream()
				.map(c -> toDTO(c))
				.collect(Collectors.toList())
			)	
			.build();
	}

	/* Admin */

	public static Admin toAdmin(AdminCreationDTO dto) {
		return Admin.builder()
			.data(PersonalData.builder()
				.name(dto.getData().getName())
				.email(dto.getData().getEmail())
				.number(dto.getData().getNumber())
				.build()
			)
			.build();
	}

	public static AdminDTO toDTO(Admin a) {
		return AdminDTO.builder()
			.id(a.getId())
			.data(Mapper.toDTO(a.getData()))
			.build();
	}

	public static AdminCompleteDTO toCompleteDTO(Admin a) { 
		return AdminCompleteDTO.builder()
			.id(a.getId())
			.data(Mapper.toDTO(a.getData()))
			.build();
	}

	/* User */

	public static SpringUser toUser(UserCreationDTO dto) {
		return SpringUser.builder()
			.login(dto.getLogin())
			.password(dto.getPassword())
			.client(dto.getClient())
			.trainer(dto.getTrainer())
			.admin(dto.getAdmin())
			.build();
	}

	public static Client toClient(UserCreationDTO dto) {
		return Client.builder()
			.data(Mapper.toPersonalData(dto.getData()))
			.build();
	}

	public static Trainer toTrainer(UserCreationDTO dto) {
		return Trainer.builder()
			.data(Mapper.toPersonalData(dto.getData()))
			.build();
	}

	public static Admin toAdmin(UserCreationDTO dto) {
		return Admin.builder()
			.data(Mapper.toPersonalData(dto.getData()))
			.build();
	}

	public static UserDTO toDTO(SpringUser u) {
		List<String> roles = new LinkedList<>();
		if (u.getClient()) roles.add("CLIENT");
		if (u.getTrainer()) roles.add("TRAINER");
		if (u.getAdmin()) roles.add("ADMIN");
		return UserDTO.builder()
			.login(u.getLogin())
			.roles(roles)
			.roleEntity(u.getRoleEntity())
			.build();
	}

}
