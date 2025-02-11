
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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.FederateStateEntity;
import es.org.cxn.backapp.model.form.responses.FederateStateResponse;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;

class FederateStateResponseTest {

    /** Sample date: December 31, 2025. */
    private static final LocalDate DATE_2025_DEC_31 = LocalDate.of(2025, 12, 31);

    /** Sample date: January 1, 2025. */
    private static final LocalDate DATE_2025_JAN_1 = LocalDate.of(2025, 1, 1);

    @Test
    void testConstructorNullValues() {
        // Given
        FederateState state = null;
        Boolean autoRenew = null;
        LocalDate dniLastUpdate = null;

        // When
        FederateStateResponse response = new FederateStateResponse(state, autoRenew, dniLastUpdate);

        // Then
        assertEquals(null, response.state());
        assertEquals(null, response.autoRenew());
        assertEquals(null, response.dniLastUpdate());
    }

    @Test
    void testConstructorWithEntity() {
        // Given
        FederateStateEntity entity = new PersistentFederateStateEntity();
        entity.setState(FederateState.NO_FEDERATE);
        entity.setAutomaticRenewal(false);
        entity.setDniLastUpdate(DATE_2025_DEC_31);

        // When
        FederateStateResponse response = new FederateStateResponse(entity);

        // Then
        assertEquals(entity.getState(), response.state());
        assertEquals(entity.isAutomaticRenewal(), response.autoRenew());
        assertEquals(DATE_2025_DEC_31, response.dniLastUpdate());
    }

    @Test
    void testConstructorWithEntityNullValues() {
        // Given
        FederateStateEntity entity = new PersistentFederateStateEntity();
        entity.setState(null);
        entity.setDniLastUpdate(null);

        // When
        FederateStateResponse response = new FederateStateResponse(entity);

        // Then
        assertEquals(null, response.state(), "state is null");
        assertEquals(Boolean.FALSE, response.autoRenew(), "when not initialized is false.");
        assertEquals(null, response.dniLastUpdate(), "dni last update is null");
    }

    @Test
    void testConstructorWithValidValues() {
        // Given
        FederateState state = FederateState.FEDERATE;
        Boolean autoRenew = true;

        // When
        FederateStateResponse response = new FederateStateResponse(state, autoRenew, DATE_2025_JAN_1);

        // Then
        assertEquals(state, response.state());
        assertEquals(autoRenew, response.autoRenew());
        assertEquals(DATE_2025_JAN_1, response.dniLastUpdate());
    }
}
