/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.controller;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.dtos.GameGuess;
import com.sg.guessthenumber.dtos.Round;
import com.sg.guessthenumber.exceptions.InvalidGameIdException;
import com.sg.guessthenumber.exceptions.InvalidGuessException;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import com.sg.guessthenumber.service.GuessNumberService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author codedchai
 */
@RestController
@RequestMapping("/api")
public class GuessNumberController {

    @Autowired
    GuessNumberService service;

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer startGame() throws RequestNotExecutedException {
        return service.generateGame();
//        return new ResponseEntity<>(gameId, HttpStatus.CREATED);
    }

    @PostMapping("/guess")
    public Round userGuess(@RequestBody GameGuess guessForGameId) throws 
            InvalidGameIdException, InvalidGuessException, RequestNotExecutedException {
            return service.getResults(guessForGameId.getGuess(),
                    guessForGameId.getGameId());
        }
    
    @GetMapping("/game")
    public List<Game> listAllGames() throws RequestNotExecutedException {
        return service.getAllGames();
    }
    
    @GetMapping("/game/{gameId}")
    public Game listGameById(@PathVariable int gameId) throws InvalidGameIdException, RequestNotExecutedException {
            return service.getGameById(gameId);
   
    }
    
    @GetMapping("rounds/{gameId}")
    public List<Round> listRoundsByGame(@PathVariable int gameId) throws InvalidGameIdException, RequestNotExecutedException {
            return service.getRoundsByGameId(gameId);
    }
    
}
