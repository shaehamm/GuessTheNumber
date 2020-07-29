/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.service;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.dtos.Round;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sg.guessthenumber.daos.GuessNumberDaoGame;
import com.sg.guessthenumber.daos.GuessNumberDaoRound;
import com.sg.guessthenumber.exceptions.InvalidGameIdException;
import com.sg.guessthenumber.exceptions.InvalidGuessException;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.time.LocalDateTime;

/**
 *
 * @author codedchai
 */
@Service
public class GuessNumberService {

    @Autowired
    GuessNumberDaoRound roundDao;

    @Autowired
    GuessNumberDaoGame gameDao;

    public Integer generateGame() throws RequestNotExecutedException {
        Game newGame = new Game();
        newGame.setAnswer(generateAnswer());
        newGame.setStatus(false);
        newGame = gameDao.generateGame(newGame);
        return newGame.getGameId();
    }

    public Round getResults(String guess, Integer gameId) throws InvalidGameIdException,
            InvalidGuessException,
            RequestNotExecutedException {
        verifyGameId(gameId);
        verifyGuess(guess);
        Round toReturn = new Round();
        Game currentGame = gameDao.getGameById(gameId);
        if (guess.equals(currentGame.getAnswer())) {
            gameDao.gameCompleted(gameId);
            toReturn.setExactMatches(guess.length());
        } else {
            toReturn = checkMatches(guess, currentGame.getAnswer());
        }
        int roundId = roundDao.addRoundToGame(toReturn, gameId);
        toReturn.setRoundId(roundId);
        toReturn.setGuess(guess);
        toReturn.setTimeOfGuess(LocalDateTime.now());
        return toReturn;
    }

    private String generateAnswer() {
        List<String> numberedArray = Arrays.asList("0", "1", "2", "3", "4",
                "5", "6", "7", "8", "9");

        Collections.shuffle(numberedArray);

        return numberedArray.get(0) + numberedArray.get(1)
                + numberedArray.get(2) + numberedArray.get(3);
    }

    private Round checkMatches(String guess, String answer) {
        int partial = 0;
        int exact = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                exact++;
            } else if (guess.contains(String.valueOf(answer.charAt(i)))) {
                partial++;
            }
        }
        Round toReturn = new Round();
        toReturn.setExactMatches(exact);
        toReturn.setPartialMatches(partial);
        return toReturn;
    }

    public List<Game> getAllGames() throws RequestNotExecutedException {
        List<Game> allGames = gameDao.getAllGames();
        for (Game g : allGames) {
            if (g.getStatus() == false) {
                g.setAnswer("Hidden");
            }
        }
        return allGames;
    }

    public Game getGameById(int gameId) throws InvalidGameIdException, RequestNotExecutedException {
        verifyGameId(gameId);
        Game toReturn = gameDao.getGameById(gameId);
        if (toReturn.getStatus() == false) {
            toReturn.setAnswer("Hidden");
        }
        return toReturn;
    }

    public List<Round> getRoundsByGameId(int gameId) throws InvalidGameIdException, RequestNotExecutedException {
        verifyGameId(gameId);
        return roundDao.getRoundsByGameId(gameId);
    }

    private void verifyGameId(int gameId) throws InvalidGameIdException, RequestNotExecutedException {
        List<Game> allGames = getAllGames();
        boolean match = false;
        for (Game g : allGames) {
            if (g.getGameId() == gameId) {
                match = true;
            }
        }
        if (!match) {
            throw new InvalidGameIdException("The entered game ID does not exist.");
        }
    }

    private void verifyGuess(String guess) throws InvalidGuessException {
        try {
            Integer.parseInt(guess);
        } catch (NumberFormatException ex) {
            throw new InvalidGuessException("User's guess must be a valid integer.");
        }
        if (guess.length() != 4) {
            throw new InvalidGuessException("User's guess must be four numbers long.");
        }
    }

}
