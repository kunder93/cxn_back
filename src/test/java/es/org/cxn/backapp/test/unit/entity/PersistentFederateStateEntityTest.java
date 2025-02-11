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

    /**
     * The test DNI (Document National Identity) for a user.
     */
    private static final String TEST_USER_DNI = "1234567890";

    /**
     * The URL for the front image of the user's DNI.
     */
    private static final String FRONT_IMAGE_URL = "http://example.com/front.jpg";

    /**
     * The URL for the back image of the user's DNI.
     */
    private static final String BACK_IMAGE_URL = "http://example.com/back.jpg";

    /**
     * The flag indicating if the user's federation has automatic renewal enabled.
     */
    private static final boolean AUTOMATIC_RENEWAL = true;

    /**
     * The date of the last update for the federate state.
     */
    private static final LocalDate LAST_UPDATE = LocalDate.of(2025, 1, 1);

    /**
     * The current federate state of the user.
     */
    private static final FederateState STATE = FederateState.FEDERATE;

    /**
     * Instance of {@link PersistentFederateStateEntity} to be tested.
     */
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
        PersistentFederateStateEntity entity1 = new PersistentFederateStateEntity(TEST_USER_DNI, FRONT_IMAGE_URL,
                BACK_IMAGE_URL, AUTOMATIC_RENEWAL, LAST_UPDATE, STATE);

        PersistentFederateStateEntity entity2 = new PersistentFederateStateEntity(TEST_USER_DNI, FRONT_IMAGE_URL,
                BACK_IMAGE_URL, AUTOMATIC_RENEWAL, LAST_UPDATE, STATE);

        // Act & Assert
        assertEquals(entity1, entity2, "Entities with the same data should be equal.");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes of equal entities should match.");
    }

    @Test
    void testParameterizedConstructor() {
        // Act
        PersistentFederateStateEntity paramEntity = new PersistentFederateStateEntity(TEST_USER_DNI, FRONT_IMAGE_URL,
                BACK_IMAGE_URL, AUTOMATIC_RENEWAL, LAST_UPDATE, STATE);

        // Assert
        assertEquals(TEST_USER_DNI, paramEntity.getUserDni(), "userDni should match the constructor value.");
        assertEquals(FRONT_IMAGE_URL, paramEntity.getDniFrontImageUrl(),
                "dniFrontImageUrl should match the constructor value.");
        assertEquals(BACK_IMAGE_URL, paramEntity.getDniBackImageUrl(),
                "dniBackImageUrl should match the constructor value.");
        assertTrue(paramEntity.isAutomaticRenewal(), "automaticRenewal should match the constructor value.");
        assertEquals(LAST_UPDATE, paramEntity.getDniLastUpdate(), "dniLastUpdate should match the constructor value.");
        assertEquals(STATE, paramEntity.getState(), "state should match the constructor value.");
    }

    @Test
    void testSettersAndGetters() {
        // Act
        entity.setUserDni(TEST_USER_DNI);
        entity.setDniFrontImageUrl(FRONT_IMAGE_URL);
        entity.setDniBackImageUrl(BACK_IMAGE_URL);
        entity.setAutomaticRenewal(false);
        entity.setDniLastUpdate(LocalDate.of(2025, 1, 1));
        entity.setState(FederateState.NO_FEDERATE);

        // Assert
        assertEquals(TEST_USER_DNI, entity.getUserDni(), "getUserDni should return the set value.");
        assertEquals(FRONT_IMAGE_URL, entity.getDniFrontImageUrl(), "getDniFrontImageUrl should return the set value.");
        assertEquals(BACK_IMAGE_URL, entity.getDniBackImageUrl(), "getDniBackImageUrl should return the set value.");
        assertFalse(entity.isAutomaticRenewal(), "isAutomaticRenewal should return the set value.");
        assertEquals(LocalDate.of(2025, 1, 1), entity.getDniLastUpdate(),
                "getDniLastUpdate should return the set value.");
        assertEquals(FederateState.NO_FEDERATE, entity.getState(), "getState should return the set value.");
    }

    @Test
    void testToString() {
        // Act
        entity.setUserDni(TEST_USER_DNI);
        entity.setDniFrontImageUrl(FRONT_IMAGE_URL);
        entity.setDniBackImageUrl(BACK_IMAGE_URL);
        entity.setAutomaticRenewal(AUTOMATIC_RENEWAL);
        entity.setDniLastUpdate(LAST_UPDATE);
        entity.setState(STATE);

        String result = entity.toString();

        // Assert
        assertNotNull(result, "toString should not return null.");
        assertTrue(result.contains(TEST_USER_DNI), "toString should include userDni.");
        assertTrue(result.contains(FRONT_IMAGE_URL), "toString should include dniFrontImageUrl.");
        assertTrue(result.contains(BACK_IMAGE_URL), "toString should include dniBackImageUrl.");
        assertTrue(result.contains(String.valueOf(AUTOMATIC_RENEWAL)), "toString should include automaticRenewal.");
        assertTrue(result.contains(LAST_UPDATE.toString()), "toString should include dniLastUpdate.");
        assertTrue(result.contains(STATE.toString()), "toString should include state.");
    }
}
