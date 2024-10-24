package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

class PersistentLichessAuthEntityTest {
    private PersistentLichessAuthEntity authEntity;

    @BeforeEach
    public void setUp() {
        authEntity = new PersistentLichessAuthEntity();
    }

    @Test
    void testSetAndGetAccessToken() {
        String testAccessToken = "testAccessToken";
        authEntity.setAccessToken(testAccessToken);
        assertEquals(testAccessToken, authEntity.getAccessToken(), "Access token should match the set value.");
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        authEntity.setCreatedAt(now);
        assertEquals(now, authEntity.getCreatedAt(), "Created at should match the set value.");
    }

    @Test
    void testSetAndGetExpirationDate() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        authEntity.setExpirationDate(expirationDate);
        assertEquals(expirationDate, authEntity.getExpirationDate(), "Expiration date should match the set value.");
    }

    @Test
    void testSetAndGetScope() {
        String testScope = "read write";
        authEntity.setScope(testScope);
        assertEquals(testScope, authEntity.getScope(), "Scope should match the set value.");
    }

    @Test
    void testSetAndGetState() {
        String testState = "state123";
        authEntity.setState(testState);
        assertEquals(testState, authEntity.getState(), "State should match the set value.");
    }

    @Test
    void testSetAndGetTokenType() {
        String testTokenType = "Bearer";
        authEntity.setTokenType(testTokenType);
        assertEquals(testTokenType, authEntity.getTokenType(), "Token type should match the set value.");
    }

    @Test
    void testSetAndGetUser() {
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setDni("user123"); // Assuming there is a setDni method in PersistentUserEntity
        authEntity.setUser(userEntity);
        assertEquals(userEntity, authEntity.getUser(), "User should match the set value.");
    }

    @Test
    void testSetAndGetUserDni() {
        String testUserDni = "user123";
        authEntity.setUserDni(testUserDni);
        assertEquals(testUserDni, authEntity.getUserDni(), "User DNI should match the set value.");
    }

    @Test
    void testUserDniIsNullInitially() {
        assertNull(authEntity.getUserDni(), "User DNI should be null initially.");
    }

    @Test
    void testUserIsNullInitially() {
        assertNull(authEntity.getUser(), "User should be null initially.");
    }
}
