package com.naturefitness.springrestapi.rest.dto;

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
public class TrainerCreationDTO {

	private PersonalDataDTO data;

}
