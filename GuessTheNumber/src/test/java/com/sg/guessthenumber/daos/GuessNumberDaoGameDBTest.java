/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessthenumber.daos;

import com.sg.guessthenumber.dtos.Game;
import com.sg.guessthenumber.exceptions.RequestNotExecutedException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class GuessNumberDaoGameDBTest {

    @Autowired
    GuessNumberDaoGame testDao;

    @Autowired
    JdbcTemplate template;

    public GuessNumberDaoGameDBTest() {
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

        template.update("ALTER TABLE Game auto_increment = 1");
        template.update("ALTER TABLE Round auto_increment = 1");
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
     * Test of generateGame method, of class GuessNumberDaoGameDB.
     */
    @Test
    public void testGenerateGameGoldenPath() throws RequestNotExecutedException {
        Game toCreate = new Game();
        toCreate.setAnswer("9753");
        toCreate.setStatus(false);
        Game generated = testDao.generateGame(toCreate);
        Game gotById = testDao.getGameById(generated.getGameId());

        assertEquals(4, generated.getGameId());
        assertEquals("9753", generated.getAnswer());
        assertEquals(false, generated.getStatus());
        assertTrue(generated.getRound() == null);

        assertEquals(4, gotById.getGameId());
        assertEquals("9753", gotById.getAnswer());
        assertEquals(false, gotById.getStatus());
        assertTrue(gotById.getRound() == null);
    }
    
    @Test
    public void testGenerateGameNull() {
        try {
            Game generated = testDao.generateGame(null);
            fail();
        } catch (RequestNotExecutedException ex) {

        }
    }

    /**
     * Test of getGameById method, of class GuessNumberDaoGameDB.
     */
    @Test
    public void testGetGameByIdGoldenPath() throws RequestNotExecutedException {
        Game toCheck = testDao.getGameById(2);

        assertEquals(2, toCheck.getGameId());
        assertEquals("9876", toCheck.getAnswer());
        assertFalse(toCheck.getStatus());
    }
    
    @Test
    public void testGetGameByIdInvalid() {
        try {
            Game toCheck = testDao.getGameById(100);
            fail();
        } catch (RequestNotExecutedException ex) {

        }
    }

    /**
     * Test of getAllGames method, of class GuessNumberDaoGameDB.
     */
    @Test
    public void testGetAllGamesGoldenPath() throws RequestNotExecutedException {
        List<Game> toCheck = testDao.getAllGames();

        assertEquals(3, toCheck.size());

        assertEquals(1, toCheck.get(0).getGameId());
        assertEquals("1234", toCheck.get(0).getAnswer());
        assertEquals(1, toCheck.get(0).getRound().size());
        assertTrue(toCheck.get(0).getStatus());

        assertEquals(2, toCheck.get(1).getGameId());
        assertEquals("9876", toCheck.get(1).getAnswer());
        assertEquals(1, toCheck.get(1).getRound().size());
        assertFalse(toCheck.get(1).getStatus());

        assertEquals(3, toCheck.get(2).getGameId());
        assertEquals("0361", toCheck.get(2).getAnswer());
        assertTrue(toCheck.get(2).getRound().isEmpty());
        assertFalse(toCheck.get(2).getStatus());
    }

    @Test
    public void TestGameCompletedGoldenPath() throws RequestNotExecutedException {
        testDao.gameCompleted(3);
        Game toCheck = testDao.getGameById(3);
        assertTrue(toCheck.getStatus());
    }
    
    @Test
    public void TestGameCompletedInvalidId() {
        try {
            testDao.gameCompleted(-3);
            fail();
        } catch (RequestNotExecutedException ex) {

        }
    }

}
