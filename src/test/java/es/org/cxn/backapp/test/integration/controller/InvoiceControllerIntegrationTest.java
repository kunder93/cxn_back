
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequest;
import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;
import es.org.cxn.backapp.service.DefaultCompanyService;
import es.org.cxn.backapp.test.utils.CompanyControllerFactory;
import es.org.cxn.backapp.test.utils.InvoicesControllerFactory;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the
 * {@link es.org.cxn.backapp.controller.InvoiceController}.
 * This test class verifies the end-to-end functionality of the Invoice API,
 * ensuring that various invoice-related operations behave as expected within
 * the application's context.
 *
 * <p>These tests involve interactions with the mock MVC framework to simulate
 * HTTP requests and responses without requiring a running server.</p>
 *
 * <p>The tests cover scenarios such as creating invoices, validating that buyer
 * and seller exist, handling duplicate invoice numbers, and verifying data
 * integrity after CRUD operations.</p>
 *
 * <p>Test configuration is sourced from the application.properties file,
 * ensuring that the tests run in an isolated environment.</p>
 *
 * <p>Author: Santiago Paz.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class InvoiceControllerIntegrationTest {

  /**
   * URL endpoint for accessing invoice-related operations.
   */
  private static final String INVOICE_URL = "/api/invoice";

  /**
   * URL endpoint for accessing company-related operations.
   */
  private static final String COMPANY_URL = "/api/company";

  /**
   * Gson instance for serializing and deserializing JSON objects, customized
   * with a {@link LocalDateAdapter} to handle {@link LocalDate} objects.
   */
  private static Gson gson;

  /**
   * MockMvc instance used to perform HTTP requests in the integration tests.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Nif of company that not exists, not found.
   */
  private static final String NO_EXISTING_COMPANY_NIFCIF = "4125112-U";

  /**
   * Initializes the Gson instance before any tests are executed.
   *
   * <p>This method registers a custom {@link LocalDateAdapter} to handle
   * the serialization and deserialization of {@link LocalDate} objects.</p>
   *
   * <p>Annotated with {@link BeforeAll} to ensure that it runs once before
   * any test methods in this class.</p>
   */
  @BeforeAll
  static void setup() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

  /**
   * Sets up the test environment before each individual test case is executed.
   *
   * <p>This method creates two companies by performing POST requests to the
   * company API endpoint. These companies are used as buyers and sellers
   * in the invoice-related tests.</p>
   *
   * <p>Annotated with {@link BeforeEach} to ensure that it runs before every
   * test method in this class.</p>
   *
   * @throws Exception if an error occurs while setting up the test data.
   */
  @BeforeEach
  void testSetup() throws Exception {
    // Create company
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(CompanyControllerFactory.getCompanyARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second company
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(CompanyControllerFactory.getCompanyBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  /**
   * Main class constructor.
   */
  InvoiceControllerIntegrationTest() {
    super();
  }

  @Test
  @Transactional
  void testCreateInvoiceCheckDataIsOk() throws Exception {

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @Transactional
  void testCreateInvoiceNoExistingBuyerSeller() throws Exception {

    // invoice request with no existing seller and buyer
    var invoiceRequestForm = new CreateInvoiceRequest(
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,

          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          NO_EXISTING_COMPANY_NIFCIF, NO_EXISTING_COMPANY_NIFCIF,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE
    );

    var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    // Create invoice
    var responseContent = mockMvc
          .perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(invoiceRequestFormJSon)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Explanation contains the problem
    Assertions.assertTrue(
          responseContent.contains(DefaultCompanyService.COMPANY_NOT_FOUND),
          "Expect company not found service message"
    );

    // invoice request with no existing buyer
    invoiceRequestForm = new CreateInvoiceRequest(
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          NO_EXISTING_COMPANY_NIFCIF,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE
    );

    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    responseContent = mockMvc
          .perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(invoiceRequestFormJSon)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Explanation contains the problem
    Assertions.assertTrue(
          responseContent.contains(DefaultCompanyService.COMPANY_NOT_FOUND),
          "Expect company not found service message"
    );

    // invoice request with no existing seller
    invoiceRequestForm = new CreateInvoiceRequest(
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          NO_EXISTING_COMPANY_NIFCIF, InvoicesControllerFactory.INVOICE_A_BUYER,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE
    );

    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    responseContent = mockMvc
          .perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(invoiceRequestFormJSon)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Explanation contains the problem
    Assertions.assertTrue(
          responseContent.contains(DefaultCompanyService.COMPANY_NOT_FOUND),
          "Expect company not found service message."
    );
  }

  @Test
  @Transactional
  void testCreateTwoInvoicesWithSameBuyerSellerDeleteOneBuyerSellerNotRemoved()
        throws Exception {

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice with same buyer and seller
    var invoiceRequestForm = new CreateInvoiceRequest(
          InvoicesControllerFactory.INVOICE_B_NUMBER,
          InvoicesControllerFactory.INVOICE_B_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          InvoicesControllerFactory.INVOICE_A_BUYER,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE
    );

    var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(invoiceRequestFormJSon)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    //Remove first invoice
    // Delete second invoice which is not stored.

    var secondInvoiceNumber = InvoicesControllerFactory.INVOICE_B_NUMBER;
    mockMvc.perform(
          delete(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_B_SERIES
                      + "/" + secondInvoiceNumber
          ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @Transactional
  void testCreateInvoiceSameBuyerSellerNoValid() throws Exception {

    // Prepare invoice request, same seller and buyer
    var invoiceRequestForm = new CreateInvoiceRequest(
          InvoicesControllerFactory.INVOICE_B_NUMBER,
          InvoicesControllerFactory.INVOICE_B_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE
    );
    final var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    // Create invoice, expect bad request due same buyer and seller.
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(invoiceRequestFormJSon)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testCreateDeleteInvoices() throws Exception {

    // Retrieve invoices from controller.
    mockMvc.perform(
          get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Create first invoice.

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // Retrieve invoices from controller.
    mockMvc.perform(
          get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Create second invoice

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // Retrieve invoices from controller.
    var invoicesListResponse = mockMvc
          .perform(
                get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
    // Create expected invoices response list.
    var invoicesList = new ArrayList<InvoiceResponse>();
    invoicesList.add(
          new InvoiceResponse(
                InvoicesControllerFactory.INVOICE_A_NUMBER,
                InvoicesControllerFactory.INVOICE_A_SERIES,
                InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
                InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE,
                InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
                InvoicesControllerFactory.INVOICE_A_SELLER,
                InvoicesControllerFactory.INVOICE_A_BUYER
          )
    );
    invoicesList.add(
          new InvoiceResponse(
                InvoicesControllerFactory.INVOICE_B_NUMBER,
                InvoicesControllerFactory.INVOICE_B_SERIES,
                InvoicesControllerFactory.INVOICE_B_PAYMENT_DATE,
                InvoicesControllerFactory.INVOICE_B_EXPEDITION_DATE,
                InvoicesControllerFactory.INVOICE_B_TAX_EXEMPT,
                InvoicesControllerFactory.INVOICE_B_SELLER,
                InvoicesControllerFactory.INVOICE_B_BUYER
          )
    );
    var expectedInvoiceListResponse = new InvoiceListResponse(invoicesList);
    var gsonExpectedInvoiceListResponse =
          gson.toJson(expectedInvoiceListResponse);
    Assertions.assertEquals(
          gsonExpectedInvoiceListResponse, invoicesListResponse,
          "Check invoices list retrieved."
    );

    // Delete first invoice.
    mockMvc.perform(
          delete(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_A_SERIES
                      + "/" + InvoicesControllerFactory.INVOICE_A_NUMBER
          ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Delete second invoice.
    mockMvc.perform(
          delete(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_B_SERIES
                      + "/" + InvoicesControllerFactory.INVOICE_B_NUMBER
          ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Get all invoices, expect 0 invoices, all has been deleted.

    var controllerResponse = mockMvc
          .perform(get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    // Create the expected invoice list after delete all invoices, this is a
    // empty list.
    var invoiceResponseList = new ArrayList<InvoiceResponse>();
    var invoiceListExpectedResponse =
          new InvoiceListResponse(invoiceResponseList);
    var invoiceListResponseJson = gson.toJson(invoiceListExpectedResponse);
    Assertions.assertEquals(
          invoiceListResponseJson, controllerResponse,
          "Expect empty invoices list"
    );
  }

  @Test
  @Transactional
  void testDeleteNotExistingInvoiceBadRequest() throws Exception {

    // Delete second invoice which is not stored.
    mockMvc.perform(
          delete(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_B_SERIES
                      + "/" + InvoicesControllerFactory.INVOICE_B_NUMBER
          ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testCreateInvoiceExistingNumberInExistingSerieNotValidBadRequest()
        throws Exception {
    // Create first invoice.

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice
    var secondInvoiceRequest = new InvoiceResponse(
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES,
          InvoicesControllerFactory.INVOICE_B_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_B_EXPEDITION_DATE,
          InvoicesControllerFactory.INVOICE_B_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_B_SELLER,
          InvoicesControllerFactory.INVOICE_B_BUYER
    );

    var secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
    // Second invoice with same number and series returns bad request
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(secondInvoiceRequestJSon)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    // If we change only the invoice series invoice will be create
    secondInvoiceRequest = new InvoiceResponse(
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_B_SERIES,
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE,
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          InvoicesControllerFactory.INVOICE_A_BUYER
    );
    secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(secondInvoiceRequestJSon)
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @Transactional
  void testCreateTwoInvoicesRetrieveDataFromFirstCheckData() throws Exception {
    // Create first invoice.

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice.
    // Create first invoice.

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    //Retrieve data from first invoice
    var responseJson = mockMvc
          .perform(
                get(
                      INVOICE_URL + "/"
                            + InvoicesControllerFactory.INVOICE_A_SERIES + "/"
                            + InvoicesControllerFactory.INVOICE_A_NUMBER
                )
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var response = gson.fromJson(responseJson, InvoiceResponse.class);

    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_NUMBER, response.number(),
          "Invoice number."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_SERIES, response.series(),
          "Invoice series."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_BUYER, response.buyerNif(),
          "Buyer NIF."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_SELLER, response.sellerNif(),
          "Seller NIF."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT, response.taxExempt(),
          "Tax exempt."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE,
          response.expeditionDate(), "Expedition date."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          response.advancePaymentDate(), "Advance payment date."
    );
  }

  @Test
  @Transactional
  void testCreateTwoInvoicesRetrieveDataFromNotExistingInvoiceNumber()
        throws Exception {
    final var notExistingInvoiceNumber =
          InvoicesControllerFactory.INVOICE_A_NUMBER
                .add(BigInteger.valueOf(22));

    // Create first invoice.
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice.
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    //Retrieve data from first invoice
    mockMvc.perform(
          get(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_A_SERIES
                      + "/" + notExistingInvoiceNumber
          )
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

}
