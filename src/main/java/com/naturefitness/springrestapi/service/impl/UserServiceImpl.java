package com.naturefitness.springrestapi.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.naturefitness.springrestapi.exception.InvalidPasswordException;
import com.naturefitness.springrestapi.model.SpringUser;
import com.naturefitness.springrestapi.repository.SpringUserRepository;

/*
 * interface do spring security serve para definir o carregam, UserDetailsento de usuários através de uma base de dados
 */
@Component
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private SpringUserRepository repository;

    @Transactional
    public SpringUser salvar(SpringUser usuario) {
        return repository.save(usuario);
    }

    public UserDetails autenticar( SpringUser usuario ){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = passwordEncoder.matches( usuario.getPassword(), user.getPassword() );

        if(senhasBatem){
            return user;
        }

        throw new InvalidPasswordException("Erro");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SpringUser usuario = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));

        String role;
        if (usuario.getClient() == true) {
            role = "CLIENT";
        } else if(usuario.getTrainer() == true) {
            role = "TRAINER";
        } else if (usuario.getAdmin() == true) {
            role = "ADMIN";
        } else {
			throw new RuntimeException("Couldn't get user role");
		}
        //String[] roles = usuario.getAdmin() ? new String[] { "ADMIN", "USER" } : new String[] { "USER" };

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .roles(role)
                .build();
    }

}
