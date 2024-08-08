
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;
import es.org.cxn.backapp.service.DefaultCompanyService;
import es.org.cxn.backapp.service.JwtUtils;
import es.org.cxn.backapp.test.utils.CompanyControllerFactory;
import es.org.cxn.backapp.test.utils.InvoicesControllerFactory;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class InvoiceControllerIntegrationTest {

  private final static String INVOICE_URL = "/api/invoice";
  private final static String COMPANY_URL = "/api/company";

  private static Gson gson;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  @BeforeAll
  static void setup() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

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
   * Main class constructor
   */
  public InvoiceControllerIntegrationTest() {
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
    final var NO_EXISTING_COMPANY_NIFCIF = "4125112-U";

    // invoice request with no existing seller and buyer
    var invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm.setBuyerNif(NO_EXISTING_COMPANY_NIFCIF);
    invoiceRequestForm.setSellerNif(NO_EXISTING_COMPANY_NIFCIF);

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
          responseContent
                .contains(DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE),
          "Expect company not found service message"
    );

    // invoice request with no existing buyer
    invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm.setBuyerNif(NO_EXISTING_COMPANY_NIFCIF);

    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    responseContent = mockMvc
          .perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(invoiceRequestFormJSon)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Explanation contains the problem
    Assertions.assertTrue(
          responseContent
                .contains(DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE),
          "Expect company not found service message"
    );

    // invoice request with no existing seller
    invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm.setSellerNif(NO_EXISTING_COMPANY_NIFCIF);

    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

    responseContent = mockMvc
          .perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(invoiceRequestFormJSon)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn()
          .getResponse().getContentAsString();
    // Explanation contains the problem
    Assertions.assertTrue(
          responseContent
                .contains(DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE),
          "Expect company not found service message."
    );
  }

  @Test
  @Transactional
  void testCreateTwoInvoicesWithSameBuyerSellerDeleteOneBuyerSellerNotRemoved()
        throws Exception {
    var invoiceRequestFormJSon =
          InvoicesControllerFactory.getInvoiceARequestJson();

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice with same buyer and seller
    var invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm
          .setNumber(InvoicesControllerFactory.INVOICE_A_NUMBER + 1);
    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(invoiceRequestFormJSon)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    //Remove first invoice
    // Delete second invoice which is not stored.

    var secondInvoiceNumber = InvoicesControllerFactory.INVOICE_A_NUMBER + 1;
    mockMvc.perform(
          delete(
                INVOICE_URL + "/" + InvoicesControllerFactory.INVOICE_A_SERIES
                      + "/" + secondInvoiceNumber
          ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @Transactional
  void testCreateInvoiceSameBuyerSellerNoValid() throws Exception {

    // Prepare invoice request, same seller and buyer
    var invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm.setBuyerNif(invoiceRequestForm.getSellerNif());
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
    var invoicesListResponse = mockMvc
          .perform(
                get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    // Create first invoice.

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // Retrieve invoices from controller.
    invoicesListResponse = mockMvc
          .perform(
                get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    // Create second invoice

    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // Retrieve invoices from controller.
    invoicesListResponse = mockMvc
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
    var secondInvoiceRequest =
          InvoicesControllerFactory.createInvoiceBRequest();
    secondInvoiceRequest.setNumber(InvoicesControllerFactory.INVOICE_A_NUMBER);
    secondInvoiceRequest.setSeries(InvoicesControllerFactory.INVOICE_A_SERIES);

    var secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
    // Second invoice with same number and series returns bad request
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(secondInvoiceRequestJSon)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    // If we change only the invoice series invoice will be create
    secondInvoiceRequest.setSeries(InvoicesControllerFactory.INVOICE_B_SERIES);
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
    ;

    var response = gson.fromJson(responseJson, InvoiceResponse.class);

    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_NUMBER, response.getNumber(),
          "Invoice number."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_SERIES, response.getSeries(),
          "Invoice series."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_BUYER, response.getBuyerNif(),
          "Buyer NIF."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_SELLER, response.getSellerNif(),
          "Seller NIF."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_TAX_EXEMPT,
          response.getTaxExempt(), "Tax exempt."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_EXPEDITION_DATE,
          response.getExpeditionDate(), "Expedition date."
    );
    Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_A_PAYMENT_DATE,
          response.getAdvancePaymentDate(), "Advance payment date."
    );
  }

  @Test
  @Transactional
  void testCreateTwoInvoicesRetrieveDataFromNotExistingInvoiceNumber()
        throws Exception {
    final var notExistingInvoiceNumber =
          InvoicesControllerFactory.INVOICE_A_NUMBER + 22;

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
