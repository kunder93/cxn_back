package es.org.cxn.backapp.test.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import es.org.cxn.backapp.service.JwtUtils;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class UserControllerIntegrationTest {

    private final static String SIGN_UP_URL = "/api/auth/signup";
    private final static String SIGN_IN_URL = "/api/auth/signinn";
    private final static String GET_USER_DATA_URL = "/api/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserDetailsService myUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @BeforeEach
    void setup() {

    }

    /**
     * Main class constructor
     */
    public UserControllerIntegrationTest() {
        super();
    }

}
