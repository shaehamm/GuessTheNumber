/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.util.List;

/**
 *
 * @author codedchai
 */
public interface GuessNumberDaoGame {
    
    public Game generateGame(Game newGame) throws RequestNotExecutedException;

    public Game getGameById(Integer gameId) throws RequestNotExecutedException;

    public List<Game> getAllGames() throws RequestNotExecutedException;

    public void gameCompleted(Integer gameId) throws RequestNotExecutedException;
    
}
