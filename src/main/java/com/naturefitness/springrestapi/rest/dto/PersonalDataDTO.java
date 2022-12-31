package com.naturefitness.springrestapi.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonalDataDTO {

	private String name;
	private String email;
	private String number;

}
