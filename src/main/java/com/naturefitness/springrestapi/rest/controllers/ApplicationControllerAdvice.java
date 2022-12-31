package com.naturefitness.springrestapi.rest.controllers;

import java.lang.RuntimeException;

import com.naturefitness.springrestapi.exception.EntityAlreadyExistsException;
import com.naturefitness.springrestapi.exception.EntityNotFoundException;
import com.naturefitness.springrestapi.exception.NullFieldException;
import com.naturefitness.springrestapi.exception.NonUniqueFieldException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import com.naturefitness.springrestapi.rest.dto.ErrorDTO;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(EntityAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorDTO handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.CONFLICT)
			.message(e.getMessage())
			.build();
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorDTO handleEntityNotFoundException(EntityNotFoundException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.build();
	}

	@ExceptionHandler(NullFieldException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleNullFieldException(NullFieldException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.BAD_REQUEST)
			.message(e.getMessage())
			.build();
	}

	@ExceptionHandler(NonUniqueFieldException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorDTO handleNonUniqueFieldException(NonUniqueFieldException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.CONFLICT)
			.message(e.getMessage())
			.build();
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorDTO handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.NOT_FOUND)
			.message(e.getMessage())
			.build();
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		return ErrorDTO.builder()
			.status(HttpStatus.BAD_REQUEST)
			.message(e.getMessage())
			.build();
	}
}
