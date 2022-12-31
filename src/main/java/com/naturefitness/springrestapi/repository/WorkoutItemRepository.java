package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.WorkoutItem;

public interface WorkoutItemRepository extends JpaRepository<WorkoutItem,Integer>{
    
}
