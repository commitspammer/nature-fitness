package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise,Integer>{
    
}
