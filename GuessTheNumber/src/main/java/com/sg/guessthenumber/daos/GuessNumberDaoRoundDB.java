/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

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
public class GuessNumberDaoRoundDB implements GuessNumberDaoRound {

    @Autowired
    JdbcTemplate template;

    @Override
    @Transactional
    public int addRoundToGame(Round toAdd, Integer gameId) throws RequestNotExecutedException {
        int toReturn = 0;
        final String insertRound = "insert into Round(GameId, Guess, "
                + "TimeOfGuess, ExactMatches, PartialMatches) values (?,?,?,?,?)";
        try {
            template.update(insertRound, gameId, toAdd.getGuess(), toAdd.getTimeOfGuess(),
                    toAdd.getExactMatches(), toAdd.getPartialMatches());
            toReturn = template.queryForObject("select last_insert_id()", Integer.class);
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        } catch (NullPointerException ex) {
            throw new RequestNotExecutedException("Oops! Something went wrong. "
            + "Please try again.");
        }
        return toReturn;
    }

    @Override
    @Transactional
    public List<Round> getRoundsByGameId(int gameId) throws RequestNotExecutedException {
        List<Round> rounds = new ArrayList<>();
        try {
            rounds = template.query("select * from Round where GameId = ?",
                    new RoundMapper(), gameId);
            if (rounds.size() == 0) {
                throw new RequestNotExecutedException("No rounds were found for"
                + " game ID " + gameId);
            }
        } catch (DataAccessException ex) {
            throw new RequestNotExecutedException("The request was not able to be"
                    + " processed at this time.");
        }
        return rounds;
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
