
package es.org.cxn.backapp.test.unit.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

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
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList;
import es.org.cxn.backapp.model.form.responses.FederateStateExtendedResponseList.FederateStateExtendedResponse;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;

class FederateStateExtendedResponseListTest {

    @Test
    void testConstructorWithEmptyList() {
        // Given
        List<FederateStateExtendedResponse> emptyList = List.of();

        // When
        FederateStateExtendedResponseList responseList = new FederateStateExtendedResponseList(emptyList);

        // Then
        assertNotNull(responseList.federateStateMembersList());
        assertTrue(responseList.federateStateMembersList().isEmpty(), "The list should be empty");
    }

    @Test
    void testConstructorWithNonEmptyList() {
        // Given
        FederateStateExtendedResponse response1 = new FederateStateExtendedResponse("DNI1", FederateState.FEDERATE,
                true, LocalDate.now());
        FederateStateExtendedResponse response2 = new FederateStateExtendedResponse("DNI2", FederateState.NO_FEDERATE,
                false, LocalDate.now());
        List<FederateStateExtendedResponse> nonEmptyList = List.of(response1, response2);

        // When
        FederateStateExtendedResponseList responseList = new FederateStateExtendedResponseList(nonEmptyList);

        // Then
        assertNotNull(responseList.federateStateMembersList());
        assertEquals(2, responseList.federateStateMembersList().size(), "The list should contain 2 elements");
    }

    @Test
    void testConstructorWithNullList() {
        // Given
        List<FederateStateExtendedResponse> nullList = null;

        // When
        FederateStateExtendedResponseList responseList = new FederateStateExtendedResponseList(nullList);

        // Then
        assertNotNull(responseList.federateStateMembersList());
        assertTrue(responseList.federateStateMembersList().isEmpty(), "The list should be empty");
    }

    @Test
    void testFromEntities() {
        // Given
        PersistentFederateStateEntity entity1 = new PersistentFederateStateEntity("DNI1", "frontImageUrl1",
                "backImageUrl1", true, LocalDate.now(), FederateState.FEDERATE);
        PersistentFederateStateEntity entity2 = new PersistentFederateStateEntity("DNI2", "frontImageUrl2",
                "backImageUrl2", false, LocalDate.now(), FederateState.NO_FEDERATE);
        List<PersistentFederateStateEntity> entities = List.of(entity1, entity2);

        // When
        FederateStateExtendedResponseList responseList = FederateStateExtendedResponseList.fromEntities(entities);

        // Then
        assertNotNull(responseList.federateStateMembersList());
        assertEquals(2, responseList.federateStateMembersList().size(), "The list should contain 2 elements");
        assertEquals("DNI1", responseList.federateStateMembersList().get(0).dni(),
                "First element's DNI should be DNI1");
        assertEquals("DNI2", responseList.federateStateMembersList().get(1).dni(),
                "Second element's DNI should be DNI2");
    }

    @Test
    void testFromEntitiesWithEmptyList() {
        // Given
        List<PersistentFederateStateEntity> entities = List.of();

        // When
        FederateStateExtendedResponseList responseList = FederateStateExtendedResponseList.fromEntities(entities);

        // Then
        assertNotNull(responseList.federateStateMembersList());
        assertTrue(responseList.federateStateMembersList().isEmpty(), "The list should be empty");
    }

    @Test
    void testImmutableList() {
        // Given
        FederateStateExtendedResponse response1 = new FederateStateExtendedResponse("DNI1", FederateState.FEDERATE,
                true, LocalDate.now());
        List<FederateStateExtendedResponse> nonEmptyList = List.of(response1);
        FederateStateExtendedResponseList responseList = new FederateStateExtendedResponseList(nonEmptyList);

        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            responseList.federateStateMembersList().add(response1);
        });
    }
}
