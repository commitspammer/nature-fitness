package com.naturefitness.springrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{
    
}
