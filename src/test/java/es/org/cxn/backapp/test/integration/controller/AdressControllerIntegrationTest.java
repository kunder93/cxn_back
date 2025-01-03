
package es.org.cxn.backapp.test.integration.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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
@TestPropertySource("/application.properties")
class AdressControllerIntegrationTest {

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
     * Mocked mail sender.
     */
    @MockBean
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
    AdressControllerIntegrationTest() {
        super();
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
