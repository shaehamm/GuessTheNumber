/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.dtos;

import java.util.List;

/**
 *
 * @author codedchai
 */
public class Game {
    
    private int gameId;
    private List<Round> round;
    private String answer;
    private boolean status;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<Round> getRound() {
        return round;
    }

    public void setRound(List<Round> round) {
        this.round = round;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
