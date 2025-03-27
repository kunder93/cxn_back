
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.lichess.LichessProfileListResponse;
import es.org.cxn.backapp.model.form.responses.lichess.LichessProfileResponse;

/**
 * Unit tests for the LichessProfileListResponse class. This test class ensures
 * the proper functionality of the LichessProfileListResponse object, validating
 * its handling of game-related attributes and responses in a list format.
 */
class LichessProfileListResponseTest {

    /**
     * Constant representing the Elo rating for Game 1. Used to test the accuracy of
     * Elo ratings for the first game type in the response.
     */
    private static final int GAME_1_ELO = 1500;

    /**
     * Constant representing the number of games played for Game 1. Used to test the
     * total number of games played for the first game type in the response.
     */
    private static final int GAME_1_AMOUNT_GAMES = 200;

    /**
     * Constant representing the Elo rating for Game 2. Used to test the accuracy of
     * Elo ratings for the second game type in the response.
     */
    private static final int GAME_2_ELO = 1400;

    /**
     * Constant representing the number of games played for Game 2. Used to test the
     * total number of games played for the second game type in the response.
     */
    private static final int GAME_2_AMOUT_GAMES = 150;

    @Test
    void testConstructorWithEmptyList() {
        LichessProfileListResponse response = new LichessProfileListResponse(List.of());

        // Ensure the list is empty
        assertNotNull(response.profilesList());
        assertTrue(response.profilesList().isEmpty());
    }

    @Test
    void testConstructorWithLargeList() {
        final int amountOfProfiles = 4;
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(GAME_1_ELO, GAME_1_AMOUNT_GAMES, false);
        List<LichessProfileResponse> profiles = List.of(
                new LichessProfileResponse("John Doe", "john_doe", LocalDateTime.now(), blitzGame, null, null, null,
                        null),
                new LichessProfileResponse("Jane Smith", "jane_smith", LocalDateTime.now(), blitzGame, null, null, null,
                        null),
                new LichessProfileResponse("Alice Brown", "alice_brown", LocalDateTime.now(), blitzGame, null, null,
                        null, null),
                new LichessProfileResponse("Bob White", "bob_white", LocalDateTime.now(), blitzGame, null, null, null,
                        null));

        LichessProfileListResponse response = new LichessProfileListResponse(profiles);

        // Ensure the list has the expected number of elements
        assertNotNull(response.profilesList());
        assertEquals(amountOfProfiles, response.profilesList().size());
    }

    @Test
    void testConstructorWithNonNullList() {
        final int amountOfProfiles = 2;
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(GAME_1_ELO, GAME_1_AMOUNT_GAMES, false);
        LichessProfileResponse.Game bulletGame = new LichessProfileResponse.Game(GAME_2_ELO, GAME_2_AMOUT_GAMES, true);

        // Creating LichessProfileResponse instances
        LichessProfileResponse profile1 = new LichessProfileResponse("John Doe", "john_doe", LocalDateTime.now(),
                blitzGame, bulletGame, null, null, null);
        LichessProfileResponse profile2 = new LichessProfileResponse("Jane Smith", "jane_smith", LocalDateTime.now(),
                blitzGame, null, null, null, null);

        List<LichessProfileResponse> profiles = List.of(profile1, profile2);

        LichessProfileListResponse response = new LichessProfileListResponse(profiles);

        // Ensure the list contains the correct profiles
        assertNotNull(response.profilesList());
        assertEquals(amountOfProfiles, response.profilesList().size());
        assertEquals("john_doe", response.profilesList().get(0).lichessUserName());
        assertEquals(GAME_1_ELO, response.profilesList().get(0).blitzGame().elo());
        assertEquals("jane_smith", response.profilesList().get(1).lichessUserName());
    }

    @Test
    void testConstructorWithNullList() {
        LichessProfileListResponse response = new LichessProfileListResponse(null);

        // Ensure the list is not null and is empty
        assertNotNull(response.profilesList());
        assertTrue(response.profilesList().isEmpty());
    }

    @Test
    void testListImmutability() {
        final int amountOfProfiles = 1;
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(GAME_1_ELO, GAME_1_AMOUNT_GAMES, false);
        LichessProfileResponse profile1 = new LichessProfileResponse("John Doe", "john_doe", LocalDateTime.now(),
                blitzGame, null, null, null, null);
        List<LichessProfileResponse> profiles = List.of(profile1);
        LichessProfileListResponse response = new LichessProfileListResponse(profiles);

        // Create a mutable copy of the list
        List<LichessProfileResponse> copiedList = new ArrayList<>(response.profilesList());

        // Now modify the mutable copy
        copiedList.add(new LichessProfileResponse("Jane Smith", "jane_smith", LocalDateTime.now(), blitzGame, null,
                null, null, null));

        // Ensure the original list inside response is unchanged
        assertEquals(amountOfProfiles, response.profilesList().size()); // Original list should still have 1 item
        assertEquals("john_doe", response.profilesList().get(0).lichessUserName()); // Verify the original profile
    }

}
