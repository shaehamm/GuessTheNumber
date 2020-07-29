/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.dtos.Round;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 *
 * @author codedchai
 */
@Repository
@Profile("service-test")
public class GuessNumberInMemDao implements GuessNumberDaoRound, GuessNumberDaoGame {

    List<Game> allGames = new ArrayList<>();

    public GuessNumberInMemDao() {
        List<Round> roundList = new ArrayList<>();

        Round round1 = new Round();
        round1.setGuess("1234");
        round1.setRoundId(1);
        round1.setExactMatches(0);
        round1.setPartialMatches(0);
        round1.setTimeOfGuess(LocalDateTime.of(2020, 1, 1, 1, 1));
        roundList.add(round1);

        Round round2 = new Round();
        round2.setGuess("0567");
        round2.setRoundId(2);
        round2.setExactMatches(1);
        round2.setPartialMatches(1);
        round2.setTimeOfGuess(LocalDateTime.of(2020, 1, 1, 1, 3));
        roundList.add(round2);

        Game game1 = new Game();
        game1.setAnswer("0987");
        game1.setGameId(1);
        game1.setStatus(false);
        game1.setRound(roundList);

        List<Round> roundList2 = new ArrayList<>();

        round1.setGuess("4120");
        round1.setRoundId(3);
        round1.setExactMatches(0);
        round1.setPartialMatches(3);
        round1.setTimeOfGuess(LocalDateTime.of(2020, 2, 1, 1, 5));
        roundList2.add(round1);

        round2.setGuess("1234");
        round2.setRoundId(4);
        round2.setExactMatches(4);
        round2.setPartialMatches(0);
        round2.setTimeOfGuess(LocalDateTime.of(2020, 2, 1, 1, 7));
        roundList2.add(round2);

        Game game2 = new Game();
        game2.setAnswer("1234");
        game2.setGameId(2);
        game2.setStatus(true);
        game2.setRound(roundList2);
        
//        List<Round> roundList3 = new ArrayList<>();
//
//        round1.setGuess("5643");
//        round1.setRoundId(5);
//        round1.setExactMatches(1);
//        round1.setPartialMatches(1);
//        round1.setTimeOfGuess(LocalDateTime.of(2020, 2, 1, 9, 8));
//        roundList3.add(round1);
//
//        Game game3 = new Game();
//        game3.setAnswer("3678");
//        game3.setGameId(3);
//        game3.setStatus(false);
//        game3.setRound(roundList3);

        allGames.add(game1);
        allGames.add(game2);
    }

    @Override
    public int addRoundToGame(Round toAdd, Integer gameId) {
        int roundId = 0;
        for (Game g : allGames) {
            if (g.getRound() != null) {
                for (int i = 0; i < g.getRound().size(); i++) {
                    if (g.getRound().get(i).getRoundId() >= roundId) {
                        roundId = g.getRound().get(i).getRoundId() + 1;
                    }
                }
            }
        }
        toAdd.setRoundId(roundId);
        for (Game g : allGames) {
            if (g.getGameId() == gameId) {
                g.getRound().add(toAdd);
            }
        }
        return roundId;
    }

    @Override
    public List<Round> getRoundsByGameId(int gameId) {
        List<Round> toReturn = new ArrayList<>();
        for (Game g : allGames) {
            if (g.getGameId() == gameId) {
                toReturn = g.getRound();
            }
        }
        return toReturn;
    }

    @Override
    public Game generateGame(Game newGame) {
        for (Game g : allGames) {
            if (g.getGameId() >= newGame.getGameId()) {
                newGame.setGameId(g.getGameId() + 1);
            }
        }
        allGames.add(newGame);
        return newGame;
    }

    @Override
    public Game getGameById(Integer gameId) {
        allGames.get(0).setAnswer("0987");
        Game toReturn = new Game();
        for (Game g : allGames) {
            if (g.getGameId() == gameId) {
                toReturn = g;
            }
        }
        return toReturn;
    }

    @Override
    public List<Game> getAllGames() {
        return allGames;
    }

    @Override
    public void gameCompleted(Integer gameId) {
        for (Game g : allGames) {
            if (g.getGameId() == gameId) {
                g.setStatus(true);
            }
        }
    }

}
