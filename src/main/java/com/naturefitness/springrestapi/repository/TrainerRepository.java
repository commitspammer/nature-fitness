package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer,Integer>{
    
}
