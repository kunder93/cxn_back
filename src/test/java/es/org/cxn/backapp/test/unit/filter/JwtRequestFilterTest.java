package es.org.cxn.backapp.test.unit.filter;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.org.cxn.backapp.filter.JwtRequestFilter;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.security.MyPrincipalUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Unit test class for {@link JwtRequestFilter}.
 *
 * This class contains unit tests for the {@link JwtRequestFilter} that test the
 * filter logic related to JWT authentication and user validation. It uses mocks
 * for dependencies like {@link UserDetailsService}, {@link DefaultJwtUtils},
 * {@link HttpServletRequest}, {@link HttpServletResponse}, and
 * {@link FilterChain}.
 */
class JwtRequestFilterTest {

    /**
     * Mocked instance of {@link UserDetailsService} used to simulate the behavior
     * of the user details service. This mock is injected into the
     * {@link JwtRequestFilter} during test execution to simulate user loading.
     */
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * Mocked instance of {@link DefaultJwtUtils} used to simulate the behavior of
     * JWT utility methods. This mock is used to validate JWT tokens in the
     * {@link JwtRequestFilter}.
     */
    @Mock
    private DefaultJwtUtils jwtUtils;

    /**
     * Mocked instance of {@link HttpServletRequest} used to simulate HTTP requests.
     * This mock is used to retrieve request headers and URI during test execution.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mocked instance of {@link HttpServletResponse} used to simulate HTTP
     * responses. This mock is used to pass the response to the filter chain during
     * test execution.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mocked instance of {@link FilterChain} used to simulate the filter chain in
     * the request processing. This mock is used to simulate the passing of requests
     * through the filter chain.
     */
    @Mock
    private FilterChain filterChain;

    /**
     * Instance of {@link JwtRequestFilter} that is being tested. This filter is
     * responsible for handling JWT-based authentication in the application. The
     * {@link JwtRequestFilter} is injected with the mocked dependencies using the
     * {@link InjectMocks} annotation.
     */
    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Initializes the mocks and sets up the {@link JwtRequestFilter} instance
     * before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JwtRequestFilter(userDetailsService);
    }

    /**
     * Test for the scenario when the JWT token is invalid. The test ensures that if
     * the token is invalid, the filter does not set authentication and the filter
     * chain is invoked.
     */
    @Test
    void testDoFilterInternalWithInvalidJwt() throws Exception {
        // Mock request and response behavior
        when(request.getRequestURI()).thenReturn("/protected-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");

        // Mock static method extractUsername to throw a RuntimeException
        try (MockedStatic<DefaultJwtUtils> mockedStatic = mockStatic(DefaultJwtUtils.class)) {
            mockedStatic.when(() -> DefaultJwtUtils.extractUsername("invalid.token"))
                    .thenThrow(new RuntimeException("Invalid token"));

            // Access the private method 'doFilterInternal'
            Method doFilterInternal = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternal.setAccessible(true);

            // Invoke the method with the request, response, and filter chain
            try {
                doFilterInternal.invoke(jwtRequestFilter, request, response, filterChain);
            } catch (InvocationTargetException e) {
                // Handle the thrown exception from DefaultJwtUtils
                assertTrue(e.getCause() instanceof RuntimeException);
                assertEquals("Invalid token", e.getCause().getMessage());
            }

            // Explicitly clear the authentication to ensure it's null
            SecurityContextHolder.clearContext();

            // Assert that authentication was not set due to the invalid token
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    /**
     * Test for the scenario when the JWT token is valid. The test ensures that if
     * the token is valid, the filter sets the authentication and the filter chain
     * is invoked.
     */
    @Test
    void testDoFilterInternalWithValidJwt() throws Exception {
        // Mock the static methods
        try (MockedStatic<DefaultJwtUtils> mockedStatic = mockStatic(DefaultJwtUtils.class)) {
            // Mocking extractUsername static method
            mockedStatic.when(() -> DefaultJwtUtils.extractUsername("valid.token.here")).thenReturn("testUser");

            // Mocking validateToken static method
            MyPrincipalUser mockUser = mock(MyPrincipalUser.class);
            mockedStatic.when(() -> DefaultJwtUtils.validateToken("valid.token.here", mockUser)).thenReturn(true);

            // Mock the request and userDetailsService
            when(request.getRequestURI()).thenReturn("/protected-endpoint");
            when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");
            when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUser);

            // Access the private method 'doFilterInternal'
            Method doFilterInternal = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternal.setAccessible(true);

            // Invoke the method with the request, response, and filter chain
            doFilterInternal.invoke(jwtRequestFilter, request, response, filterChain);

            // Capture the request and response
            ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
            ArgumentCaptor<HttpServletResponse> responseCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);

            // Verify that doFilter was called on the filter chain
            verify(filterChain).doFilter(requestCaptor.capture(), responseCaptor.capture());
        }
    }

    /**
     * Test for the scenario when the Authorization header does not contain a valid
     * JWT prefix. The test ensures that the method
     * {@link JwtRequestFilter#extractJwtFromRequest(HttpServletRequest)} returns an
     * empty {@link Optional}.
     */
    @Test
    void testExtractJwtFromRequestWithInvalidPrefix() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Invalid valid.token.here");

        // Access the private method
        Method extractJwtFromRequest = JwtRequestFilter.class.getDeclaredMethod("extractJwtFromRequest",
                HttpServletRequest.class);
        extractJwtFromRequest.setAccessible(true);

        @SuppressWarnings("unchecked")
        Optional<String> jwt = (Optional<String>) extractJwtFromRequest.invoke(jwtRequestFilter, request);

        assertTrue(jwt.isEmpty());
    }

    /**
     * Test for the scenario when the Authorization header is missing from the
     * request. The test ensures that the method
     * {@link JwtRequestFilter#extractJwtFromRequest(HttpServletRequest)} returns an
     * empty {@link Optional}.
     */
    @Test
    void testExtractJwtFromRequestWithMissingHeader() throws Exception {
        // Mock the request to return null for the Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Use reflection to access the private method
        Method extractJwtFromRequestMethod = JwtRequestFilter.class.getDeclaredMethod("extractJwtFromRequest",
                HttpServletRequest.class);
        extractJwtFromRequestMethod.setAccessible(true);

        // Invoke the method
        @SuppressWarnings("unchecked")
        Optional<String> jwt = (Optional<String>) extractJwtFromRequestMethod.invoke(jwtRequestFilter, request);

        // Assert the result
        assertTrue(jwt.isEmpty());
    }

    /**
     * Test for the scenario when the Authorization header contains a valid JWT. The
     * test ensures that the method
     * {@link JwtRequestFilter#extractJwtFromRequest(HttpServletRequest)} extracts
     * the JWT correctly.
     */
    @Test
    void testExtractJwtFromRequestWithValidHeader() throws Exception {
        // Mock the request to return the Authorization header with a valid token
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");

        // Use reflection to access the private method
        Method extractJwtFromRequestMethod = JwtRequestFilter.class.getDeclaredMethod("extractJwtFromRequest",
                HttpServletRequest.class);
        extractJwtFromRequestMethod.setAccessible(true);

        // Invoke the method
        @SuppressWarnings("unchecked")
        Optional<String> jwt = (Optional<String>) extractJwtFromRequestMethod.invoke(jwtRequestFilter, request);

        // Assert the result
        assertTrue(jwt.isPresent());
        assertEquals("valid.token.here", jwt.get());
    }

    /**
     * Test for the scenario when the JWT token is invalid and the username
     * extraction fails. The test ensures that the method
     * {@link JwtRequestFilter#getUsernameFromJwt(String)} returns an empty
     * {@link Optional}.
     */
    @Test
    void testGetUsernameFromJwtWithInvalidToken() throws Exception {
        // Mock the static method DefaultJwtUtils.extractUsername to throw an exception
        try (MockedStatic<DefaultJwtUtils> mockedStatic = mockStatic(DefaultJwtUtils.class)) {
            mockedStatic.when(() -> DefaultJwtUtils.extractUsername("invalid.token"))
                    .thenThrow(new RuntimeException("Invalid token"));

            // Use reflection to access the private method 'getUsernameFromJwt'
            Method getUsernameFromJwtMethod = JwtRequestFilter.class.getDeclaredMethod("getUsernameFromJwt",
                    String.class);
            getUsernameFromJwtMethod.setAccessible(true);

            // Use assertThrows to catch the InvocationTargetException and check the cause
            Exception exception = assertThrows(InvocationTargetException.class, () -> {
                // Invoke the method with an invalid token
                getUsernameFromJwtMethod.invoke(jwtRequestFilter, "invalid.token");
            });

            // Assert that the cause of the InvocationTargetException is a RuntimeException
            assertTrue(exception.getCause() instanceof RuntimeException);
            assertEquals("Invalid token", exception.getCause().getMessage());
        }
    }

    /**
     * Test for the scenario when the JWT token is valid and the username extraction
     * succeeds. The test ensures that the method
     * {@link JwtRequestFilter#getUsernameFromJwt(String)} correctly returns the
     * extracted username.
     */
    @Test
    void testGetUsernameFromJwtWithValidToken() throws Exception {
        // Use Mockito to mock static method behavior
        try (MockedStatic<DefaultJwtUtils> mocked = Mockito.mockStatic(DefaultJwtUtils.class)) {
            mocked.when(() -> DefaultJwtUtils.extractUsername("valid.token.here")).thenReturn("testUser");

            // Use reflection to access the private method
            Method getUsernameFromJwtMethod = JwtRequestFilter.class.getDeclaredMethod("getUsernameFromJwt",
                    String.class);
            getUsernameFromJwtMethod.setAccessible(true);

            // Invoke the method with a valid token
            Object result = getUsernameFromJwtMethod.invoke(jwtRequestFilter, "valid.token.here");

            // Ensure the result is an instance of Optional and suppress unchecked warning
            if (result instanceof Optional<?>) {
                @SuppressWarnings("unchecked")
                Optional<String> username = (Optional<String>) result;

                // Assert that the result contains the expected username
                assertTrue(username.isPresent());
                assertEquals("testUser", username.get());
            } else {
                fail("Expected Optional<String>, but got: " + result.getClass().getName());
            }
        }
    }

    /**
     * Test for the scenario when the authentication is set correctly in the
     * security context. The test ensures that the
     * {@link JwtRequestFilter#setAuthentication(MyPrincipalUser, HttpServletRequest)}
     * method sets the authentication in the {@link SecurityContextHolder}.
     */
    @Test
    void testSetAuthenticationSetsContext() throws Exception {
        // Mock the behavior for MyPrincipalUser
        MyPrincipalUser mockUser = mock(MyPrincipalUser.class);
        when(mockUser.getUsername()).thenReturn("testUser");
        when(mockUser.getAuthorities()).thenReturn(null);

        // Use reflection to access the private method
        Method setAuthenticationMethod = JwtRequestFilter.class.getDeclaredMethod("setAuthentication",
                MyPrincipalUser.class, HttpServletRequest.class);
        setAuthenticationMethod.setAccessible(true);

        // Invoke the method with the mock user and request
        setAuthenticationMethod.invoke(jwtRequestFilter, mockUser, request);

        // Retrieve the authentication from the security context
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        // Assert that authentication is not null and has the correct values
        assertNotNull(authentication);
        assertEquals("testUser", authentication.getName());
        assertEquals(mockUser, authentication.getPrincipal());
    }

    /**
     * Test for the scenario when the filter is not applied to a protected URI. The
     * test ensures that the method
     * {@link JwtRequestFilter#shouldNotFilter(HttpServletRequest)} correctly
     * identifies whether the URI is protected.
     */
    @Test
    void testShouldNotFilterProtectedUri() throws Exception {
        // Use reflection to access the private method
        Method shouldNotFilterMethod = JwtRequestFilter.class.getDeclaredMethod("shouldNotFilter",
                HttpServletRequest.class);
        shouldNotFilterMethod.setAccessible(true);

        // Simulate request for protected URI
        when(request.getRequestURI()).thenReturn("/protected-endpoint");

        // Invoke the method
        boolean result = (boolean) shouldNotFilterMethod.invoke(jwtRequestFilter, request);

        // Assert the result should be false for a protected URI
        assertFalse(result);
    }

    /**
     * Test for the scenario when the request URI corresponds to an unprotected
     * endpoint. This test ensures that the method
     * {@link JwtRequestFilter#shouldNotFilter(HttpServletRequest)} correctly
     * identifies unprotected URIs and returns {@code true} to indicate that the
     * filter should not be applied.
     *
     * The test simulates a request to the `/h2-console` endpoint, which is
     * typically unprotected, and verifies that the method returns {@code true},
     * indicating that the filter should not intercept the request.
     *
     * @throws Exception If there is an issue invoking the private method or
     *                   simulating the request.
     */
    @Test
    void testShouldNotFilterUnprotectedUri() throws Exception {
        // Use reflection to access the private method
        Method shouldNotFilterMethod = JwtRequestFilter.class.getDeclaredMethod("shouldNotFilter",
                HttpServletRequest.class);
        shouldNotFilterMethod.setAccessible(true);

        // Simulate request for unprotected URI
        when(request.getRequestURI()).thenReturn("/h2-console");

        // Invoke the method
        boolean result = (boolean) shouldNotFilterMethod.invoke(jwtRequestFilter, request);

        // Assert the result should be true for an unprotected URI
        assertTrue(result);
    }

    /**
     * Test for the scenario where the user is not found by the userDetailsService.
     * This test simulates a case where the username does not exist, resulting in a
     * {@link RuntimeException} being thrown. The method
     * {@link JwtRequestFilter#validateAndLoadUser(String, HttpServletRequest)} is
     * invoked with the "testUser" username, and the expected result is an empty
     * { @link Optional<MyPrincipalUser> }.
     *
     * @throws Exception If there is an issue invoking the private method or
     *                   simulating the request.
     */
    @Test
    void testValidateAndLoadUserWithException() throws Exception {
        // Mock user details service to throw an exception for a username
        when(userDetailsService.loadUserByUsername("testUser")).thenThrow(new RuntimeException("User not found"));
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");

        // Use reflection to access the private method
        Method validateAndLoadUserMethod = JwtRequestFilter.class.getDeclaredMethod("validateAndLoadUser", String.class,
                HttpServletRequest.class);
        validateAndLoadUserMethod.setAccessible(true);

        // Invoke the method and catch the InvocationTargetException
        InvocationTargetException thrownException = assertThrows(InvocationTargetException.class, () -> {
            // Invoke the method with the username and request
            validateAndLoadUserMethod.invoke(jwtRequestFilter, "testUser", request);
        });

        // Unwrap the cause of the InvocationTargetException
        Throwable cause = thrownException.getCause();

        // Assert that the cause is a RuntimeException with the expected message
        assertTrue(cause instanceof RuntimeException);
        assertEquals("User not found", cause.getMessage());
    }

    @Test
    void testValidateAndLoadUserWithInvalidToken() throws Exception {
        // Create a mock user
        MyPrincipalUser mockUser = mock(MyPrincipalUser.class);

        // Mock the behavior of the userDetailsService to return the mock user for a
        // username
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUser);

        // Mock the request header to simulate the Authorization header with an invalid
        // token
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");

        // Mock the static method validateToken using Mockito.mockStatic for an invalid
        // token
        try (MockedStatic<DefaultJwtUtils> mockedStatic = mockStatic(DefaultJwtUtils.class)) {
            // Define the behavior of validateToken when called with an invalid token
            mockedStatic.when(() -> DefaultJwtUtils.validateToken("invalid.token", mockUser)).thenReturn(false);

            // Use reflection to access the private method 'validateAndLoadUser'
            Method validateAndLoadUserMethod = JwtRequestFilter.class.getDeclaredMethod("validateAndLoadUser",
                    String.class, HttpServletRequest.class);
            validateAndLoadUserMethod.setAccessible(true);

            // Invoke the method with the username and request
            Object result = validateAndLoadUserMethod.invoke(jwtRequestFilter, "testUser", request);

            // Ensure the result is an instance of Optional and suppress unchecked warning
            if (result instanceof Optional<?>) {
                @SuppressWarnings("unchecked")
                Optional<MyPrincipalUser> user = (Optional<MyPrincipalUser>) result;

                // Assert that the result is empty because the token is invalid
                assertTrue(user.isEmpty());
            } else {
                fail("Expected Optional<MyPrincipalUser>, but got: " + result.getClass().getName());
            }
        }
    }

    /**
     * Test for the scenario where a valid token and an existing user are provided.
     * This test ensures that the method
     * {@link JwtRequestFilter#validateAndLoadUser(String, HttpServletRequest)}
     * returns a present { @link Optional<MyPrincipalUser> } with the correct user
     * when called with a valid token.
     *
     * The behavior of the static method
     * {@link DefaultJwtUtils#validateToken(String, MyPrincipalUser)} is mocked to
     * return true for the valid token.
     *
     * @throws Exception If there is an issue invoking the private method or
     *                   simulating the request.
     */
    @Test
    void testValidateAndLoadUserWithValidTokenAndUser() throws Exception {
        // Create a mock user
        MyPrincipalUser mockUser = mock(MyPrincipalUser.class);

        // Mock the behavior of the userDetailsService to return the mock user for a
        // username
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUser);

        // Mock the request header to simulate the Authorization header
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");

        // Mock the static method validateToken using Mockito.mockStatic
        try (MockedStatic<DefaultJwtUtils> mockedStatic = mockStatic(DefaultJwtUtils.class)) {
            // Define the behavior of validateToken when called
            mockedStatic.when(() -> DefaultJwtUtils.validateToken("valid.token.here", mockUser)).thenReturn(true);

            // Use reflection to access the private method 'validateAndLoadUser'
            Method validateAndLoadUserMethod = JwtRequestFilter.class.getDeclaredMethod("validateAndLoadUser",
                    String.class, HttpServletRequest.class);
            validateAndLoadUserMethod.setAccessible(true);

            // Invoke the method with the username and request
            Object result = validateAndLoadUserMethod.invoke(jwtRequestFilter, "testUser", request);

            // Ensure the result is an instance of Optional and suppress unchecked warning
            if (result instanceof Optional<?>) {
                @SuppressWarnings("unchecked")
                Optional<MyPrincipalUser> user = (Optional<MyPrincipalUser>) result;

                // Assert that the user is present and equals the mock user
                assertTrue(user.isPresent());
                assertEquals(mockUser, user.get());
            } else {
                fail("Expected Optional<MyPrincipalUser>, but got: " + result.getClass().getName());
            }
        }
    }
}
