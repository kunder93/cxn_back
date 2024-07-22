
package es.org.cxn.backapp.test.integration.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import es.org.cxn.backapp.service.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class AdressControllerIntegrationTest {

  private final static String GET_COUNTRIES_URL = "/api/address/getCountries";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  @BeforeEach
  void setup() {

  }

  private final static Integer COUNTRIES_COUNT = 2;
  private final static Integer SPAIN_SUBDIVISIONS = 50;

  /**
   * Main class constructor
   */
  public AdressControllerIntegrationTest() {
    super();
  }

  @Test
  @Transactional
  void testRetieveAllCountriesSize() throws Exception {

    mockMvc
          .perform(
                get(GET_COUNTRIES_URL).contentType(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(jsonPath("$.countryList.size()", is(COUNTRIES_COUNT)));
  }

  @Test
  @Transactional
  void testRetieveAllCountriesSubdivisionsSize() throws Exception {
    final var spainCode = 724;
    final var GET_SPAIN_SUBDIVISIONS__URL = "/api/address/country/" + spainCode;
    mockMvc.perform(
          get(GET_SPAIN_SUBDIVISIONS__URL)
                .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
          jsonPath("$.subCountryList.size()", is(SPAIN_SUBDIVISIONS))
    );
  }

  @Test
  @Transactional
  void testRetieveAllCountriesSubdivisionsNotExistingCountryBadRequest()
        throws Exception {
    final var notExistingCountryCode = 999;
    final var GET_SPAIN_SUBDIVISIONS__URL =
          "/api/address/country/" + notExistingCountryCode;
    mockMvc.perform(
          get(GET_SPAIN_SUBDIVISIONS__URL)
                .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

}
