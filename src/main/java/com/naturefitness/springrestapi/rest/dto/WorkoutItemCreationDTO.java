package com.naturefitness.springrestapi.rest.dto;

import java.sql.Time;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutItemCreationDTO {
    
	private Time duration;
	private Integer reps;
	private Integer priority;
    private Integer exerciseId;
}
