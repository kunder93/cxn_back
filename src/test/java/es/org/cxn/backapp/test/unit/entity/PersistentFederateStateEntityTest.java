
package es.org.cxn.backapp.test.unit.entity;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

/**
 * Unit tests for {@link PersistentFederateStateEntity}.
 */
class PersistentFederateStateEntityTest {

    private PersistentFederateStateEntity entity;

    @BeforeEach
    void setUp() {
        entity = new PersistentFederateStateEntity();
    }

    @Test
    void testAssociationSettersAndGetters() {
        // Arrange
        PersistentUserEntity user = new PersistentUserEntity();
        PersistentPaymentsEntity payment = new PersistentPaymentsEntity();

        // Act
        entity.setUser(user);
        entity.setPayment(payment);

        // Assert
        assertEquals(user, entity.getUser(), "getUser should return the associated user.");
        assertEquals(payment, entity.getPayment(), "getPayment should return the associated payment.");
    }

    @Test
    void testDefaultConstructor() {
        // Verify default constructor initializes fields to default values
        assertNull(entity.getUserDni(), "Default userDni should be null.");
        assertNull(entity.getDniFrontImageUrl(), "Default dniFrontImageUrl should be null.");
        assertNull(entity.getDniBackImageUrl(), "Default dniBackImageUrl should be null.");
        assertFalse(entity.isAutomaticRenewal(), "Default automaticRenewal should be false.");
        assertNull(entity.getDniLastUpdate(), "Default dniLastUpdate should be null.");
        assertNull(entity.getState(), "Default state should be null.");
        assertNull(entity.getUser(), "Default user should be null.");
        assertNull(entity.getPayment(), "Default payment should be null.");
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        PersistentFederateStateEntity entity1 = new PersistentFederateStateEntity("1234567890",
                "http://example.com/front.jpg", "http://example.com/back.jpg", true, LocalDate.of(2025, 1, 1),
                FederateState.FEDERATE);

        PersistentFederateStateEntity entity2 = new PersistentFederateStateEntity("1234567890",
                "http://example.com/front.jpg", "http://example.com/back.jpg", true, LocalDate.of(2025, 1, 1),
                FederateState.FEDERATE);

        // Act & Assert
        assertEquals(entity1, entity2, "Entities with the same data should be equal.");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes of equal entities should match.");
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String userDni = "1234567890";
        String frontImageUrl = "http://example.com/dni-front.jpg";
        String backImageUrl = "http://example.com/dni-back.jpg";
        boolean isAutomaticRenewal = true;
        LocalDate lastUpdate = LocalDate.now();
        FederateState state = FederateState.FEDERATE;

        // Act
        PersistentFederateStateEntity paramEntity = new PersistentFederateStateEntity(userDni, frontImageUrl,
                backImageUrl, isAutomaticRenewal, lastUpdate, state);

        // Assert
        assertEquals(userDni, paramEntity.getUserDni(), "userDni should match the constructor value.");
        assertEquals(frontImageUrl, paramEntity.getDniFrontImageUrl(),
                "dniFrontImageUrl should match the constructor value.");
        assertEquals(backImageUrl, paramEntity.getDniBackImageUrl(),
                "dniBackImageUrl should match the constructor value.");
        assertTrue(paramEntity.isAutomaticRenewal(), "automaticRenewal should match the constructor value.");
        assertEquals(lastUpdate, paramEntity.getDniLastUpdate(), "dniLastUpdate should match the constructor value.");
        assertEquals(state, paramEntity.getState(), "state should match the constructor value.");
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        String userDni = "0987654321";
        String frontImageUrl = "http://example.com/front.jpg";
        String backImageUrl = "http://example.com/back.jpg";
        boolean isAutomaticRenewal = false;
        LocalDate lastUpdate = LocalDate.of(2025, 1, 1);
        FederateState state = FederateState.NO_FEDERATE;

        // Act
        entity.setUserDni(userDni);
        entity.setDniFrontImageUrl(frontImageUrl);
        entity.setDniBackImageUrl(backImageUrl);
        entity.setAutomaticRenewal(isAutomaticRenewal);
        entity.setDniLastUpdate(lastUpdate);
        entity.setState(state);

        // Assert
        assertEquals(userDni, entity.getUserDni(), "getUserDni should return the set value.");
        assertEquals(frontImageUrl, entity.getDniFrontImageUrl(), "getDniFrontImageUrl should return the set value.");
        assertEquals(backImageUrl, entity.getDniBackImageUrl(), "getDniBackImageUrl should return the set value.");
        assertFalse(entity.isAutomaticRenewal(), "isAutomaticRenewal should return the set value.");
        assertEquals(lastUpdate, entity.getDniLastUpdate(), "getDniLastUpdate should return the set value.");
        assertEquals(state, entity.getState(), "getState should return the set value.");
    }

    @Test
    void testToString() {
        // Arrange
        entity.setUserDni("1234567890");
        entity.setDniFrontImageUrl("http://example.com/front.jpg");
        entity.setDniBackImageUrl("http://example.com/back.jpg");
        entity.setAutomaticRenewal(true);
        entity.setDniLastUpdate(LocalDate.of(2025, 1, 1));
        entity.setState(FederateState.FEDERATE);

        // Act
        String result = entity.toString();

        // Assert
        assertNotNull(result, "toString should not return null.");
        assertTrue(result.contains("1234567890"), "toString should include userDni.");
        assertTrue(result.contains("http://example.com/front.jpg"), "toString should include dniFrontImageUrl.");
        assertTrue(result.contains("http://example.com/back.jpg"), "toString should include dniBackImageUrl.");
        assertTrue(result.contains("true"), "toString should include automaticRenewal.");
        assertTrue(result.contains("2025-01-01"), "toString should include dniLastUpdate.");
        assertTrue(result.contains("FEDERATE"), "toString should include state.");
    }
}
