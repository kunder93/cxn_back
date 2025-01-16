
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

class LichessProfileResponseTest {
    @Test
    void testEqualsAndHashCodeForGame() {
        // Arrange
        LichessProfileResponse.Game game1 = new LichessProfileResponse.Game(1800, 50, false);
        LichessProfileResponse.Game game2 = new LichessProfileResponse.Game(1800, 50, false);

        // Act & Assert
        assertEquals(game1, game2, "Game records with the same data should be equal");
        assertEquals(game1.hashCode(), game2.hashCode(), "Hash codes should match for equal Game records");
    }

    @Test
    void testEqualsAndHashCodeForLichessProfileResponse() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(2000, 50, false);

        LichessProfileResponse profile1 = new LichessProfileResponse("John Doe", "JD123", now, blitzGame, blitzGame,
                blitzGame, blitzGame, blitzGame);

        LichessProfileResponse profile2 = new LichessProfileResponse("John Doe", "JD123", now, blitzGame, blitzGame,
                blitzGame, blitzGame, blitzGame);

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
        LichessProfileResponse.Game game = new LichessProfileResponse.Game(1500, 25, true);

        // Assert
        assertEquals(1500, game.elo());
        assertEquals(25, game.amountOfGames());
        assertTrue(game.isProvisional());
    }

    @Test
    void testLichessProfileResponseCreation() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(2000, 50, false);
        LichessProfileResponse.Game bulletGame = new LichessProfileResponse.Game(1900, 100, true);
        LichessProfileResponse.Game rapidGame = new LichessProfileResponse.Game(2100, 30, false);
        LichessProfileResponse.Game classicalGame = new LichessProfileResponse.Game(1800, 10, false);
        LichessProfileResponse.Game puzzleGame = new LichessProfileResponse.Game(2200, 200, false);

        // Act
        LichessProfileResponse profile = new LichessProfileResponse("John Doe", "JD123", now, blitzGame, bulletGame,
                rapidGame, classicalGame, puzzleGame);

        // Assert
        assertEquals("John Doe", profile.userName());
        assertEquals("JD123", profile.lichessUserName());
        assertEquals(now, profile.lastUpdate());
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
        LocalDateTime now = LocalDateTime.now();
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(2000, 50, false);
        LichessProfileResponse profile = new LichessProfileResponse("John Doe", "JD123", now, blitzGame, blitzGame,
                blitzGame, blitzGame, blitzGame);

        // Act
        String profileString = profile.toString();

        // Assert
        assertTrue(profileString.contains("John Doe"), "ToString should include the user name");
        assertTrue(profileString.contains("JD123"), "ToString should include the Lichess user name");
        assertTrue(profileString.contains(now.toString()), "ToString should include the last update");
    }
}