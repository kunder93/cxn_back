
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CompanyUpdateRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.CompanyListResponse;
import es.org.cxn.backapp.model.form.responses.CompanyResponse;
import es.org.cxn.backapp.model.form.responses.CompanyUpdateResponse;
import es.org.cxn.backapp.service.DefaultCompanyService;
import es.org.cxn.backapp.service.JwtUtils;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import utils.LocalDateAdapter;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class CompanyControllerIntegrationTest {

  private final static String COMPANY_URL = "/api/company";

  private final static String COMPANY_NIF = "45235234G";
  private final static String COMPANY_NAME = "MyCompanyName";
  private final static String COMPANY_ADDRESS = "MyCompanyAddress";

  private final static String SECOND_COMPANY_NIFCIF = "99988834-G";
  private final static String SECOND_COMPANY_NAME = "SecondCompanyName";
  private final static String SECOND_COMPANY_ADDRESS = "SecondCompanyAddress";
  private final static String UPDATED_COMPANY_NAME = "New Company Name";
  private final static String UPDATED_COMPANY_ADDRESS = "New Company Address";

  private final static String INVOICE_URL = "/api/invoice";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  private static String companyRequestJson;
  private static String sameNifCifCompanyRequestJson;
  private static String secondCompanyRequestJson;
  private static String companyUpdateRequestJson;
  private static String companyUpdateResponseJson;

  @BeforeAll
  public static void setup() {
    var companyRequest = new CreateCompanyRequestForm(
          COMPANY_NIF, COMPANY_NAME, COMPANY_ADDRESS
    );
    var sameNifCifCompanyRequest = new CreateCompanyRequestForm(
          COMPANY_NIF, SECOND_COMPANY_NAME, SECOND_COMPANY_ADDRESS
    );

    var secondCompanyRequest = new CreateCompanyRequestForm(
          SECOND_COMPANY_NIFCIF, SECOND_COMPANY_NAME, SECOND_COMPANY_ADDRESS
    );

    var updateCompanyRequest = new CompanyUpdateRequestForm(
          UPDATED_COMPANY_NAME, UPDATED_COMPANY_ADDRESS
    );

    var updateCompanyResponse = new CompanyUpdateResponse(
          COMPANY_NIF, UPDATED_COMPANY_NAME, UPDATED_COMPANY_ADDRESS
    );

    companyRequestJson = new Gson().toJson(companyRequest);
    sameNifCifCompanyRequestJson = new Gson().toJson(sameNifCifCompanyRequest);
    secondCompanyRequestJson = new Gson().toJson(secondCompanyRequest);
    companyUpdateRequestJson = new Gson().toJson(updateCompanyRequest);
    companyUpdateResponseJson = new Gson().toJson(updateCompanyResponse);
  }

  /**
   * Main class constructor
   */
  public CompanyControllerIntegrationTest() {
    super();
  }

  @Test
  @Transactional
  void testCreateCompanyCheckResponseIsOk() throws Exception {
    // Do request, expect controller status created
    var requestResult =
          mockMvc
                .perform(
                      post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                            .content(companyRequestJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

    var response =
          new CompanyResponse(COMPANY_NIF, COMPANY_NAME, COMPANY_ADDRESS);
    var gson = new Gson();
    var responseJson = gson.toJson(response);
    // Response content is the company data created.
    Assertions.assertEquals(
          requestResult.getResponse().getContentAsString(), responseJson,
          "create company returns company with data created"
    );
  }

  /**
   * Using a existing Nif for create company raises a bad request response
   * with explanation message.
   *
   * @throws Exception the test exception.
   */
  @Test
  @Transactional
  void testCreateCompanyExistingNifBadRequest() {
    try {
      // Create first company.
      mockMvc.perform(
            post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                  .content(companyRequestJson)
      ).andExpect(MockMvcResultMatchers.status().isCreated());

      // Try create second company with same nifcif. Expect bad request as
      // request status.
      var responseResultContent = mockMvc
            .perform(
                  post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(sameNifCifCompanyRequestJson)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

      // Response contains explanation message.
      Assertions.assertTrue(
            responseResultContent
                  .contains(DefaultCompanyService.COMPANY_EXISTS_MESSAGE),
            "contains nif cif which is not valid"
      );
    } catch (Exception e) {

    }
  }

  @Test
  @Transactional
  void testRemoveCompanyAndRetrieveAllCompaniesEmpty() throws Exception {
    // Create company expect created.
    mockMvc
          .perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(companyRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse();

    // Delete company, expect ok status
    mockMvc
          .perform(
                delete(COMPANY_URL + "/" + COMPANY_NIF)
                      .contentType(MediaType.APPLICATION_JSON)

          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    // Get all companies
    var companiesList = mockMvc
          .perform(
                get(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)

          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    var expectedCompaniesList = new CompanyListResponse(new ArrayList<>());
    final var companyListJSon = new Gson().toJson(expectedCompaniesList);
    // Received company list is empty.
    Assertions
          .assertEquals(companyListJSon, companiesList, "empty companies list");
  }

  @Test
  @Transactional
  void testDeleteNotExistingCompanyBadRequest() throws Exception {
    // Delete no existing company expect bad request as response status.
    var deleteCompanyResult = mockMvc
          .perform(
                delete(COMPANY_URL + "/" + COMPANY_NIF)
                      .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Response contains not existing nifcif.
    Assertions.assertTrue(
          deleteCompanyResult
                .contains(DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE),
          "company not exists service message"
    );
  }

  @Test
  @Transactional
  void testUpdateExistingCompany() throws Exception {
    // Create company expect created.
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(companyRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
    // Update company with new data.
    var updateResponse = mockMvc
          .perform(
                put(COMPANY_URL + "/" + COMPANY_NIF)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(companyUpdateRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse();

    Assertions.assertEquals(
          updateResponse.getContentAsString(), companyUpdateResponseJson,
          "Check update response"
    );
  }

  @Test
  @Transactional
  void testUpdateNotExistingCompany() throws Exception {
    // Update company which not exists response with bad request status.
    mockMvc.perform(
          put(COMPANY_URL + "/" + COMPANY_NIF)
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyUpdateRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testDeleteCompanyWithInvoicesNotValid() throws Exception {
    final var INVOICE_A_NUMBER = 0001;
    final var INVOICE_A_SERIES = "IAS";
    final var INVOICE_A_EXPEDITION_DATE = LocalDate.of(2020, 1, 8);
    final var INVOICE_A_TAX_EXEMPT = Boolean.TRUE;
    final var INVOICE_A_BUYER = COMPANY_NIF;
    final var INVOICE_A_SELLER = SECOND_COMPANY_NIFCIF;
    final var INVOICE_A_PAYMENT_DATE = LocalDate.of(2020, 1, 24);

    // Create first company.
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(companyRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create Second company.
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(secondCompanyRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create invoice using first company as Seller and second as Buyer.
    // Create first invoice.
    var invoiceRequest = new CreateInvoiceRequestForm(
          INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
          INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT, INVOICE_A_SELLER,
          INVOICE_A_BUYER
    );
    var gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
    var invoiceRequestJSon = gson.toJson(invoiceRequest);
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(invoiceRequestJSon)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Deleting company is not possible, catch message.
    mockMvc
          .perform(
                delete(COMPANY_URL + "/" + COMPANY_NIF)
                      .contentType(MediaType.APPLICATION_JSON)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();

  }

}
