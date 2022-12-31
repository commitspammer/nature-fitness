package com.naturefitness.springrestapi.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {

	private Integer id;
	private String name;
	private String description;

}
