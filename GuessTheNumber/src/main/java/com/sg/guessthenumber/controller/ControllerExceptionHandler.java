/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.controller;

import com.sg.guessthenumber.exceptions.Error;
import com.sg.guessthenumber.exceptions.InvalidGameIdException;
import com.sg.guessthenumber.exceptions.InvalidGuessException;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author codedchai
 */
@ControllerAdvice
@RestController
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage("Could not save the item. "
                + "Please ensure it is valid and try again.");
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({InvalidGameIdException.class})
    public final ResponseEntity<Error> handleInvalidGameIdException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage("The entered game ID does not exist.");
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler({InvalidGuessException.class})
    public final ResponseEntity<Error> handleInvalidGuessException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage("The entered guess is invalid. Please ensure it is a " +
                "number with four digits and no decimal.");
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler({RequestNotExecutedException.class})
    public final ResponseEntity<Error> handleRequestNotExecutedException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage("An error has occured and the request was not able to be "
                + "processed at this time. Please try again later.");
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
