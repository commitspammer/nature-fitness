package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.Workout;

public interface WorkoutRepository extends JpaRepository<Workout,Integer>{
    
}
