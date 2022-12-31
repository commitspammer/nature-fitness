package com.naturefitness.springrestapi.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseCreationDTO {

	private String name;
	private String description;

}
