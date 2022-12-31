package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.Client;

public interface ClientRepository extends JpaRepository<Client,Integer>{
    
}
