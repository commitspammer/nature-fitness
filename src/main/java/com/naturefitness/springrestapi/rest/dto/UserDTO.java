package com.naturefitness.springrestapi.rest.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

	private String login;
	private List<String> roles;
	private Integer roleEntity;

}
