
package es.org.cxn.backapp.test.unit.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.security.MyPrincipalUser;

import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Unit tests for the {@link DefaultJwtUtils} class.
 * <p>
 * These tests cover the generation, extraction, validation, and expiration
 * checks of JWT tokens.
 */
class JwtUtilsTest {

  /**
   * The user details is mocked for this test cases.
   */
  private MyPrincipalUser mockUserDetails;

  /**
   * Set up method to initialize mock objects before each test.
   */
  @BeforeEach
  void setUp() {
    // Use a fixed secret key for testing
    mockUserDetails = mock(MyPrincipalUser.class);
    when(mockUserDetails.getUsername()).thenReturn("test@example.com");
  }

  /**
   * Tear down method to clean up resources after each test.
   */
  @AfterEach
  public void tearDown() {
    // No specific resources to clean up in this implementation.
  }

  /**
   * Test the {@link DefaultJwtUtils#generateToken(UserDetails)} method to
   * ensure it generates a valid JWT token.
   */
  @Test
  void testGenerateToken() {
    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    assertNotNull(token, "Token should not be null");

    var extractedUsername = DefaultJwtUtils.extractUsername(token);
    assertEquals(
          "test@example.com", extractedUsername,
          "Extracted username should match the input username"
    );
  }

  /**
   * Test the {@link DefaultJwtUtils#extractUsername(String)} method to
   * ensure it correctly extracts the username from a token.
   */
  @Test
  void testExtractUsername() {
    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    var username = DefaultJwtUtils.extractUsername(token);
    assertEquals(
          "test@example.com", username,
          "Extracted username should match the expected value"
    );
  }

  /**
   * Test the {@link DefaultJwtUtils#extractExpiration(String)} method to
   * ensure it correctly extracts the expiration date from a token.
   */
  @Test
  void testExtractExpiration() {
    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    var expiration = DefaultJwtUtils.extractExpiration(token);
    assertTrue(
          expiration.toInstant().isAfter(Instant.now()),
          "Expiration date should be in the future"
    );
  }

  /**
   * Test the {@link DefaultJwtUtils#isTokenExpired(String)} method to ensure
   * it correctly identifies if a token has expired.
   */
  @Test
  void testIsTokenExpired() {
    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    boolean isExpired = DefaultJwtUtils.isTokenExpired(token);
    assertFalse(
          isExpired, "Token should not be expired immediately after generation"
    );
  }

  /**
   * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)}
   * method to ensure it correctly validates a token against a user.
   */
  @Test
  void testValidateToken() {
    var userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("test@example.com");

    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    boolean isValid = DefaultJwtUtils.validateToken(token, userDetails);
    assertTrue(isValid, "Token should be valid when usernames match");
  }

  /**
   * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)}
   * method to ensure it returns false when the username does not match.
   */
  @Test
  void testValidateTokenUsernameMismatch() {
    var userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("wrong@example.com");

    var token = DefaultJwtUtils.generateToken(mockUserDetails);
    boolean isValid = DefaultJwtUtils.validateToken(token, userDetails);
    assertFalse(isValid, "Token should be invalid when usernames do not match");
  }

  /**
   * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)}
   * method to ensure it returns false when the token is expired.
   */
  @Test
  void testValidateTokenExpired() {
    MockedStatic<DefaultJwtUtils> mockStatic =
          Mockito.mockStatic(DefaultJwtUtils.class);

    // Generate a token for testing
    var token = DefaultJwtUtils.generateToken(mockUserDetails);

    // Simulate that the token has expired
    mockStatic.when(() -> DefaultJwtUtils.isTokenExpired(token))
          .thenReturn(true);

    // Validate the token
    boolean isValid = DefaultJwtUtils.validateToken(token, mockUserDetails);
    assertFalse(isValid, "Token should be invalid when it is expired");

    // Close the static mock
    mockStatic.close();
  }
}
