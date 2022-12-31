package com.naturefitness.springrestapi.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.time.LocalDate;

@Data
@Builder
public class WorkoutDTO {

	private Integer id;
	private String title;
	private List<Integer> items;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;
	//private Integer client;

}
