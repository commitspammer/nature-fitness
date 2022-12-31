package com.naturefitness.springrestapi.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminDTO {

	private Integer id;
	private PersonalDataDTO data;

}
