
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;

import es.org.cxn.backapp.model.form.requests.AuthenticationRequest;
import es.org.cxn.backapp.model.form.requests.payments.CreatePaymentRequest;
import es.org.cxn.backapp.model.form.responses.AuthenticationResponse;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc()
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:IntegrationController.properties")
class PaymentsControllerIntegrationIT {

    /**
     * Gson instance used for converting Java objects to JSON and vice versa. This
     * static instance is used for serializing and deserializing request and
     * response payloads in the tests.
     */
    private static Gson gson;
    /**
     * URL endpoint for user sign-in. This static final string represents the URL
     * used for user authentication and generating JWT tokens.
     */
    private static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * URL endpoint for user registration (sign-up). This static final string
     * represents the URL used to create a new user account.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";

    /**
     * The email service mocked implementation.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * Mocked {@link SecurityContext} used in the test to simulate the security
     * context for authentication. It represents the context for a specific user's
     * security information, such as authentication and authorization details.
     */
    @MockitoBean
    private SecurityContext securityContext;

    /**
     * Mocked {@link Authentication} used in the test to simulate the authenticated
     * user information. This object contains details about the currently
     * authenticated user, such as the username, roles, and credentials.
     */
    @MockitoBean
    private Authentication authentication;

    /**
     * Provides the ability to perform HTTP requests and receive responses for
     * testing. Used for sending HTTP requests to the controllers and verifying the
     * responses in integration tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked mail sender.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * Mime message.
     */
    private MimeMessage mimeMessage = new MimeMessage((Session) null);

    /**
     * Main class constructor.
     */
    PaymentsControllerIntegrationIT() {
        super();

    }

    private String authenticateAndGetToken(final String email, final String password) throws Exception {
        var authRequest = new AuthenticationRequest(email, password);
        var authRequestJson = gson.toJson(authRequest);

        var response = mockMvc
                .perform(post(SIGN_IN_URL).contentType(MediaType.APPLICATION_JSON).content(authRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        var authResponse = gson.fromJson(response, AuthenticationResponse.class);
        return authResponse.jwt();
    }

    @BeforeEach
    void setup() {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        gson = UsersControllerFactory.GSON;

        // USER A
        var memberARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var memberARequestJson = gson.toJson(memberARequest);
        try {
            mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberARequestJson))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final var USER_A_JWT = authenticateAndGetToken(UsersControllerFactory.USER_A_EMAIL,
                    UsersControllerFactory.USER_A_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // USER B
        var memberBRequest = UsersControllerFactory.getSignUpRequestFormUserB();
        var memberBRequestJson = gson.toJson(memberBRequest);
        try {
            mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberBRequestJson))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final var USER_B_JWT = authenticateAndGetToken(UsersControllerFactory.USER_B_EMAIL,
                    UsersControllerFactory.USER_B_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Test case to create a payment and then cancel it, verifying that the payment
     * is correctly canceled.
     *
     * <p>
     * This test involves two main steps:
     * </p>
     * <ol>
     * <li>Creating a payment via a POST request</li>
     * <li>Cancelling the created payment via a PATCH request</li>
     * </ol>
     *
     * <p>
     * The test verifies the payment ID, status, and other properties like title,
     * description, and amount after the cancellation.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for creating and cancelling a payment successfully")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCancelPayment() throws Exception {
        // Variables inside the test method
        BigDecimal paymentAmount = BigDecimal.valueOf(100.00);
        String paymentCategory = "FEDERATE_PAYMENT";
        String userDni = UsersControllerFactory.USER_A_DNI;
        String paymentTitle = "Title Payment";
        String paymentDescription = "Payment description";

        // 1. Create a payment request
        CreatePaymentRequest request = new CreatePaymentRequest(userDni, paymentTitle, paymentDescription,
                PaymentsCategory.FEDERATE_PAYMENT, paymentAmount);
        final var jsonRequest = gson.toJson(request);

        // Perform POST request to create the payment
        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andReturn();

        // Extract the paymentId from the response using jsonPath
        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id");

        // 2. Perform PATCH request to cancel the payment
        mockMvc.perform(patch("/api/payments/{paymentId}/cancel", paymentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verify the response is OK
                .andExpect(jsonPath("$.id", is(paymentId))) // Verify the payment ID is the same
                .andExpect(jsonPath("$.state", is("CANCELLED"))) // Verify the payment state is 'CANCELLED'
                .andExpect(jsonPath("$.paidAt").value(nullValue())) // Verify paidAt is null
                .andExpect(jsonPath("$.title", is(paymentTitle))) // Verify payment title
                .andExpect(jsonPath("$.description", is(paymentDescription))) // Verify payment description
                .andExpect(jsonPath("$.category", is(paymentCategory))) // Verify payment category
                .andExpect(jsonPath("$.amount", equalTo(paymentAmount.doubleValue()))) // Compare amount as double
                .andExpect(jsonPath("$.userDni", is(userDni))) // Verify user DNI
                .andExpect(jsonPath("$.createdAt").isNotEmpty()); // Verify createdAt is not empty

    }

    /**
     * Test case for attempting to cancel a payment with a non-existent payment ID.
     *
     * <p>
     * This test simulates a scenario where the user tries to cancel a payment using
     * a payment ID that does not exist in the system.
     * </p>
     *
     * <p>
     * The test expects a 400 Bad Request status code as a response, indicating that
     * the payment with the given ID was not found.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for canceling a payment with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCancelPaymentWithNonExistentId() throws Exception {
        // Generate a random UUID for the non-existent payment ID
        UUID nonExistentPaymentId = UUID.randomUUID();

        // Perform the PATCH request to cancel the payment
        mockMvc.perform(patch("/api/payments/{paymentId}/cancel", nonExistentPaymentId))
                .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request status
    }

    /**
     * Test case for creating a payment and then retrieving the payment data by its
     * ID.
     *
     * <p>
     * This test simulates the creation of a payment, followed by a GET request to
     * retrieve the payment data using its ID.
     * </p>
     *
     * <p>
     * The test ensures that the created payment contains the correct data,
     * including title, description, category, amount, and more. It also verifies
     * that the GET request returns the correct payment data using the generated
     * payment ID.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for creating a payment and retrieving its data")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCreatePaymentGetPaymentData() throws Exception {
        // Variables inside the test method
        String userDni = "32721860J";
        String paymentTitle = "Title Payment";
        String paymentDescription = "Payment description";
        PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        BigDecimal paymentAmount = BigDecimal.valueOf(100.00);

        // Create the request body for creating a payment
        CreatePaymentRequest request = new CreatePaymentRequest(userDni, paymentTitle, paymentDescription,
                paymentCategory, paymentAmount);
        final var jsonRequest = gson.toJson(request);

        // Perform POST request to create a payment
        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated()) // Verify the response status
                .andExpect(jsonPath("$.title", is(paymentTitle))) // Verify payment title
                .andExpect(jsonPath("$.description", is(paymentDescription))) // Verify payment description
                .andExpect(jsonPath("$.category", is(paymentCategory.toString()))) // Verify payment category
                .andExpect(jsonPath("$.amount", equalTo(paymentAmount.doubleValue()))) // Compare as double
                .andExpect(jsonPath("$.userDni", is(userDni))) // Verify user DNI
                .andExpect(jsonPath("$.createdAt").isNotEmpty()) // Verify createdAt is not empty
                .andExpect(jsonPath("$.paidAt").value(nullValue())) // Verify paidAt is null
                .andExpect(jsonPath("$.id").isNotEmpty()) // Ensure id is not null or empty
                .andReturn();

        // Extract the payment ID from the response using jsonPath
        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id"); // Use JsonPath to extract the ID

        // Perform GET request to retrieve the payment data using the payment ID
        mockMvc.perform(get("/api/payments/" + paymentId)).andExpect(status().isOk()) // Verify the response status
                .andExpect(jsonPath("$.id", is(paymentId))) // Verify the payment ID matches
                .andExpect(jsonPath("$.title", is(paymentTitle))) // Verify payment title
                .andExpect(jsonPath("$.description", is(paymentDescription))) // Verify payment description
                .andExpect(jsonPath("$.category", is(paymentCategory.toString()))) // Verify payment category
                .andExpect(jsonPath("$.amount", equalTo(paymentAmount.doubleValue()))) // Compare as double
                .andExpect(jsonPath("$.userDni", is(userDni))) // Verify user DNI
                .andExpect(jsonPath("$.createdAt").isNotEmpty()) // Verify createdAt is not empty
                .andExpect(jsonPath("$.paidAt").value(nullValue())); // Verify paidAt is null

    }

    /**
     * Test case for retrieving payment info with a non-existent payment ID.
     *
     * <p>
     * This test simulates the retrieval of payment information for a payment ID
     * that does not exist.
     * </p>
     *
     * <p>
     * The test ensures that when a request is made with a non-existent payment ID,
     * the response returns a Bad Request (400) status, as the payment does not
     * exist in the system.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for retrieving payment info with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testGetPaymentInfoWithNonExistentId() throws Exception {
        // Generate a random UUID for the non-existent payment ID
        UUID nonExistentPaymentId = UUID.randomUUID();

        // Perform the PATCH request to cancel the payment
        mockMvc.perform(get("/api/payments/{paymentId}", nonExistentPaymentId)).andExpect(status().isBadRequest());
    }

    /**
     * Test case for making a payment and marking it as paid.
     *
     * <p>
     * This test simulates creating a payment and then marking it as paid. It
     * ensures that after making the payment, the payment's details (such as the
     * payment ID, paid status, and timestamps) are correctly updated.
     * </p>
     *
     * <p>
     * First, a POST request is made to create a payment, then a PATCH request is
     * performed to mark the payment as paid. Finally, a GET request is made to
     * verify that the payment's `paidAt` field is populated and the payment is
     * marked as paid.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for making a payment and marking it as paid")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testMakePayment() throws Exception {
        // Variables inside the test method
        String userDni = "32721860J";
        String paymentTitle = "Title Payment";
        String paymentDescription = "Payment description";
        PaymentsCategory paymentCategory = PaymentsCategory.FEDERATE_PAYMENT;
        BigDecimal paymentAmount = BigDecimal.valueOf(100.00);

        // Create the request body for creating a payment
        CreatePaymentRequest request = new CreatePaymentRequest(userDni, paymentTitle, paymentDescription,
                paymentCategory, paymentAmount);
        final var jsonRequest = gson.toJson(request);

        // Perform POST request to create a payment
        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andReturn();

        // Extract the payment ID from the response using jsonPath
        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id");

        // Perform PATCH request to mark the payment as paid
        mockMvc.perform(patch("/api/payments/{paymentId}/pay", paymentId)).andExpect(status().isOk()) // Verify the
                                                                                                      // response status
                                                                                                      // is OK
                .andExpect(jsonPath("$.id", is(paymentId))) // Verify the payment ID matches
                .andExpect(jsonPath("$.paidAt").isNotEmpty()) // Ensure paidAt is populated
                .andExpect(jsonPath("$.title", is(paymentTitle))) // Verify payment title
                .andExpect(jsonPath("$.description", is(paymentDescription))) // Verify payment description
                .andExpect(jsonPath("$.category", is(paymentCategory.toString()))) // Verify payment category
                .andExpect(jsonPath("$.amount", equalTo(paymentAmount.doubleValue()))) // Compare as double
                .andExpect(jsonPath("$.userDni", is(userDni))) // Verify user DNI
                .andExpect(jsonPath("$.createdAt").isNotEmpty()); // Verify createdAt is populated

        // Verify the payment status by fetching the payment data after it's marked as
        // paid
        mockMvc.perform(get("/api/payments/{paymentId}", paymentId)).andExpect(status().isOk()) // Verify the response
                                                                                                // status is OK
                .andExpect(jsonPath("$.id", is(paymentId))) // Verify the payment ID matches
                .andExpect(jsonPath("$.paidAt").isNotEmpty()) // Verify that paidAt is populated
                .andExpect(jsonPath("$.paidAt").value(notNullValue())); // Ensure that paidAt is not null
    }

    /**
     * Test case for attempting to make a payment with a non-existent payment ID.
     *
     * <p>
     * This test simulates a scenario where a payment is marked as paid using a
     * non-existent payment ID. It ensures that the system responds with a 400 Bad
     * Request status when an invalid payment ID is provided.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test for making a payment with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testmakePaymentWithNonExistentId() throws Exception {
        // Generate a random UUID for the non-existent payment ID
        UUID nonExistentPaymentId = UUID.randomUUID();

        // Perform the PATCH request to cancel the payment
        mockMvc.perform(patch("/api/payments/{paymentId}/pay", nonExistentPaymentId))
                .andExpect(status().isBadRequest()); // Expecting
        // status
    }

    /**
     * Test case for creating payments for two different users and retrieving
     * payments for each user.
     *
     * <p>
     * This test creates payments for two different users and verifies that each
     * user can only retrieve their own payments.
     * </p>
     *
     * @throws Exception if the request or response handling fails
     */
    @Test
    @DisplayName("Test creating payments for two users and retrieving payments for each")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testTwoUsersCreatePaymentsRetrieveForOne() throws Exception {
        // Variables inside the test method
        String firstUserDni = UsersControllerFactory.USER_A_DNI;
        String secondUserDni = UsersControllerFactory.USER_B_DNI;
        BigDecimal paymentAmount = BigDecimal.valueOf(100.00);
        String paymentCategory = "FEDERATE_PAYMENT";

        // Create the first payment request
        CreatePaymentRequest firstRequest = new CreatePaymentRequest(firstUserDni, "Title Payment",
                "Payment description", PaymentsCategory.FEDERATE_PAYMENT, paymentAmount);
        final var firstJsonRequest = gson.toJson(firstRequest);

        // Perform POST request to create the first payment
        var firstResult = mockMvc
                .perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(firstJsonRequest))
                .andReturn();
        // Extract the payment ID from the response using jsonPath
        String firstPaymentId = firstResult.getResponse().getContentAsString();
        firstPaymentId = JsonPath.read(firstPaymentId, "$.id");

        // Create the second payment request
        CreatePaymentRequest secondRequest = new CreatePaymentRequest(secondUserDni, "Title Payment",
                "Payment description", PaymentsCategory.FEDERATE_PAYMENT, paymentAmount);
        final var secondJsonRequest = gson.toJson(secondRequest);

        // Perform POST request to create the second payment
        var secondResult = mockMvc
                .perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(secondJsonRequest))
                .andReturn();
        // Extract the payment ID from the response using jsonPath
        String secondPaymentId = secondResult.getResponse().getContentAsString();
        secondPaymentId = JsonPath.read(secondPaymentId, "$.id");

        // Retrieve payments for USER_A
        mockMvc.perform(get("/api/payments/user/{userDni}", firstUserDni)).andExpect(status().isOk()) // Verify the
                                                                                                      // status is OK
                .andExpect(jsonPath("$", hasSize(1))) // Verify there is one payment for USER_A
                .andExpect(jsonPath("$[0].id", is(firstPaymentId))) // Check the first payment ID
                .andExpect(jsonPath("$[0].userDni", is(firstUserDni))) // Verify the user DNI
                .andExpect(jsonPath("$[0].category", is(paymentCategory))) // Verify the payment category
                .andExpect(jsonPath("$[0].amount", equalTo(paymentAmount.doubleValue()))); // Compare the amount as
                                                                                           // double

        // Retrieve payments for USER_B
        mockMvc.perform(get("/api/payments/user/{userDni}", secondUserDni)).andExpect(status().isOk()) // Verify the
                                                                                                       // status is OK
                .andExpect(jsonPath("$", hasSize(1))) // Verify there is one payment for USER_B
                .andExpect(jsonPath("$[0].id", is(secondPaymentId))) // Check the second payment ID
                .andExpect(jsonPath("$[0].userDni", is(secondUserDni))) // Verify the user DNI
                .andExpect(jsonPath("$[0].category", is(paymentCategory))) // Verify the payment category
                .andExpect(jsonPath("$[0].amount", equalTo(paymentAmount.doubleValue()))); // Compare the amount as
                                                                                           // double

    }
}
