package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;

class PersistentLichessProfileEntityTest {
    /**
     * The number of games played in the Blitz category.
     */
    private static final int BLITZ_GAMES = 100;

    /**
     * The progress value for Blitz games.
     */
    private static final int BLITZ_PROG = 20;

    /**
     * Indicates if Blitz games are provisional.
     */
    private static final boolean BLITZ_PROV = true;

    /**
     * The rating of the user in Blitz games.
     */
    private static final int BLITZ_RATING = 1800;

    /**
     * The rating deviation (RD) for Blitz games.
     */
    private static final int BLITZ_RD = 50;

    /**
     * The number of games played in the Bullet category.
     */
    private static final int BULLET_GAMES = 200;

    /**
     * The progress value for Bullet games.
     */
    private static final int BULLET_PROG = 15;

    /**
     * Indicates if Bullet games are provisional.
     */
    private static final boolean BULLET_PROV = false;

    /**
     * The rating of the user in Bullet games.
     */
    private static final int BULLET_RATING = 2000;

    /**
     * The rating deviation (RD) for Bullet games.
     */
    private static final int BULLET_RD = 30;

    /**
     * The number of games played in the Classical category.
     */
    private static final int CLASSICAL_GAMES = 150;

    /**
     * The progress value for Classical games.
     */
    private static final int CLASSICAL_PROG = 25;

    /**
     * Indicates if Classical games are provisional.
     */
    private static final boolean CLASSICAL_PROV = true;

    /**
     * The rating of the user in Classical games.
     */
    private static final int CLASSICAL_RATING = 1500;

    /**
     * The rating deviation (RD) for Classical games.
     */
    private static final int CLASSICAL_RD = 40;

    /**
     * The number of games played in the Puzzle category.
     */
    private static final int PUZZLE_GAMES = 120;

    /**
     * The progress value for Puzzle games.
     */
    private static final int PUZZLE_PROG = 30;

    /**
     * Indicates if Puzzle games are provisional.
     */
    private static final boolean PUZZLE_PROV = true;

    /**
     * The rating of the user in Puzzle games.
     */
    private static final int PUZZLE_RATING = 2000;

    /**
     * The rating deviation (RD) for Puzzle games.
     */
    private static final int PUZZLE_RD = 55;

    /**
     * The number of games played in the Rapid category.
     */
    private static final int RAPID_GAMES = 80;

    /**
     * The progress value for Rapid games.
     */
    private static final int RAPID_PROG = 10;

    /**
     * Indicates if Rapid games are provisional.
     */
    private static final boolean RAPID_PROV = false;

    /**
     * The rating of the user in Rapid games.
     */
    private static final int RAPID_RATING = 1700;

    /**
     * The rating deviation (RD) for Rapid games.
     */
    private static final int RAPID_RD = 60;

    /**
     * The test identifier for the Lichess profile.
     */
    private static final String TEST_ID = "lichess123";

    /**
     * The user's DNI (document number) for testing purposes.
     */
    private static final String USER_DNI = "12345678A";

    /**
     * The username of the user for testing purposes.
     */
    private static final String USERNAME = "chessMaster";

    /**
     * The Lichess profile entity.
     */
    private PersistentLichessProfileEntity profile;

    @BeforeEach
    public void setUp() {
        profile = new PersistentLichessProfileEntity();
    }

    @Test
    void testNullValues() {
        assertNull(profile.getUserDni());
        assertNull(profile.getUsername());
        assertNull(profile.getBlitzGames());
        assertNull(profile.getBlitzRating());
        assertNull(profile.getUpdatedAt());
    }

    // Blitz statistics tests
    @Test
    void testSetAndGetBlitzGames() {
        profile.setBlitzGames(BLITZ_GAMES);
        assertEquals(BLITZ_GAMES, profile.getBlitzGames());
    }

    @Test
    void testSetAndGetBlitzProg() {
        profile.setBlitzProg(BLITZ_PROG);
        assertEquals(BLITZ_PROG, profile.getBlitzProg());
    }

    @Test
    void testSetAndGetBlitzProv() {
        profile.setBlitzProv(BLITZ_PROV);
        assertEquals(BLITZ_PROV, profile.isBlitzProv());
    }

    @Test
    void testSetAndGetBlitzRating() {
        profile.setBlitzRating(BLITZ_RATING);
        assertEquals(BLITZ_RATING, profile.getBlitzRating());
    }

    @Test
    void testSetAndGetBlitzRd() {
        profile.setBlitzRd(BLITZ_RD);
        assertEquals(BLITZ_RD, profile.getBlitzRd());
    }

    // Bullet statistics tests
    @Test
    void testSetAndGetBulletGames() {
        profile.setBulletGames(BULLET_GAMES);
        assertEquals(BULLET_GAMES, profile.getBulletGames());
    }

    @Test
    void testSetAndGetBulletProg() {
        profile.setBulletProg(BULLET_PROG);
        assertEquals(BULLET_PROG, profile.getBulletProg());
    }

    @Test
    void testSetAndGetBulletProv() {
        profile.setBulletProv(BULLET_PROV);
        assertEquals(BULLET_PROV, profile.isBulletProv());
    }

    @Test
    void testSetAndGetBulletRating() {
        profile.setBulletRating(BULLET_RATING);
        assertEquals(BULLET_RATING, profile.getBulletRating());
    }

    @Test
    void testSetAndGetBulletRd() {
        profile.setBulletRd(BULLET_RD);
        assertEquals(BULLET_RD, profile.getBulletRd());
    }

    // Classical statistics tests
    @Test
    void testSetAndGetClassicalGames() {
        profile.setClassicalGames(CLASSICAL_GAMES);
        assertEquals(CLASSICAL_GAMES, profile.getClassicalGames());
    }

    @Test
    void testSetAndGetClassicalProg() {
        profile.setClassicalProg(CLASSICAL_PROG);
        assertEquals(CLASSICAL_PROG, profile.getClassicalProg());
    }

    @Test
    void testSetAndGetClassicalProv() {
        profile.setClassicalProv(CLASSICAL_PROV);
        assertEquals(CLASSICAL_PROV, profile.isClassicalProv());
    }

    @Test
    void testSetAndGetClassicalRating() {
        profile.setClassicalRating(CLASSICAL_RATING);
        assertEquals(CLASSICAL_RATING, profile.getClassicalRating());
    }

    @Test
    void testSetAndGetClassicalRd() {
        profile.setClassicalRd(CLASSICAL_RD);
        assertEquals(CLASSICAL_RD, profile.getClassicalRd());
    }

    // Test the setId() and getId() methods
    @Test
    void testSetAndGetId() {
        profile.setIdentifier(TEST_ID);
        assertEquals(TEST_ID, profile.getIdentifier(), "The retrieved ID should match the set ID.");
    }

    // Puzzle statistics tests
    @Test
    void testSetAndGetPuzzleGames() {
        profile.setPuzzleGames(PUZZLE_GAMES);
        assertEquals(PUZZLE_GAMES, profile.getPuzzleGames());
    }

    @Test
    void testSetAndGetPuzzleProg() {
        profile.setPuzzleProg(PUZZLE_PROG);
        assertEquals(PUZZLE_PROG, profile.getPuzzleProg());
    }

    @Test
    void testSetAndGetPuzzleProv() {
        profile.setPuzzleProv(PUZZLE_PROV);
        assertEquals(PUZZLE_PROV, profile.isPuzzleProv());
    }

    @Test
    void testSetAndGetPuzzleRating() {
        profile.setPuzzleRating(PUZZLE_RATING);
        assertEquals(PUZZLE_RATING, profile.getPuzzleRating());
    }

    @Test
    void testSetAndGetPuzzleRd() {
        profile.setPuzzleRd(PUZZLE_RD);
        assertEquals(PUZZLE_RD, profile.getPuzzleRd());
    }

    // Rapid statistics tests
    @Test
    void testSetAndGetRapidGames() {
        profile.setRapidGames(RAPID_GAMES);
        assertEquals(RAPID_GAMES, profile.getRapidGames());
    }

    @Test
    void testSetAndGetRapidProg() {
        profile.setRapidProg(RAPID_PROG);
        assertEquals(RAPID_PROG, profile.getRapidProg());
    }

    @Test
    void testSetAndGetRapidProv() {
        profile.setRapidProv(RAPID_PROV);
        assertEquals(RAPID_PROV, profile.isRapidProv());
    }

    @Test
    void testSetAndGetRapidRating() {
        profile.setRapidRating(RAPID_RATING);
        assertEquals(RAPID_RATING, profile.getRapidRating());
    }

    @Test
    void testSetAndGetRapidRd() {
        profile.setRapidRd(RAPID_RD);
        assertEquals(RAPID_RD, profile.getRapidRd());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        profile.setUpdatedAt(now);
        assertEquals(now, profile.getUpdatedAt());
    }

    @Test
    void testSetAndGetUserDni() {
        profile.setUserDni(USER_DNI);
        assertEquals(USER_DNI, profile.getUserDni());
    }

    @Test
    void testSetAndGetUsername() {
        profile.setUsername(USERNAME);
        assertEquals(USERNAME, profile.getUsername());
    }

    // Test setting fields to null (if applicable)
    @Test
    void testSetFieldsToNull() {
        profile.setBlitzGames(null);
        assertNull(profile.getBlitzGames());

        profile.setBulletGames(null);
        assertNull(profile.getBulletGames());

        profile.setClassicalGames(null);
        assertNull(profile.getClassicalGames());

        profile.setRapidGames(null);
        assertNull(profile.getRapidGames());

        profile.setPuzzleGames(null);
        assertNull(profile.getPuzzleGames());

        profile.setUsername(null);
        assertNull(profile.getUsername());

        profile.setUserDni(null);
        assertNull(profile.getUserDni());
    }
}
