package com.naturefitness.springrestapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naturefitness.springrestapi.security.JwtAuthFilter;
import com.naturefitness.springrestapi.security.JwtService;
import com.naturefitness.springrestapi.service.impl.UserServiceImpl;

@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private JwtService jwtService;

    @Autowired
    private UserServiceImpl usuarioService;

 	@Autowired
 	private PasswordEncoder passwordEncoder;

 	@Bean
 	public OncePerRequestFilter jwtFilter(){
     	return new JwtAuthFilter(jwtService, usuarioService);
 	}
 
 	@Bean
 	public InMemoryUserDetailsManager user(){
     	return new InMemoryUserDetailsManager(
         	User.withUsername("admin")
             	.password(passwordEncoder.encode("admin"))
             	.authorities("read", "write")
             	.roles("ADMIN")
             	.build()
     	);
 	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
			    .antMatchers(HttpMethod.POST,   "/api/users/").permitAll()
			    .antMatchers(HttpMethod.POST,   "/api/users/token").permitAll()
			    .antMatchers(HttpMethod.POST,   "/api/users/{token}").permitAll()
			    //.antMatchers(                   "/api/users/**").hasAnyRole("ADMIN")

			    .antMatchers(HttpMethod.POST,   "/api/trainers/").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.GET,    "/api/trainers/").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.GET,    "/api/trainers/{id}").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.PUT,    "/api/trainers/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.PATCH,  "/api/trainers/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/api/trainers/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.POST,   "/api/trainers/{id}/clients").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/trainers/{id}/clients").hasAnyRole("ADMIN", "TRAINER")

			    .antMatchers(HttpMethod.POST,   "/api/clients/").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.GET,    "/api/clients/").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.GET,    "/api/clients/{id}").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.PUT,    "/api/clients/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.PATCH,  "/api/clients/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/api/clients/{id}").hasAnyRole("ADMIN")
			    .antMatchers(HttpMethod.POST,   "/api/clients/{id}/trainers").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/clients/{id}/trainers").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.POST,   "/api/clients/{id}/workouts").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/clients/{id}/workouts").hasAnyRole("ADMIN", "TRAINER")

			    .antMatchers(HttpMethod.POST,   "/api/workout/").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.GET,    "/api/workout/").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.GET,    "/api/workout/{id}").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.PUT,    "/api/workout/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.PATCH,  "/api/workout/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/workout/{id}").hasAnyRole("ADMIN", "TRAINER")

			    .antMatchers(HttpMethod.POST,   "/api/workoutitems/").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.GET,    "/api/workoutitems/").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.GET,    "/api/workoutitems/{id}").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.PUT,    "/api/workoutitems/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.PATCH,  "/api/workoutitems/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/workoutitems/{id}").hasAnyRole("ADMIN", "TRAINER")

			    .antMatchers(HttpMethod.POST,   "/api/exercises/").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.GET,    "/api/exercises/").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.GET,    "/api/exercises/{id}").hasAnyRole("ADMIN", "TRAINER", "CLIENT")
			    .antMatchers(HttpMethod.PUT,    "/api/exercises/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.PATCH,  "/api/exercises/{id}").hasAnyRole("ADMIN", "TRAINER")
			    .antMatchers(HttpMethod.DELETE, "/api/exercises/{id}").hasAnyRole("ADMIN", "TRAINER")

				//.anyRequest().permitAll()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore( jwtFilter(), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}
