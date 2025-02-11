
package es.org.cxn.backapp.test.unit.security;

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

/**
 * Unit tests for the {@link es.org.cxn.backapp.security.DefaultJwtUtils} class.
 * <p>
 * These tests cover the generation, extraction, validation, and expiration
 * checks of JWT tokens.
 */
class JwtUtilsTest {

//    private MyPrincipalUser mockUserDetails;
//    private DefaultJwtUtils jwtUtils;
//
//    @BeforeEach
//    void setUp() {
//        // Configurar JWTUtils con valores de prueba
//        String testSecret = "c3VwZXItc2VjcmV0LWNsYXZlLWRlLXRlc3RpbmctMTIzNDU2Nzg5MDEyMzQ1Njc4OTA=";
//        Duration testExpiration = Duration.ofHours(1);
//        jwtUtils = new DefaultJwtUtils(testSecret, testExpiration);
//
//        // Mock UserDetails
//        mockUserDetails = mock(MyPrincipalUser.class);
//        when(mockUserDetails.getUsername()).thenReturn("test@example.com");
//    }
//
//    /**
//     * Tear down method to clean up resources after each test.
//     */
//    @AfterEach
//    public void tearDown() {
//        // No specific resources to clean up in this implementation.
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#extractExpiration(String)} method to ensure
//     * it correctly extracts the expiration date from a token.
//     */
//    @Test
//    void testExtractExpiration() {
//        var token = jwtUtils.generateToken(mockUserDetails);
//        var expiration = jwtUtils.extractExpiration(token);
//        assertTrue(expiration.isAfter(Instant.now()), "Expiration date should be in the future");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#extractUsername(String)} method to ensure it
//     * correctly extracts the username from a token.
//     */
//    @Test
//    void testExtractUsername() {
//        var token = jwtUtils.generateToken(mockUserDetails);
//        var username = jwtUtils.extractUsername(token);
//        assertEquals("test@example.com", username, "Extracted username should match the expected value");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#generateToken(UserDetails)} method to ensure
//     * it generates a valid JWT token.
//     */
//    @Test
//    void testGenerateToken() {
//        var token = jwtUtils.generateToken(mockUserDetails);
//        assertNotNull(token, "Token should not be null");
//
//        var extractedUsername = jwtUtils.extractUsername(token);
//        assertEquals("test@example.com", extractedUsername, "Extracted username should match the input username");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#isTokenExpired(String)} method to ensure it
//     * correctly identifies if a token has expired.
//     */
//    @Test
//    void testIsTokenExpired() {
//        var token = jwtUtils.generateToken(mockUserDetails);
//        boolean isExpired = jwtUtils.isTokenExpired(token);
//        assertFalse(isExpired, "Token should not be expired immediately after generation");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)} method to
//     * ensure it correctly validates a token against a user.
//     */
//    @Test
//    void testValidateToken() {
//        var userDetails = mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn("test@example.com");
//
//        var token = jwtUtils.generateToken(mockUserDetails);
//        boolean isValid = jwtUtils.validateToken(token, userDetails);
//        assertTrue(isValid, "Token should be valid when usernames match");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)} method to
//     * ensure it returns false when the token is expired.
//     */
//    @Test
//    void testValidateTokenExpired() {
//        // 1. Crear instancia con expiración inmediata
//        DefaultJwtUtils expiredJwtUtils = new DefaultJwtUtils(
//                "c3VwZXItc2VjcmV0LWNsYXZlLWRlLXRlc3RpbmctMTIzNDU2Nzg5MDEyMzQ1Njc4OTA=" // Usar un secreto válido en
//                // Base64
//
//        );
//
//        // 2. Generar token con expiración en el pasado
//        String token = expiredJwtUtils.generateToken(mockUserDetails);
//
//        // 3. Validar con la instancia normal
//        boolean isValid = jwtUtils.validateToken(token, mockUserDetails);
//
//        assertFalse(isValid, "El token debe ser inválido cuando está expirado");
//    }
//
//    /**
//     * Test the {@link DefaultJwtUtils#validateToken(String, UserDetails)} method to
//     * ensure it returns false when the username does not match.
//     */
//    @Test
//    void testValidateTokenUsernameMismatch() {
//        var userDetails = mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn("wrong@example.com");
//
//        var token = jwtUtils.generateToken(mockUserDetails);
//        boolean isValid = jwtUtils.validateToken(token, userDetails);
//        assertFalse(isValid, "Token should be invalid when usernames do not match");
//    }
}
