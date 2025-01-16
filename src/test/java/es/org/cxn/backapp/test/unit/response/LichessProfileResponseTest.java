
package es.org.cxn.backapp.test.unit.response;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.LichessProfileResponse;

/**
 * Unit tests for the LichessProfileResponse class. This test class validates
 * the functionality and behavior of the LichessProfileResponse object, ensuring
 * that user data and game-related attributes are correctly handled.
 */
class LichessProfileResponseTest {

    /**
     * Constant representing the Elo rating for Game 1. Used in test cases to
     * validate the Elo rating for the first game type.
     */
    private static final int GAME_1_ELO = 1800;

    /**
     * Constant representing the number of games played for Game 1. Used in test
     * cases to validate the number of games for the first game type.
     */
    private static final int GAME_1_AMOUNT_GAMES = 50;

    /**
     * Constant representing the Elo rating for Game 2. Used in test cases to
     * validate the Elo rating for the second game type.
     */
    private static final int GAME_2_ELO = 1800;

    /**
     * Constant representing the number of games played for Game 2. Used in test
     * cases to validate the number of games for the second game type.
     */
    private static final int GAME_2_AMOUNT_GAMES = 50;

    /**
     * Constant representing the user's real name. Used in test cases to verify the
     * handling of the user's name.
     */
    private static final String USER_NAME = "John Doe";

    /**
     * Constant representing the Lichess username of the user. Used in test cases to
     * validate the handling of the Lichess username.
     */
    private static final String LICHESS_USER_NAME = "JD123";

    /**
     * Constant representing the timestamp of the last update to the Lichess
     * profile. Used in test cases to validate the handling of profile update
     * timestamps.
     */
    private static final LocalDateTime LICHESS_PROFILE_LAST_UPDATE = LocalDateTime.now();

    /**
     * Constant representing the Elo rating for Blitz games. Used in test cases to
     * validate the Elo rating for Blitz game type.
     */
    private static final int BLITZ_GAME_ELO = 2000;

    /**
     * Constant representing the Elo rating for other games. Used in test cases to
     * validate the Elo rating for general game types.
     */
    private static final int OTHER_GAME_ELO = 1500;

    /**
     * Constant representing the Elo rating for Bullet games. Used in test cases to
     * validate the Elo rating for Bullet game type.
     */
    private static final int BULLET_GAME_ELO = 1900;

    /**
     * Constant representing the Elo rating for Rapid games. Used in test cases to
     * validate the Elo rating for Rapid game type.
     */
    private static final int RAPID_GAME_ELO = 2100;

    /**
     * Constant representing the Elo rating for Classical games. Used in test cases
     * to validate the Elo rating for Classical game type.
     */
    private static final int CLASSICAL_GAME_ELO = 1800;

    /**
     * Constant representing the Elo rating for Puzzle games. Used in test cases to
     * validate the Elo rating for Puzzle game type.
     */
    private static final int PUZZLE_GAME_ELO = 2200;

    @Test
    void testEqualsAndHashCodeForGame() {
        // Arrange
        LichessProfileResponse.Game game1 = new LichessProfileResponse.Game(GAME_1_ELO, GAME_1_AMOUNT_GAMES, false);
        LichessProfileResponse.Game game2 = new LichessProfileResponse.Game(GAME_2_ELO, GAME_2_AMOUNT_GAMES, false);

        // Act & Assert
        assertEquals(game1, game2, "Game records with the same data should be equal");
        assertEquals(game1.hashCode(), game2.hashCode(), "Hash codes should match for equal Game records");
    }

    @Test
    void testEqualsAndHashCodeForLichessProfileResponse() {
        // Arrange
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(BLITZ_GAME_ELO, GAME_1_AMOUNT_GAMES,
                false);

        LichessProfileResponse profile1 = new LichessProfileResponse(USER_NAME, LICHESS_USER_NAME,
                LICHESS_PROFILE_LAST_UPDATE, blitzGame, blitzGame, blitzGame, blitzGame, blitzGame);

        LichessProfileResponse profile2 = new LichessProfileResponse(USER_NAME, LICHESS_USER_NAME,
                LICHESS_PROFILE_LAST_UPDATE, blitzGame, blitzGame, blitzGame, blitzGame, blitzGame);

        // Act & Assert
        assertEquals(profile1, profile2, "Profiles with the same data should be equal");
        assertEquals(profile1.hashCode(), profile2.hashCode(), "Hash codes should match for equal profiles");
    }

    @Test
    void testGameHandlesEdgeCases() {
        // Arrange
        LichessProfileResponse.Game game = new LichessProfileResponse.Game(0, 0, null);

        // Act & Assert
        assertEquals(0, game.elo());
        assertEquals(0, game.amountOfGames());
        assertNull(game.isProvisional(), "Provisional should be null if not provided");
    }

    @Test
    void testGameRecordCreation() {
        // Act
        LichessProfileResponse.Game game = new LichessProfileResponse.Game(OTHER_GAME_ELO, GAME_1_AMOUNT_GAMES, true);

        // Assert
        assertEquals(OTHER_GAME_ELO, game.elo());
        assertEquals(GAME_1_AMOUNT_GAMES, game.amountOfGames());
        assertTrue(game.isProvisional());
    }

    @Test
    void testLichessProfileResponseCreation() {
        // Arrange
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(BLITZ_GAME_ELO, GAME_1_AMOUNT_GAMES,
                false);
        LichessProfileResponse.Game bulletGame = new LichessProfileResponse.Game(BULLET_GAME_ELO, GAME_1_AMOUNT_GAMES,
                true);
        LichessProfileResponse.Game rapidGame = new LichessProfileResponse.Game(RAPID_GAME_ELO, GAME_1_AMOUNT_GAMES,
                false);
        LichessProfileResponse.Game classicalGame = new LichessProfileResponse.Game(CLASSICAL_GAME_ELO,
                GAME_1_AMOUNT_GAMES, false);
        LichessProfileResponse.Game puzzleGame = new LichessProfileResponse.Game(PUZZLE_GAME_ELO, GAME_1_AMOUNT_GAMES,
                false);

        // Act
        LichessProfileResponse profile = new LichessProfileResponse(USER_NAME, LICHESS_USER_NAME,
                LICHESS_PROFILE_LAST_UPDATE, blitzGame, bulletGame, rapidGame, classicalGame, puzzleGame);

        // Assert
        assertEquals(USER_NAME, profile.userName());
        assertEquals(LICHESS_USER_NAME, profile.lichessUserName());
        assertEquals(LICHESS_PROFILE_LAST_UPDATE, profile.lastUpdate());
        assertEquals(blitzGame, profile.blitzGame());
        assertEquals(bulletGame, profile.bulletGame());
        assertEquals(rapidGame, profile.rapidGame());
        assertEquals(classicalGame, profile.classicalGame());
        assertEquals(puzzleGame, profile.puzzleGame());
    }

    @Test
    void testLichessProfileResponseHandlesNullValues() {
        // Arrange
        LichessProfileResponse profile = new LichessProfileResponse(null, null, null, null, null, null, null, null);

        // Act & Assert
        assertNull(profile.userName(), "User name should be null");
        assertNull(profile.lichessUserName(), "Lichess user name should be null");
        assertNull(profile.lastUpdate(), "Last update should be null");
        assertNull(profile.blitzGame(), "Blitz game should be null");
        assertNull(profile.bulletGame(), "Bullet game should be null");
        assertNull(profile.rapidGame(), "Rapid game should be null");
        assertNull(profile.classicalGame(), "Classical game should be null");
        assertNull(profile.puzzleGame(), "Puzzle game should be null");
    }

    @Test
    void testToStringForLichessProfileResponse() {
        // Arrange
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(BLITZ_GAME_ELO, GAME_1_AMOUNT_GAMES,
                false);
        LichessProfileResponse profile = new LichessProfileResponse(USER_NAME, LICHESS_USER_NAME,
                LICHESS_PROFILE_LAST_UPDATE, blitzGame, blitzGame, blitzGame, blitzGame, blitzGame);

        // Act
        String profileString = profile.toString();

        // Assert
        assertTrue(profileString.contains(USER_NAME), "ToString should include the user name");
        assertTrue(profileString.contains(LICHESS_USER_NAME), "ToString should include the Lichess user name");
        assertTrue(profileString.contains(LICHESS_PROFILE_LAST_UPDATE.toString()),
                "ToString should include the last update");
    }
}
