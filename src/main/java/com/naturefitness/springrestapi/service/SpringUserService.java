package com.naturefitness.springrestapi.service;

import java.util.List;
import java.util.Optional;

import com.naturefitness.springrestapi.model.SpringUser;

public interface SpringUserService {

    SpringUser create(SpringUser SpringUser);
    Optional<SpringUser> getById(Integer id);
    List<SpringUser> getAll();
    SpringUser replace(SpringUser SpringUser);
    SpringUser update(SpringUser SpringUser);
    void deleteById(Integer id);

}
