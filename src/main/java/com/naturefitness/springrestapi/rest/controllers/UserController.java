package com.naturefitness.springrestapi.rest.controllers;

//import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.naturefitness.springrestapi.exception.InvalidPasswordException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.model.SpringUser;
import com.naturefitness.springrestapi.repository.SpringUserRepository;
import com.naturefitness.springrestapi.rest.dto.CredentialsDTO;
import com.naturefitness.springrestapi.rest.dto.UserCreationDTO;
import com.naturefitness.springrestapi.rest.dto.UserDTO;
import com.naturefitness.springrestapi.rest.dto.TokenDTO;
import com.naturefitness.springrestapi.rest.mappers.Mapper;
import com.naturefitness.springrestapi.service.impl.UserServiceImpl;
import com.naturefitness.springrestapi.service.ClientService;
import com.naturefitness.springrestapi.service.TrainerService;
import com.naturefitness.springrestapi.service.AdminService;
import com.naturefitness.springrestapi.security.JwtService;

@RestController
@RequestMapping("/api/users")
//@RequiredArgsConstructor
public class UserController {

	@Autowired
    private ClientService clientService;

	@Autowired
    private TrainerService trainerService;

	@Autowired
    private AdminService adminService;

	@Autowired
    private UserServiceImpl service;

	@Autowired
    private SpringUserRepository repository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
    private JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpringUser salvar(@RequestBody UserCreationDTO dto) {
		SpringUser user = Mapper.toUser(dto);
		int roleEntity;
		if (user.getClient())
			roleEntity = clientService.create(Mapper.toClient(dto)).getId();
		else if (user.getTrainer())
			roleEntity = trainerService.create(Mapper.toTrainer(dto)).getId();
		else if (user.getAdmin())
			roleEntity = adminService.create(Mapper.toAdmin(dto)).getId();
		else throw new NullFieldException("User has no role");
		user.setRoleEntity(roleEntity);

        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        return service.salvar(user);
    }

	@PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenDTO getToken(@RequestBody CredentialsDTO credentials){
        try{
            SpringUser user = SpringUser.builder()
                    .login(credentials.getLogin())
                    .password(credentials.getPassword()).build();
            UserDetails authUser = service.autenticar(user);
            String token = jwtService.gerarToken(user);
            return TokenDTO.builder()
				.login(user.getLogin())
				.token(token)
				.build();
        } catch (UsernameNotFoundException | InvalidPasswordException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

	@GetMapping("/{token}")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO /*Map<String, Object>*/ getInfo(@PathVariable String token) {
		String login = jwtService.obterLoginUsuario(token);
		//SpringUser user = repository.findByLogin(login);
		return Mapper.toDTO(repository.findByLogin(login).orElseThrow());
		//UserDetails user = service.loadUserByUsername(login);
		//Map<String, Object> map = new HashMap<>();
		//map.put("login", user.getUsername());
		//map.put("roles", user.getAuthorities().stream().map(a -> a.getAuthority().substring(5)).collect(Collectors.toList()));
		//return map;
	}

}
