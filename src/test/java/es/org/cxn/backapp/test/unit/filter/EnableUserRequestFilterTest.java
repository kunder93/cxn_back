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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.org.cxn.backapp.exceptions.DisabledUserException;
import es.org.cxn.backapp.filter.EnableUserRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Unit test class for the {@link EnableUserRequestFilter}.
 * <p>
 * This test class is responsible for verifying the behavior of the
 * {@code EnableUserRequestFilter} class, which is used to check if a user is
 * authenticated and enabled before processing requests. The class uses Mockito
 * to mock dependencies such as {@code UserDetailsService},
 * {@code Authentication}, and {@code HttpServletRequest}, allowing for unit
 * testing of the filter's logic without requiring integration with actual
 * request handling or authentication mechanisms.
 * </p>
 */
class EnableUserRequestFilterTest {

    /**
     * Mocked instance of {@link UserDetailsService} used for loading user details.
     */
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * Mocked instance of {@link Authentication} used for simulating authenticated
     * user requests.
     */
    @Mock
    private Authentication authentication;

    /**
     * Mocked instance of {@link UserDetails} representing user information used in
     * tests.
     */
    @Mock
    private UserDetails userDetails;

    /**
     * Mocked instance of {@link HttpServletRequest} representing the HTTP request.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mocked instance of {@link HttpServletResponse} representing the HTTP
     * response.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mocked instance of {@link FilterChain} used to simulate the filter chain in
     * the test.
     */
    @Mock
    private FilterChain filterChain;

    /**
     * Mock instance of {@link SecurityContex}.
     */
    @Mock
    private SecurityContext securityContext;
    /**
     * The instance of {@link EnableUserRequestFilter} under test, which is
     * responsible for ensuring that users are authenticated and enabled before
     * processing requests.
     */
    private EnableUserRequestFilter enableUserRequestFilter;

    /**
     * Initializes mocks and the filter instance before each test. This method is
     * invoked before each test method to set up the necessary mock objects and
     * create a fresh instance of {@link EnableUserRequestFilter} for testing.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);
    }

    /**
     * Tests the {@code doFilterInternal} method of the
     * {@link EnableUserRequestFilter} class when the request URI corresponds to an
     * excluded URI (i.e., "/sign-up").
     * <p>
     * This test simulates a scenario where the request URI is "/sign-up", which is
     * excluded from the filter's logic. The test ensures that even if the user is
     * authenticated and enabled, the filter does not interfere with the request
     * processing for this excluded URI, and the request is passed along the filter
     * chain without further validation or checks.
     * </p>
     *
     * @throws Exception if there is an error during the execution of the test or
     *                   filter
     */
    @Test
    void testDoFilterInternalExcludedURISignUp() throws Exception {
        // Initialize the filter with the mocked UserDetailsService
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);

        String email = "test@example.com";

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            // Mock SecurityContext and Authentication
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock Authentication details
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn(email);

            // Mock UserDetailsService behavior
            when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
            when(userDetails.isEnabled()).thenReturn(true);

            // Mock request URI and method for excluded URI
            when(request.getRequestURI()).thenReturn("/sign-up");
            when(request.getMethod()).thenReturn("POST");

            // Use reflection to access the protected method
            Method doFilterInternalMethod = EnableUserRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternalMethod.setAccessible(true);

            // Invoke the protected method using reflection
            doFilterInternalMethod.invoke(enableUserRequestFilter, request, response, filterChain);

            // Verify the filter chain was called, indicating the filter passed through
            verify(filterChain).doFilter(request, response);
        }
    }

    /**
     * Tests the {@code doFilterInternal} method of the
     * {@link EnableUserRequestFilter} class when no authentication is present in
     * the {@link SecurityContext}.
     * <p>
     * This test simulates a scenario where there is no authentication in the
     * {@code SecurityContext}. The test ensures that even without authentication,
     * the filter should pass the request along the filter chain, allowing the
     * request to proceed without any interference from the filter. The test mocks
     * the necessary components, such as {@link SecurityContextHolder}, to simulate
     * the absence of authentication.
     * </p>
     *
     * @throws Exception if there is an error during the execution of the test or
     *                   filter
     */
    @Test
    void testDoFilterInternalNoAuthentication() throws Exception {
        // Initialize the filter with the mocked UserDetailsService
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);

        // Use a MockedStatic block to mock SecurityContextHolder
        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            // Mock SecurityContextHolder.getContext() to return the mock SecurityContext
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock getAuthentication() to return null, simulating no authentication
            when(securityContext.getAuthentication()).thenReturn(null);

            // Use reflection to access the protected method
            Method doFilterInternalMethod = EnableUserRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternalMethod.setAccessible(true);

            // Invoke the protected method using reflection
            doFilterInternalMethod.invoke(enableUserRequestFilter, request, response, filterChain);

            // Verify that the filter chain is called, indicating the filter passed through
            verify(filterChain).doFilter(request, response);
        }
    }

    /**
     * Tests the {@code doFilterInternal} method of the
     * {@link EnableUserRequestFilter} class when the request method is
     * {@code POST}.
     * <p>
     * This test verifies that when a user is authenticated and enabled, and the
     * request method is a {@code POST}, the filter should pass the request through
     * the filter chain without interruption. The test mocks the necessary
     * components, such as {@link SecurityContextHolder}, {@link Authentication},
     * and {@link UserDetailsService}, to simulate an authenticated and enabled user
     * making a POST request. The request URI is set to
     * {@code /getAllLichessProfiles} to test the filter behavior for a valid POST
     * request.
     * </p>
     *
     * @throws Exception if there is an error during the execution of the test or
     *                   filter
     */
    @Test
    void testDoFilterInternalPostMethod() throws Exception {
        // Initialize the filter with the mocked UserDetailsService
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);

        String email = "test@example.com";

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            // Mock SecurityContext and Authentication
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            // Ensure authentication is considered authenticated
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn(email);

            // Mock UserDetailsService to return an enabled user
            when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
            when(userDetails.isEnabled()).thenReturn(true);

            // Setup the request and method
            when(request.getRequestURI()).thenReturn("/getAllLichessProfiles");
            when(request.getMethod()).thenReturn("POST");

            // Use reflection to access the protected method
            Method doFilterInternalMethod = EnableUserRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternalMethod.setAccessible(true);

            // Invoke the protected method using reflection
            doFilterInternalMethod.invoke(enableUserRequestFilter, request, response, filterChain);

            // Verify that the filter chain is called (filter passed through)
            verify(filterChain).doFilter(request, response);
        }
    }

    /**
     * Tests the {@code doFilterInternal} method of the
     * {@link EnableUserRequestFilter} class when the user is authenticated but
     * disabled.
     * <p>
     * This test verifies that when a user is authenticated but their account is
     * disabled, the filter should clear the security context and throw an
     * {@link IllegalStateException}. It mocks the necessary components such as
     * {@link SecurityContextHolder}, {@link Authentication}, and
     * {@link UserDetailsService} to simulate an authenticated but disabled user
     * scenario. The test ensures that the filter behaves as expected by throwing an
     * exception and clearing the security context, as the user should not be
     * allowed to proceed with the request.
     * </p>
     *
     * @throws Exception if there is an error during the execution of the test or
     *                   filter
     */
    @Test
    void testDoFilterInternalUserIsAuthenticatedAndDisabled() throws Exception {
        // Initialize the filter with the mocked UserDetailsService
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);

        String email = "test@example.com";

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            // Mock SecurityContext and Authentication
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            // Ensure the authentication is considered authenticated
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn(email);

            // Mock UserDetailsService to return a disabled user
            when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
            when(userDetails.isEnabled()).thenReturn(false);

            // Use reflection to access the protected method
            Method doFilterInternalMethod = EnableUserRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternalMethod.setAccessible(true);

            // Execute and verify that an exception is thrown
            InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
                doFilterInternalMethod.invoke(enableUserRequestFilter, request, response, filterChain);
            });

            // Extract the cause of the InvocationTargetException and assert it
            Throwable cause = exception.getCause();
            assertNotNull(cause, "Expected a cause for InvocationTargetException");
            assertEquals(DisabledUserException.class, cause.getClass());
            assertEquals("User is disabled.", cause.getMessage());

            // Verify that SecurityContextHolder.clearContext() is called
            mocked.verify(() -> SecurityContextHolder.clearContext());
        }
    }

    /**
     * Tests the {@code doFilterInternal} method of the
     * {@link EnableUserRequestFilter} class when the user is authenticated and
     * enabled.
     * <p>
     * This test verifies that when a user is authenticated and their account is
     * enabled, the filter allows the request to proceed without interruption. It
     * mocks the necessary components such as {@link SecurityContextHolder},
     * {@link Authentication}, and {@link UserDetailsService} to simulate an
     * authenticated and enabled user scenario. The test ensures that the
     * {@code filterChain.doFilter} method is called, indicating that the request is
     * passed through without any issues.
     * </p>
     *
     * @throws Exception if there is an error during the execution of the test or
     *                   filter
     */
    @Test
    void testDoFilterInternalUserIsAuthenticatedAndEnabled() throws Exception {
        // Initialize the filter with the mocked UserDetailsService
        enableUserRequestFilter = new EnableUserRequestFilter(userDetailsService);

        String email = "test@example.com";

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            // Mock SecurityContext and Authentication
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            // Ensure authentication is considered authenticated
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn(email);

            // Mock UserDetailsService to return an enabled user
            when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
            when(userDetails.isEnabled()).thenReturn(true);

            // Use reflection to access the protected method
            Method doFilterInternalMethod = EnableUserRequestFilter.class.getDeclaredMethod("doFilterInternal",
                    HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
            doFilterInternalMethod.setAccessible(true);

            // Invoke the method
            doFilterInternalMethod.invoke(enableUserRequestFilter, request, response, filterChain);

            // Verify that the filter chain is passed along (filter passed through)
            verify(filterChain).doFilter(request, response);
        }
    }

    /**
     * Tests the {@code shouldNotFilter} method of the
     * {@link EnableUserRequestFilter} class.
     * <p>
     * This test verifies that the filter is not applied to requests for excluded
     * URIs. Specifically, it checks the behavior when a request is made to the
     * {@code /h2-console} endpoint using the {@code GET} method. The test accesses
     * the {@code shouldNotFilter} method via reflection and asserts that it returns
     * {@code true}, indicating that the filter should not process the request for
     * this excluded URI.
     * </p>
     *
     * @throws Exception if there is an error during method invocation or test
     *                   execution
     */
    @Test
    void testShouldNotFilterExcludedURI() throws Exception {
        // Setup
        when(request.getRequestURI()).thenReturn("/h2-console");
        when(request.getMethod()).thenReturn("GET");

        // Use reflection to invoke the protected method
        Method method = EnableUserRequestFilter.class.getDeclaredMethod("shouldNotFilter", HttpServletRequest.class);
        method.setAccessible(true);

        // Execute and assert that the filter should not be applied
        assertTrue((Boolean) method.invoke(enableUserRequestFilter, request));
    }

    /**
     * Tests the {@code shouldNotFilter} method of the
     * {@link EnableUserRequestFilter} class.
     * <p>
     * This test verifies that the filter is applied when a valid request is made to
     * the {@code /api/user} endpoint using the {@code GET} method. The test
     * accesses the {@code shouldNotFilter} method via reflection and asserts that
     * it returns {@code false}, indicating that the filter should process the
     * request.
     * </p>
     *
     * @throws Exception if there is an error during method invocation or test
     *                   execution
     */
    @Test
    void testShouldNotFilterValidRequest() throws Exception {
        // Setup
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        // Access the protected method using reflection
        Method method = EnableUserRequestFilter.class.getDeclaredMethod("shouldNotFilter", HttpServletRequest.class);
        method.setAccessible(true); // Make the method accessible

        // Invoke the method and assert that the filter should be applied (i.e., it
        // should return false)
        boolean result = (boolean) method.invoke(enableUserRequestFilter, request);

        // Assert that the filter should be applied
        assertFalse(result);
    }
}
