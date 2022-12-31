package com.naturefitness.springrestapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturefitness.springrestapi.model.SpringUser;

public interface SpringUserRepository extends JpaRepository<SpringUser,Integer>{

    Optional<SpringUser> findByLogin(String login);

}