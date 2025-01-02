package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentLichessAuthEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

/**
 * Unit tests for the {@link PersistentLichessAuthEntity} class.
 *
 * This test class verifies the behavior of the methods in the
 * {@link PersistentLichessAuthEntity} class, including getter and setter
 * functionality for its properties.
 *
 * @author Santiago Paz
 */
class PersistentLichessAuthEntityTest {

    /**
     * The {@link PersistentLichessAuthEntity} instance to be tested.
     */
    private PersistentLichessAuthEntity authEntity;

    /**
     * Initializes the test setup by creating a new
     * {@link PersistentLichessAuthEntity} instance before each test.
     */
    @BeforeEach
    public void setUp() {
        authEntity = new PersistentLichessAuthEntity();
    }

    /**
     * Tests the setter and getter for the access token. Verifies that the access
     * token is correctly set and retrieved.
     */
    @Test
    void testSetAndGetAccessToken() {
        String testAccessToken = "testAccessToken";
        authEntity.setAccessToken(testAccessToken);
        assertEquals(testAccessToken, authEntity.getAccessToken(), "Access token should match the set value.");
    }

    /**
     * Tests the setter and getter for the created date. Verifies that the created
     * date is correctly set and retrieved.
     */
    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        authEntity.setCreatedAt(now);
        assertEquals(now, authEntity.getCreatedAt(), "Created at should match the set value.");
    }

    /**
     * Tests the setter and getter for the expiration date. Verifies that the
     * expiration date is correctly set and retrieved.
     */
    @Test
    void testSetAndGetExpirationDate() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        authEntity.setExpirationDate(expirationDate);
        assertEquals(expirationDate, authEntity.getExpirationDate(), "Expiration date should match the set value.");
    }

    /**
     * Tests the setter and getter for the scope. Verifies that the scope is
     * correctly set and retrieved.
     */
    @Test
    void testSetAndGetScope() {
        String testScope = "read write";
        authEntity.setScope(testScope);
        assertEquals(testScope, authEntity.getScope(), "Scope should match the set value.");
    }

    /**
     * Tests the setter and getter for the state. Verifies that the state is
     * correctly set and retrieved.
     */
    @Test
    void testSetAndGetState() {
        String testState = "state123";
        authEntity.setState(testState);
        assertEquals(testState, authEntity.getState(), "State should match the set value.");
    }

    /**
     * Tests the setter and getter for the token type. Verifies that the token type
     * is correctly set and retrieved.
     */
    @Test
    void testSetAndGetTokenType() {
        String testTokenType = "Bearer";
        authEntity.setTokenType(testTokenType);
        assertEquals(testTokenType, authEntity.getTokenType(), "Token type should match the set value.");
    }

    /**
     * Tests the setter and getter for the user entity. Verifies that the user
     * entity is correctly set and retrieved.
     */
    @Test
    void testSetAndGetUser() {
        PersistentUserEntity userEntity = new PersistentUserEntity();
        userEntity.setDni("user123"); // Assuming there is a setDni method in PersistentUserEntity
        authEntity.setUser(userEntity);
        assertEquals(userEntity, authEntity.getUser(), "User should match the set value.");
    }

    /**
     * Tests the setter and getter for the user DNI. Verifies that the user DNI is
     * correctly set and retrieved.
     */
    @Test
    void testSetAndGetUserDni() {
        String testUserDni = "user123";
        authEntity.setUserDni(testUserDni);
        assertEquals(testUserDni, authEntity.getUserDni(), "User DNI should match the set value.");
    }

    /**
     * Tests the initial value of the user DNI, which should be null.
     */
    @Test
    void testUserDniIsNullInitially() {
        assertNull(authEntity.getUserDni(), "User DNI should be null initially.");
    }

    /**
     * Tests the initial value of the user entity, which should be null.
     */
    @Test
    void testUserIsNullInitially() {
        assertNull(authEntity.getUser(), "User should be null initially.");
    }
}
