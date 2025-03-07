package es.org.cxn.backapp.test.unit.entity;

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

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;

/**
 * Unit tests for the {@link PersistentOAuthAuthorizationRequestEntity} class.
 *
 * This test class verifies the behavior of the methods in the
 * {@link PersistentOAuthAuthorizationRequestEntity} class, including getter and
 * setter functionality for its properties such as code verifier, created
 * timestamp, state, and user DNI.
 *
 * @author Santiago Paz
 */
class PersistentOAuthAuthorizationRequestEntityTest {

    /**
     * The {@link PersistentOAuthAuthorizationRequestEntity} instance to be tested.
     */
    private PersistentOAuthAuthorizationRequestEntity authRequestEntity;

    /**
     * Initializes the test setup by creating a new
     * {@link PersistentOAuthAuthorizationRequestEntity} instance before each test.
     */
    @BeforeEach
    public void setUp() {
        authRequestEntity = new PersistentOAuthAuthorizationRequestEntity();
    }

    /**
     * Tests the initial value of the code verifier, which should be null.
     */
    @Test
    void testCodeVerifierIsNullInitially() {
        assertNull(authRequestEntity.getCodeVerifier(), "Code verifier should be null initially.");
    }

    /**
     * Tests the initial value of the created timestamp, which should be null.
     */
    @Test
    void testCreatedAtIsNullInitially() {
        assertNull(authRequestEntity.getCreatedAt(), "Created at should be null initially.");
    }

    /**
     * Tests the setter and getter for the code verifier. Verifies that the code
     * verifier is correctly set and retrieved.
     */
    @Test
    void testSetAndGetCodeVerifier() {
        String testCodeVerifier = "testCodeVerifier";
        authRequestEntity.setCodeVerifier(testCodeVerifier);
        assertEquals(testCodeVerifier, authRequestEntity.getCodeVerifier(),
                "Code verifier should match the set value.");
    }

    /**
     * Tests the setter and getter for the created timestamp. Verifies that the
     * created timestamp is correctly set and retrieved.
     */
    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        authRequestEntity.setCreatedAt(now);
        assertEquals(now, authRequestEntity.getCreatedAt(), "Created at should match the set value.");
    }

    /**
     * Tests the setter and getter for the state. Verifies that the state is
     * correctly set and retrieved.
     */
    @Test
    void testSetAndGetState() {
        String testState = "testState";
        authRequestEntity.setState(testState);
        assertEquals(testState, authRequestEntity.getState(), "State should match the set value.");
    }

    /**
     * Tests the setter and getter for the user DNI. Verifies that the user DNI is
     * correctly set and retrieved.
     */
    @Test
    void testSetAndGetUserDni() {
        String testUserDni = "user123";
        authRequestEntity.setUserDni(testUserDni);
        assertEquals(testUserDni, authRequestEntity.getUserDni(), "User DNI should match the set value.");
    }

    /**
     * Tests the initial value of the state, which should be null.
     */
    @Test
    void testStateIsNullInitially() {
        assertNull(authRequestEntity.getState(), "State should be null initially.");
    }

    /**
     * Tests the initial value of the user DNI, which should be null.
     */
    @Test
    void testUserDniIsNullInitially() {
        assertNull(authRequestEntity.getUserDni(), "User DNI should be null initially.");
    }
}
