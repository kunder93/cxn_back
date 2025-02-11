package es.org.cxn.backapp.test.unit.filter;

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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.filter.JwtRequestFilter;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import io.jsonwebtoken.JwtException;
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
     * Mocked instance of {@link DefaultJwtUtils} used to simulate JWT utility
     * methods.
     */
    @Mock
    private DefaultJwtUtils jwtUtils;

    /**
     * Mocked instance of {@link UserDetailsService} to load user details.
     */
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * Mocked instance of {@link HttpServletRequest} used to simulate HTTP requests.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mocked instance of {@link HttpServletResponse} used to simulate HTTP
     * responses.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mocked instance of {@link FilterChain} used to simulate the filter chain.
     */
    @Mock
    private FilterChain filterChain;

    /**
     * Instance of {@link JwtRequestFilter} that is being tested. This filter
     * handles JWT authentication and user validation.
     */
    @InjectMocks
    private JwtRequestFilter jwtFilter;

    /**
     * Sets up the test environment, initializing the mocks and configuring their
     * behavior. This method is run before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDetails mockUser = mock(UserDetails.class);

        when(mockUser.getUsername()).thenReturn("user@test.com");

        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(mockUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // Limpiar contexto después de cada test
    }

    @Test
    void testInvalidJwt() throws Exception {
        // Configurar mocks
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtUtils.extractUsername("invalid.token")).thenThrow(new JwtException("Invalid token"));

        // Acceder al método protegido usando reflexión
        Method method = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class,
                HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtFilter, request, response, filterChain);

        // Verificar
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testUnprotectedEndpoint() throws Exception {
        // Configurar mocks
        when(request.getRequestURI()).thenReturn(AppURL.SIGN_IN_URL);

        // Acceder al método protegido
        Method method = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class,
                HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);
        method.invoke(jwtFilter, request, response, filterChain);

        // Verificar
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testValidJwt() throws Exception {
        // Configurar mocks
        UserDetails mockUser = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtils.extractUsername("valid.token")).thenReturn("user@test.com");
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(mockUser);
        when(jwtUtils.validateToken("valid.token", mockUser)).thenReturn(true);

        // Acceder al método protegido usando reflexión
        Method method = JwtRequestFilter.class.getDeclaredMethod("doFilterInternal", HttpServletRequest.class,
                HttpServletResponse.class, FilterChain.class);
        method.setAccessible(true);

        method.invoke(jwtFilter, request, response, filterChain);

        // Verificaciones
        verify(filterChain).doFilter(request, response);
    }
}
