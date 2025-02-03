package es.org.cxn.backapp.test.integration.controller;

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

import es.org.cxn.backapp.model.form.requests.payments.CreatePaymentRequest;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
@AutoConfigureMockMvc()
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:IntegrationController.properties")
class PaymentsControllerIntegrationIT {

    /**
     * Gson instance for JSON serialization and deserialization.
     */
    private static Gson gson;

    /**
     * URL for the sign-up endpoint.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";

    // Constants for payment values
    /**
     * The payment amount for test payment requests.
     */
    private static final BigDecimal PAYMENT_AMOUNT = BigDecimal.valueOf(100.00);

    /**
     * The payment title used in test payment requests.
     */
    private static final String PAYMENT_TITLE = "Title Payment";

    /**
     * The payment description used in test payment requests.
     */
    private static final String PAYMENT_DESCRIPTION = "Payment description";

    /**
     * The category for the payment, used in test requests.
     */
    private static final String PAYMENT_CATEGORY = "FEDERATE_PAYMENT";

    /**
     * Mocked {@link DefaultEmailService} instance to test email-related
     * functionality. This service is used to send emails in the application, and it
     * is mocked to avoid actual email sending during tests.
     */
    @MockitoBean
    private DefaultEmailService defaultEmailService;

    /**
     * Mocked {@link SecurityContext} instance to provide the security context for
     * authentication. This is used to simulate the security context in tests
     * without needing to set up a full security environment.
     */
    @MockitoBean
    private SecurityContext securityContext;

    /**
     * Mocked {@link Authentication} instance to simulate the authenticated user in
     * tests. This is used to mock the authentication object for the current user
     * performing the request.
     */
    @MockitoBean
    private Authentication authentication;

    /**
     * MockMvc instance for performing HTTP requests and assertions in tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked service that sends messages to users through email.
     */
    @MockitoBean
    private JavaMailSender javaMailSender;

    /**
     * MimeMessage instance for testing email sending functionality.
     */
    private MimeMessage mimeMessage = new MimeMessage((Session) null);

    PaymentsControllerIntegrationIT() {
        super();
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

        // USER B
        var memberBRequest = UsersControllerFactory.getSignUpRequestFormUserB();
        var memberBRequestJson = gson.toJson(memberBRequest);
        try {
            mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(memberBRequestJson))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Test for creating and cancelling a payment successfully")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCancelPayment() throws Exception {
        String userDni = UsersControllerFactory.USER_A_DNI;

        CreatePaymentRequest request = new CreatePaymentRequest(userDni, PAYMENT_TITLE, PAYMENT_DESCRIPTION,
                PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_AMOUNT);
        final var jsonRequest = gson.toJson(request);

        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andReturn();

        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id");

        mockMvc.perform(patch("/api/payments/{paymentId}/cancel", paymentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(paymentId)))
                .andExpect(jsonPath("$.state", is("CANCELLED"))).andExpect(jsonPath("$.paidAt").value(nullValue()))
                .andExpect(jsonPath("$.title", is(PAYMENT_TITLE)))
                .andExpect(jsonPath("$.description", is(PAYMENT_DESCRIPTION)))
                .andExpect(jsonPath("$.category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$.amount", equalTo(PAYMENT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.userDni", is(userDni))).andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("Test for canceling a payment with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCancelPaymentWithNonExistentId() throws Exception {
        UUID nonExistentPaymentId = UUID.randomUUID();
        mockMvc.perform(patch("/api/payments/{paymentId}/cancel", nonExistentPaymentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test for creating a payment and retrieving its data")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testCreatePaymentGetPaymentData() throws Exception {
        String userDni = "32721860J";

        CreatePaymentRequest request = new CreatePaymentRequest(userDni, PAYMENT_TITLE, PAYMENT_DESCRIPTION,
                PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_AMOUNT);
        final var jsonRequest = gson.toJson(request);

        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.title", is(PAYMENT_TITLE)))
                .andExpect(jsonPath("$.description", is(PAYMENT_DESCRIPTION)))
                .andExpect(jsonPath("$.category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$.amount", equalTo(PAYMENT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.userDni", is(userDni))).andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.paidAt").value(nullValue())).andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id");

        mockMvc.perform(get("/api/payments/" + paymentId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentId))).andExpect(jsonPath("$.title", is(PAYMENT_TITLE)))
                .andExpect(jsonPath("$.description", is(PAYMENT_DESCRIPTION)))
                .andExpect(jsonPath("$.category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$.amount", equalTo(PAYMENT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.userDni", is(userDni))).andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.paidAt").value(nullValue()));
    }

    @Test
    @DisplayName("Test for retrieving payment info with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testGetPaymentInfoWithNonExistentId() throws Exception {
        UUID nonExistentPaymentId = UUID.randomUUID();
        mockMvc.perform(get("/api/payments/{paymentId}", nonExistentPaymentId)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test for making a payment and marking it as paid")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testMakePayment() throws Exception {
        String userDni = "32721860J";

        CreatePaymentRequest request = new CreatePaymentRequest(userDni, PAYMENT_TITLE, PAYMENT_DESCRIPTION,
                PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_AMOUNT);
        final var jsonRequest = gson.toJson(request);

        var result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andReturn();

        String paymentId = result.getResponse().getContentAsString();
        paymentId = JsonPath.read(paymentId, "$.id");

        mockMvc.perform(patch("/api/payments/{paymentId}/pay", paymentId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentId))).andExpect(jsonPath("$.paidAt").isNotEmpty())
                .andExpect(jsonPath("$.title", is(PAYMENT_TITLE)))
                .andExpect(jsonPath("$.description", is(PAYMENT_DESCRIPTION)))
                .andExpect(jsonPath("$.category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$.amount", equalTo(PAYMENT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.userDni", is(userDni))).andExpect(jsonPath("$.createdAt").isNotEmpty());

        mockMvc.perform(get("/api/payments/{paymentId}", paymentId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentId))).andExpect(jsonPath("$.paidAt").isNotEmpty())
                .andExpect(jsonPath("$.paidAt").value(notNullValue()));
    }

    @Test
    @DisplayName("Test for making a payment with a non-existent payment ID")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testmakePaymentWithNonExistentId() throws Exception {
        UUID nonExistentPaymentId = UUID.randomUUID();
        mockMvc.perform(patch("/api/payments/{paymentId}/pay", nonExistentPaymentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test creating payments for two users and retrieving payments for each")
    @Transactional
    @WithMockUser(username = "tesorero", roles = { "TESORERO" })
    void testTwoUsersCreatePaymentsRetrieveForOne() throws Exception {
        String firstUserDni = UsersControllerFactory.USER_A_DNI;
        String secondUserDni = UsersControllerFactory.USER_B_DNI;

        CreatePaymentRequest firstRequest = new CreatePaymentRequest(firstUserDni, PAYMENT_TITLE, PAYMENT_DESCRIPTION,
                PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_AMOUNT);
        final var firstJsonRequest = gson.toJson(firstRequest);

        var firstResult = mockMvc
                .perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(firstJsonRequest))
                .andReturn();
        String firstPaymentId = firstResult.getResponse().getContentAsString();
        firstPaymentId = JsonPath.read(firstPaymentId, "$.id");

        CreatePaymentRequest secondRequest = new CreatePaymentRequest(secondUserDni, PAYMENT_TITLE, PAYMENT_DESCRIPTION,
                PaymentsCategory.FEDERATE_PAYMENT, PAYMENT_AMOUNT);
        final var secondJsonRequest = gson.toJson(secondRequest);

        var secondResult = mockMvc
                .perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(secondJsonRequest))
                .andReturn();
        String secondPaymentId = secondResult.getResponse().getContentAsString();
        secondPaymentId = JsonPath.read(secondPaymentId, "$.id");

        mockMvc.perform(get("/api/payments/user/{userDni}", firstUserDni)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(firstPaymentId)))
                .andExpect(jsonPath("$[0].userDni", is(firstUserDni)))
                .andExpect(jsonPath("$[0].category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$[0].amount", equalTo(PAYMENT_AMOUNT.doubleValue())));

        mockMvc.perform(get("/api/payments/user/{userDni}", secondUserDni)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(secondPaymentId)))
                .andExpect(jsonPath("$[0].userDni", is(secondUserDni)))
                .andExpect(jsonPath("$[0].category", is(PAYMENT_CATEGORY)))
                .andExpect(jsonPath("$[0].amount", equalTo(PAYMENT_AMOUNT.doubleValue())));
    }
}
