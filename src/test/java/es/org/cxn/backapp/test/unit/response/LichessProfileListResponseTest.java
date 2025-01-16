
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

import es.org.cxn.backapp.model.form.responses.LichessProfileListResponse;
import es.org.cxn.backapp.model.form.responses.LichessProfileResponse;

class LichessProfileListResponseTest {
    @Test
    void testConstructorWithEmptyList() {
        LichessProfileListResponse response = new LichessProfileListResponse(List.of());

        // Ensure the list is empty
        assertNotNull(response.profilesList());
        assertTrue(response.profilesList().isEmpty());
    }

    @Test
    void testConstructorWithLargeList() {
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(1500, 200, false);
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
        assertEquals(4, response.profilesList().size());
    }

    @Test
    void testConstructorWithNonNullList() {
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(1500, 200, false);
        LichessProfileResponse.Game bulletGame = new LichessProfileResponse.Game(1400, 150, true);

        // Creating LichessProfileResponse instances
        LichessProfileResponse profile1 = new LichessProfileResponse("John Doe", "john_doe", LocalDateTime.now(),
                blitzGame, bulletGame, null, null, null);
        LichessProfileResponse profile2 = new LichessProfileResponse("Jane Smith", "jane_smith", LocalDateTime.now(),
                blitzGame, null, null, null, null);

        List<LichessProfileResponse> profiles = List.of(profile1, profile2);

        LichessProfileListResponse response = new LichessProfileListResponse(profiles);

        // Ensure the list contains the correct profiles
        assertNotNull(response.profilesList());
        assertEquals(2, response.profilesList().size());
        assertEquals("john_doe", response.profilesList().get(0).lichessUserName());
        assertEquals(1500, response.profilesList().get(0).blitzGame().elo());
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
        // Creating Game instances with sample data
        LichessProfileResponse.Game blitzGame = new LichessProfileResponse.Game(1500, 200, false);
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
        assertEquals(1, response.profilesList().size()); // Original list should still have 1 item
        assertEquals("john_doe", response.profilesList().get(0).lichessUserName()); // Verify the original profile
    }

}
