/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.exceptions;

/**
 *
 * @author codedchai
 */
public class RequestNotExecutedException extends Exception {
    public RequestNotExecutedException(String message) {
        super(message);
    }
    
    public RequestNotExecutedException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
