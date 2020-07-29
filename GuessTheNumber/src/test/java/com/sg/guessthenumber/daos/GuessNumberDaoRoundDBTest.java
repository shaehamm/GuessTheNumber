/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Round;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author codedchai
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dao-test")
public class GuessNumberDaoRoundDBTest {

    @Autowired
    GuessNumberDaoRound testDao;

    @Autowired
    JdbcTemplate template;

    public GuessNumberDaoRoundDBTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        template.update("DELETE FROM Round");
        template.update("DELETE FROM Game");

        template.update("ALTER TABLE Round auto_increment = 1");
        template.update("ALTER TABLE Game auto_increment = 1");
        template.update("INSERT INTO Game (GameId, Answer, Status) VALUES "
                + "(1, '1234', true), (2, '9876', false), (3, '0361', false);");
        template.update("INSERT INTO Round (GameId, Guess, TimeOfGuess, "
                + "ExactMatches, PartialMatches) VALUES (1, '1234', '2020-06-20T15:09:15.409036',"
                + "4, 0), (2, '9061', '2020-06-19T15:10:05.409036', 1, 1);");
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addRoundToGame method, of class GuessNumberDaoRoundDB.
     */
    @Test
    public void testAddRoundToGameGoldenPath() throws RequestNotExecutedException {
        Round toAdd = new Round();
        toAdd.setGuess("9210");
        toAdd.setExactMatches(1);
        toAdd.setPartialMatches(0);
        toAdd.setTimeOfGuess(LocalDateTime.now());
        int roundId = testDao.addRoundToGame(toAdd, 2);

        List<Round> toCheck = testDao.getRoundsByGameId(2);

        assertEquals(2, toCheck.size());
        assertEquals(3, roundId);
        assertEquals(3, toCheck.get(1).getRoundId());
        assertEquals("9210", toCheck.get(1).getGuess());
        assertEquals(1, toCheck.get(1).getExactMatches());
        assertEquals(0, toCheck.get(1).getPartialMatches());

    }
    
    @Test
    public void testAddRoundToGameNullRound() {
        try {
            int roundId = testDao.addRoundToGame(null, 2);
            fail();
        } catch (RequestNotExecutedException ex) {

        }

    }
    
    @Test
    public void testAddRoundToGameInvalidId() {
        Round toAdd = new Round();
        toAdd.setGuess("9210");
        toAdd.setExactMatches(1);
        toAdd.setPartialMatches(0);
        toAdd.setTimeOfGuess(LocalDateTime.now());
        try {
            int roundId = testDao.addRoundToGame(toAdd, 10);
            fail();
        } catch (RequestNotExecutedException ex) {

        }

    }

    /**
     * Test of getRoundsByGameId method, of class GuessNumberDaoRoundDB.
     */
    @Test
    public void testGetRoundsByGameIdGoldenPath() throws RequestNotExecutedException {
        List<Round> toCheck = testDao.getRoundsByGameId(1);
        assertEquals(1, toCheck.size());
        assertEquals(4, toCheck.get(0).getExactMatches());
        assertEquals(0, toCheck.get(0).getPartialMatches());
        assertEquals("1234", toCheck.get(0).getGuess());
        assertEquals(1, toCheck.get(0).getRoundId());
        assertEquals(LocalDateTime.of(2020, 6, 20, 15, 9, 15), toCheck.get(0).getTimeOfGuess());

    }
    
    @Test
    public void testGetRoundsByGameIdInvalid() {
        try {
            List<Round> toCheck = testDao.getRoundsByGameId(-10);
            fail();
        } catch (RequestNotExecutedException ex) {

        }

    }

}
