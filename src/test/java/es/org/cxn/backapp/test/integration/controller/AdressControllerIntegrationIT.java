
package es.org.cxn.backapp.test.integration.controller;

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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.service.impl.DefaultEmailService;

/**
 * Integration tests for the AddressController.
 *
 * <p>
 * These tests verify that the REST endpoints related to address data retrieval
 * work as expected.
 * </p>
 *
 * <p>
 * It covers scenarios such as retrieving the list of countries, validating the
 * number of subdivisions for a specific country, and handling cases where a
 * non-existing country code is requested.
 * </p>
 *
 * @author Santiago Paz
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AdressControllerIntegrationIT {

    /**
     * The URL endpoint for retrieving the list of countries.
     *
     * <p>
     * This constant is used in the test methods to send GET requests to the
     * endpoint that returns the list of available countries.
     * </p>
     */
    private static final String GET_COUNTRIES_URL = "/api/address/getCountries";

    /**
     * The expected number of countries returned by the API.
     *
     * <p>
     * This constant represents the number of countries that should be returned by
     * the API when the {@code GET_COUNTRIES_URL} endpoint is called.
     * </p>
     */
    private static final int COUNTRIES_COUNT = 2;
    /**
     * The expected number of subdivisions for Spain.
     *
     * <p>
     * This constant represents the number of subdivisions that should be returned
     * by the API for Spain when its country code is used in a request.
     * </p>
     */
    private static final int SPAIN_SUBDIVISIONS = 50;

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * Mocked mail sender.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * Used to simulate HTTP requests and perform assertions on the results within
     * the test cases.
     *
     * <p>
     * It allows the tests to be run in a way that simulates sending requests to the
     * application's controllers without needing to start a full HTTP server.
     * </p>
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Default constructor.
     */
    AdressControllerIntegrationIT() {
        super();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "1025");
        registry.add("spring.mail.username", () -> "test@example.com");
        registry.add("spring.mail.password", () -> "testpassword");
    }

    /**
     * Tests if the number of countries retrieved by the API is correct.
     *
     * <p>
     * It sends a GET request to the endpoint that returns the list of countries and
     * verifies that the size of the list matches the expected count.
     * </p>
     *
     * @throws Exception if the request fails.
     */
    @Test
    @Transactional
    void testRetieveAllCountriesSize() throws Exception {
        mockMvc.perform(get(GET_COUNTRIES_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.countryList.size()", is(COUNTRIES_COUNT)));
    }

    /**
     * Tests the API response when a non-existing country code is requested.
     *
     * <p>
     * It sends a GET request to the endpoint that returns the list of subdivisions
     * for a country, using a code that doesn't exist. The test verifies that a bad
     * request response is returned.
     * </p>
     *
     * @throws Exception if the request fails.
     */
    @Test
    @Transactional
    void testRetieveAllCountriesSubdivisionsNotExistingCountryBadRequest() throws Exception {

        final var notExistingCountryCode = 999;
        final var getSpainSubdivisionsUrl = "/api/address/country/" + notExistingCountryCode;

        mockMvc.perform(get(getSpainSubdivisionsUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Tests if the number of subdivisions for Spain is correct.
     *
     * <p>
     * It sends a GET request to the endpoint that returns the list of subdivisions
     * for Spain and verifies that the size of the list matches the expected count.
     * </p>
     *
     * @throws Exception if the request fails.
     */
    @Test
    @Transactional
    void testRetieveAllCountriesSubdivisionsSize() throws Exception {
        final var spainCode = 724;
        final var getSpainSubdivisionsUrl = "/api/address/country/" + spainCode;
        mockMvc.perform(get(getSpainSubdivisionsUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.subCountryList.size()", is(SPAIN_SUBDIVISIONS)));
    }

}
