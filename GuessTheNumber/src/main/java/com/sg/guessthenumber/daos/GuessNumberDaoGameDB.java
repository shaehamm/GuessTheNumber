/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.dtos.Round;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author codedchai
 */
@Component
@Profile({"production", "dao-test"})
public class GuessNumberDaoGameDB implements GuessNumberDaoGame {

    @Autowired
    JdbcTemplate template;

    @Override
    @Transactional
    public Game generateGame(Game newGame) throws RequestNotExecutedException {
        int newId = 0;
        final String STMNT = "insert into Game(Answer, `Status`) values (?,?)";
        try {
            template.update(STMNT, newGame.getAnswer(), newGame.getStatus());
            newId = template.queryForObject("select last_insert_id()", Integer.class);
        } catch (DataAccessException | NullPointerException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
        newGame.setGameId(newId);
        return newGame;
    }

    @Override
    public Game getGameById(Integer gameId) throws RequestNotExecutedException {
        final String selectById = "select * from Game where GameId = ?";
        Game toReturn = new Game();
        try {
            toReturn = template.queryForObject(selectById, new GameMapper(), gameId);
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
        return toReturn;
    }

    @Override
    @Transactional
    public List<Game> getAllGames() throws RequestNotExecutedException {
        List<Game> allGames = new ArrayList<>();
        try {
            allGames = template.query("select * from Game", new GameMapper());
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
        addRoundsToGames(allGames);
        return allGames;
    }

    @Override
    public void gameCompleted(Integer gameId) throws RequestNotExecutedException {
        try {
            int rows = template.update("update Game set Status = true where GameId = ?", gameId);
            if (rows != 1) {
                throw new RequestNotExecutedException("The request did not go through.");
            }
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
    }

    private void addRoundsToGames(List<Game> allGames) throws RequestNotExecutedException {
        try {
            for (Game g : allGames) {
                g.setRound(template.query("select Round.* from Round join Game on Round.GameId = "
                        + "Game.GameId where Game.GameId = ?", new RoundMapper(), g.getGameId()));
            }
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
    }

    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game gm = new Game();
            gm.setGameId(rs.getInt("GameId"));
            gm.setAnswer(rs.getString("Answer"));
            gm.setStatus(rs.getBoolean("Status"));
            return gm;
        }

    }

    public static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round rd = new Round();
            rd.setRoundId(rs.getInt("RoundId"));
            rd.setGuess(rs.getString("Guess"));
            rd.setTimeOfGuess(rs.getTimestamp("TimeOfGuess").toLocalDateTime());
            rd.setPartialMatches(rs.getInt("PartialMatches"));
            rd.setExactMatches(rs.getInt("ExactMatches"));
            return rd;
        }

    }

}
