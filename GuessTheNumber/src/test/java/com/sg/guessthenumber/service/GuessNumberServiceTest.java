/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.service;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.dtos.Round;
import com.sg.guessthenumber.exceptions.InvalidGameIdException;
import com.sg.guessthenumber.exceptions.InvalidGuessException;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author codedchai
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GuessNumberServiceTest {

    @Autowired
    GuessNumberService testService;

    public GuessNumberServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of generateGame method, of class GuessNumberService.
     */
    @Test
    public void testGenerateGameGoldenPath() throws InvalidGameIdException, RequestNotExecutedException {
        int generatedId = testService.generateGame();
        assertEquals(3, generatedId);

        Game toCheck = testService.getGameById(generatedId);
        assertFalse(toCheck.getStatus());
        assertEquals(3, toCheck.getGameId());
    }

    /**
     * Test of getResults method, of class GuessNumberService.
     */
    @Test
    public void testGetResultsGoldenPath() throws InvalidGameIdException, InvalidGuessException, RequestNotExecutedException {
        Round toCheck = testService.getResults("1908", 1);

        assertEquals("1908", toCheck.getGuess());
        assertEquals(1, toCheck.getExactMatches());
        assertEquals(2, toCheck.getPartialMatches());
        assertEquals(5, toCheck.getRoundId());

        Game checkStatus = testService.getGameById(1);

        assertFalse(checkStatus.getStatus());
    }
    
        @Test
    public void testGetResultsGoldenPathFinishedGame() throws InvalidGameIdException, InvalidGuessException, RequestNotExecutedException {
        Round toCheck = testService.getResults("0987", 1);

        assertEquals("0987", toCheck.getGuess());
        assertEquals(4, toCheck.getExactMatches());
        assertEquals(0, toCheck.getPartialMatches());
        assertEquals(5, toCheck.getRoundId());

        Game checkStatus = testService.getGameById(1);

        assertTrue(checkStatus.getStatus());
    }

    @Test
    public void testGetResultsInvalidGuess() throws RequestNotExecutedException, InvalidGuessException {
        try {
            Round toCheck = testService.getResults("1908", -11);
            fail();
        } catch (InvalidGameIdException ex) {

        }
    }

    @Test
    public void testGetResultsInvalidId() throws InvalidGameIdException, RequestNotExecutedException {
        try {
            Round toCheck = testService.getResults("The right answer", 1);
            fail();
        } catch (InvalidGuessException ex) {

        }
    }

    @Test
    public void testGetResultsInvalidIdLength() throws InvalidGameIdException, RequestNotExecutedException {
        try {
            Round toCheck = testService.getResults("01234", 1);
            fail();
        } catch (InvalidGuessException ex) {

        }
    }

    /**
     * Test of getAllGames method, of class GuessNumberService.
     */
    @Test
    public void testGetAllGamesGoldenPath() throws RequestNotExecutedException {
        List<Game> toCheck = testService.getAllGames();

        assertEquals(2, toCheck.size());
        assertEquals("Hidden", toCheck.get(0).getAnswer());
        assertEquals(1, toCheck.get(0).getGameId());
        assertFalse(toCheck.get(0).getStatus());

        assertEquals("1234", toCheck.get(1).getAnswer());
        assertEquals(2, toCheck.get(1).getGameId());
        assertTrue(toCheck.get(1).getStatus());
    }

    /**
     * Test of getGameById method, of class GuessNumberService.
     */
    @Test
    public void testGetGameByIdGoldenPath() throws InvalidGameIdException, RequestNotExecutedException {
        Game toCheck = testService.getGameById(1);
        assertEquals("Hidden", toCheck.getAnswer());
        assertEquals(1, toCheck.getGameId());
        assertFalse(toCheck.getStatus());
        assertEquals(2, toCheck.getRound().size());

        Game toCheck2 = testService.getGameById(2);
        assertEquals("1234", toCheck2.getAnswer());
        assertEquals(2, toCheck2.getGameId());
        assertTrue(toCheck2.getStatus());
        assertEquals(2, toCheck2.getRound().size());
    }

    @Test
    public void testGetGameByIdInvalid() throws RequestNotExecutedException {
        try {
            Game toCheck = testService.getGameById(6);
            fail();
        } catch (InvalidGameIdException ex) {

        }
    }

    /**
     * Test of getRoundsByGameId method, of class GuessNumberService.
     */
    @Test
    public void testGetRoundsByGameIdGoldenPath() throws InvalidGameIdException, RequestNotExecutedException {
        List<Round> toCheck = testService.getRoundsByGameId(2);

        assertEquals(2, toCheck.size());

        assertEquals("4120", toCheck.get(0).getGuess());
        assertEquals(0, toCheck.get(0).getExactMatches());
        assertEquals(3, toCheck.get(0).getPartialMatches());
        assertEquals(3, toCheck.get(0).getRoundId());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 5), toCheck.get(0).getTimeOfGuess());

        assertEquals("1234", toCheck.get(1).getGuess());
        assertEquals(4, toCheck.get(1).getExactMatches());
        assertEquals(0, toCheck.get(1).getPartialMatches());
        assertEquals(4, toCheck.get(1).getRoundId());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 7), toCheck.get(1).getTimeOfGuess());

    }

    @Test
    public void testGetRoundsByGameIdInvalid() throws RequestNotExecutedException {
        try {
            List<Round> toCheck = testService.getRoundsByGameId(-1);
            fail();
        } catch (InvalidGameIdException ex) {

        }
    }

}
