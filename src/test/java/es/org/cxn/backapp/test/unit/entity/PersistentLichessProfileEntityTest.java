package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;

class PersistentLichessProfileEntityTest {
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
        profile.setBlitzGames(100);
        assertEquals(100, profile.getBlitzGames());
    }

    @Test
    void testSetAndGetBlitzProg() {
        profile.setBlitzProg(20);
        assertEquals(20, profile.getBlitzProg());
    }

    @Test
    void testSetAndGetBlitzProv() {
        profile.setBlitzProv(true);
        assertEquals(true, profile.getBlitzProv());
    }

    @Test
    void testSetAndGetBlitzRating() {
        profile.setBlitzRating(1800);
        assertEquals(1800, profile.getBlitzRating());
    }

    @Test
    void testSetAndGetBlitzRd() {
        profile.setBlitzRd(50);
        assertEquals(50, profile.getBlitzRd());
    }

    // Bullet statistics tests
    @Test
    void testSetAndGetBulletGames() {
        profile.setBulletGames(200);
        assertEquals(200, profile.getBulletGames());
    }

    @Test
    void testSetAndGetBulletProg() {
        profile.setBulletProg(15);
        assertEquals(15, profile.getBulletProg());
    }

    @Test
    void testSetAndGetBulletProv() {
        profile.setBulletProv(false);
        assertEquals(false, profile.getBulletProv());
    }

    @Test
    void testSetAndGetBulletRating() {
        profile.setBulletRating(2000);
        assertEquals(2000, profile.getBulletRating());
    }

    @Test
    void testSetAndGetBulletRd() {
        profile.setBulletRd(30);
        assertEquals(30, profile.getBulletRd());
    }

    // Classical statistics tests
    @Test
    void testSetAndGetClassicalGames() {
        profile.setClassicalGames(150);
        assertEquals(150, profile.getClassicalGames());
    }

    @Test
    void testSetAndGetClassicalProg() {
        profile.setClassicalProg(25);
        assertEquals(25, profile.getClassicalProg());
    }

    @Test
    void testSetAndGetClassicalProv() {
        profile.setClassicalProv(true);
        assertEquals(true, profile.getClassicalProv());
    }

    @Test
    void testSetAndGetClassicalRating() {
        profile.setClassicalRating(1500);
        assertEquals(1500, profile.getClassicalRating());
    }

    @Test
    void testSetAndGetClassicalRd() {
        profile.setClassicalRd(40);
        assertEquals(40, profile.getClassicalRd());
    }

    // Test the setId() and getId() methods
    @Test
    public void testSetAndGetId() {
        // Arrange
        String testId = "lichess123";

        // Act
        profile.setId(testId);
        String retrievedId = profile.getId();

        // Assert
        assertEquals(testId, retrievedId, "The retrieved ID should match the set ID.");
    }

    // Puzzle statistics tests
    @Test
    void testSetAndGetPuzzleGames() {
        profile.setPuzzleGames(120);
        assertEquals(120, profile.getPuzzleGames());
    }

    @Test
    void testSetAndGetPuzzleProg() {
        profile.setPuzzleProg(30);
        assertEquals(30, profile.getPuzzleProg());
    }

    @Test
    void testSetAndGetPuzzleProv() {
        profile.setPuzzleProv(true);
        assertEquals(true, profile.getPuzzleProv());
    }

    @Test
    void testSetAndGetPuzzleRating() {
        profile.setPuzzleRating(2000);
        assertEquals(2000, profile.getPuzzleRating());
    }

    @Test
    void testSetAndGetPuzzleRd() {
        profile.setPuzzleRd(55);
        assertEquals(55, profile.getPuzzleRd());
    }

    // Rapid statistics tests
    @Test
    void testSetAndGetRapidGames() {
        profile.setRapidGames(80);
        assertEquals(80, profile.getRapidGames());
    }

    @Test
    void testSetAndGetRapidProg() {
        profile.setRapidProg(10);
        assertEquals(10, profile.getRapidProg());
    }

    @Test
    void testSetAndGetRapidProv() {
        profile.setRapidProv(false);
        assertEquals(false, profile.getRapidProv());
    }

    @Test
    void testSetAndGetRapidRating() {
        profile.setRapidRating(1700);
        assertEquals(1700, profile.getRapidRating());
    }

    @Test
    void testSetAndGetRapidRd() {
        profile.setRapidRd(60);
        assertEquals(60, profile.getRapidRd());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        profile.setUpdatedAt(now);
        assertEquals(now, profile.getUpdatedAt());
    }

    @Test
    void testSetAndGetUserDni() {
        profile.setUserDni("12345678A");
        assertEquals("12345678A", profile.getUserDni());
    }

    @Test
    void testSetAndGetUsername() {
        profile.setUsername("chessMaster");
        assertEquals("chessMaster", profile.getUsername());
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