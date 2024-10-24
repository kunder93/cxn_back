package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentOAuthAuthorizationRequestEntity;

class PersistentOAuthAuthorizationRequestEntityTest {
    private PersistentOAuthAuthorizationRequestEntity authRequestEntity;

    @BeforeEach
    public void setUp() {
        authRequestEntity = new PersistentOAuthAuthorizationRequestEntity();
    }

    @Test
    void testCodeVerifierIsNullInitially() {
        assertNull(authRequestEntity.getCodeVerifier(), "Code verifier should be null initially.");
    }

    @Test
    void testCreatedAtIsNullInitially() {
        assertNull(authRequestEntity.getCreatedAt(), "Created at should be null initially.");
    }

    @Test
    void testSetAndGetCodeVerifier() {
        String testCodeVerifier = "testCodeVerifier";
        authRequestEntity.setCodeVerifier(testCodeVerifier);
        assertEquals(testCodeVerifier, authRequestEntity.getCodeVerifier(),
                "Code verifier should match the set value.");
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        authRequestEntity.setCreatedAt(now);
        assertEquals(now, authRequestEntity.getCreatedAt(), "Created at should match the set value.");
    }

    @Test
    void testSetAndGetState() {
        String testState = "testState";
        authRequestEntity.setState(testState);
        assertEquals(testState, authRequestEntity.getState(), "State should match the set value.");
    }

    @Test
    void testSetAndGetUserDni() {
        String testUserDni = "user123";
        authRequestEntity.setUserDni(testUserDni);
        assertEquals(testUserDni, authRequestEntity.getUserDni(), "User DNI should match the set value.");
    }

    @Test
    void testStateIsNullInitially() {
        assertNull(authRequestEntity.getState(), "State should be null initially.");
    }

    @Test
    void testUserDniIsNullInitially() {
        assertNull(authRequestEntity.getUserDni(), "User DNI should be null initially.");
    }
}
