package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.PersonalData;

public interface PersonalDataRepository extends JpaRepository<PersonalData,Integer>{
    
}
