/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Round;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.util.List;

/**
 *
 * @author codedchai
 */
public interface GuessNumberDaoRound {

    public int addRoundToGame(Round toReturn, Integer gameId) throws RequestNotExecutedException;

    public List<Round> getRoundsByGameId(int gameId) throws RequestNotExecutedException;
    
}
