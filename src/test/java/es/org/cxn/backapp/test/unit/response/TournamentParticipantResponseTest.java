
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.TournamentParticipantResponse;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

class TournamentParticipantResponseTest {
    /**
     * The participant birth date.
     */
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);

    /**
     * The participant Fide id.
     */
    private static final BigInteger FIDE_ID = new BigInteger("12345");

    @Test
    void testDifferentTournamentCategories() {

        String name = "John Doe";
        String club = "Chess Club";

        TournamentCategory categorySUB10 = TournamentCategory.SUB10;
        TournamentCategory categorySUB12 = TournamentCategory.SUB12;
        String byes = "2";

        TournamentParticipantResponse responseSUB10 = new TournamentParticipantResponse(FIDE_ID, name, club, BIRTH_DATE,
                categorySUB10, byes);
        TournamentParticipantResponse responseSUB12 = new TournamentParticipantResponse(FIDE_ID, name, club, BIRTH_DATE,
                categorySUB12, byes);

        // Ensure that different categories are handled correctly
        assertNotEquals(responseSUB10, responseSUB12);
        assertEquals(TournamentCategory.SUB10, responseSUB10.category());
        assertEquals(TournamentCategory.SUB12, responseSUB12.category());
    }
}
