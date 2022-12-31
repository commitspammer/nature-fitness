package com.naturefitness.springrestapi.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {

	private String login;
	private String token;

}
