package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;
import es.org.cxn.backapp.service.DefaultCompanyService;
import es.org.cxn.backapp.service.JwtUtils;

/**
 * @author Santiago Paz. User controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class InvoiceControllerIntegrationTest {

    private final static String INVOICE_URL = "/api/invoice";
    private final static String COMPANY_URL = "/api/company";

    private final static String COMPANY_A_NIFCIF = "45235234-G";
    private final static String COMPANY_A_NAME = "MyCompanyName";
    private final static String COMPANY_A_IDTAXNUMBER = "MyCompTaxNmbr";
    private final static String COMPANY_A_ADDRESS = "MyCompanyAddress";

    private final static String COMPANY_B_NIFCIF = "33344434-G";
    private final static String COMPANY_B_NAME = "OtherCompanyName";
    private final static String COMPANY_B_IDTAXNUMBER = "OtherCompTaxNmbr";
    private final static String COMPANY_B_ADDRESS = "OtherCompanyAddress";
    private static String createCompanyARequestJson;
    private static String createCompanyBRequestJson;

    private final static int INVOICE_A_NUMBER = 0001;
    private final static String INVOICE_A_SERIES = "IAS";
    private final static LocalDate INVOICE_A_EXPEDITION_DATE = LocalDate
            .of(2020, 1, 8);
    private final static Boolean INVOICE_A_TAX_EXEMPT = Boolean.TRUE;
    private final static String INVOICE_A_BUYER = COMPANY_B_NIFCIF;
    private final static String INVOICE_A_SELLER = COMPANY_A_NIFCIF;
    private final static LocalDate INVOICE_A_PAYMENT_DATE = LocalDate
            .of(2020, 1, 24);

    private final static int INVOICE_B_NUMBER = 2;
    private final static String INVOICE_B_SERIES = "IBS";
    private final static LocalDate INVOICE_B_EXPEDITION_DATE = LocalDate
            .of(2020, 1, 16);
    private final static Boolean INVOICE_B_TAX_EXEMPT = Boolean.TRUE;
    private final static String INVOICE_B_BUYER = COMPANY_B_NIFCIF;
    private final static String INVOICE_B_SELLER = COMPANY_A_NIFCIF;
    private final static LocalDate INVOICE_B_PAYMENT_DATE = LocalDate
            .of(2020, 1, 5);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserDetailsService myUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @BeforeAll
    static void setup() {
        var createCompanyARequest = new CreateCompanyRequestForm(
                COMPANY_A_NIFCIF, COMPANY_A_NAME, COMPANY_A_IDTAXNUMBER,
                COMPANY_A_ADDRESS
        );
        var createCompanyBRequest = new CreateCompanyRequestForm(
                COMPANY_B_NIFCIF, COMPANY_B_NAME, COMPANY_B_IDTAXNUMBER,
                COMPANY_B_ADDRESS
        );
        createCompanyARequestJson = new Gson().toJson(createCompanyARequest);
        createCompanyBRequestJson = new Gson().toJson(createCompanyBRequest);
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDate> {

        @Override
        public JsonElement serialize(
                LocalDate src, Type typeOfSrc, JsonSerializationContext context
        ) {
            return new JsonPrimitive(
                    src.format(DateTimeFormatter.ISO_LOCAL_DATE)
            );
        }
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

        // Create company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create second company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyBRequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Prepare invoice request
        var invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, INVOICE_A_BUYER
        );
        var gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        final var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

        // Create invoice
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceRequestFormJSon)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Transactional
    void testCreateInvoiceNoExistingBuyerSeller() throws Exception {
        final var NO_EXISTING_COMPANY_NIFCIF = "4125112-U";

        // invoice request with no existing seller and buyer
        var invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                NO_EXISTING_COMPANY_NIFCIF, NO_EXISTING_COMPANY_NIFCIF
        );
        var gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

        // Create invoice
        var responseContent = mockMvc
                .perform(
                        post(INVOICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invoiceRequestFormJSon)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        // Explanation contains the problem
        Assertions.assertTrue(
                responseContent.contains(
                        DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE
                ), "Expect company not found service message"
        );

        // Create company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // invoice request with no existing buyer
        invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, NO_EXISTING_COMPANY_NIFCIF
        );
        invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

        responseContent = mockMvc
                .perform(
                        post(INVOICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invoiceRequestFormJSon)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        // Explanation contains the problem
        Assertions.assertTrue(
                responseContent.contains(
                        DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE
                ), "Expect company not found service message"
        );

        // invoice request with no existing seller
        invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                NO_EXISTING_COMPANY_NIFCIF, INVOICE_A_BUYER
        );
        invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

        responseContent = mockMvc
                .perform(
                        post(INVOICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invoiceRequestFormJSon)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        // Explanation contains the problem
        Assertions.assertTrue(
                responseContent.contains(
                        DefaultCompanyService.COMPANY_NOT_FOUND_MESSAGE
                ), "Expect company not found service message."
        );
    }

    @Test
    @Transactional
    void testCreateInvoicesSameSeriesNumberNoValid() throws Exception {

        // Create company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create second company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyBRequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Prepare invoice request
        var invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, INVOICE_A_BUYER
        );
        var gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        final var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);

        // Create invoice
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceRequestFormJSon)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create second invoice with same number and series
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceRequestFormJSon)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    void testCreateInvoiceSameBuyerSellerNoValid() throws Exception {
        // Create company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Prepare invoice request, same seller and buyer
        var invoiceRequestForm = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, INVOICE_A_SELLER
        );
        var gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
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

        // Create first company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create Second company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyBRequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create first invoice.
        var invoiceRequest = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, INVOICE_A_BUYER
        );
        var gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        var invoiceRequestJSon = gson.toJson(invoiceRequest);

        var firstInvoiceResponse = mockMvc
                .perform(
                        post(INVOICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invoiceRequestJSon)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Retrieve invoices from controller.
        invoicesListResponse = mockMvc
                .perform(
                        get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

        // Create second invoice
        var secondInvoiceRequest = new CreateInvoiceRequestForm(
                INVOICE_B_NUMBER, INVOICE_B_SERIES, INVOICE_B_PAYMENT_DATE,
                INVOICE_B_EXPEDITION_DATE, INVOICE_B_TAX_EXEMPT,
                INVOICE_B_SELLER, INVOICE_B_BUYER
        );

        var secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
        var secondInvoiceResponse = mockMvc
                .perform(
                        post(INVOICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(secondInvoiceRequestJSon)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

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
                        INVOICE_A_NUMBER, INVOICE_A_SERIES,
                        INVOICE_A_PAYMENT_DATE, INVOICE_A_EXPEDITION_DATE,
                        INVOICE_A_TAX_EXEMPT, INVOICE_A_SELLER, INVOICE_A_BUYER
                )
        );
        invoicesList.add(
                new InvoiceResponse(
                        INVOICE_B_NUMBER, INVOICE_B_SERIES,
                        INVOICE_B_PAYMENT_DATE, INVOICE_B_EXPEDITION_DATE,
                        INVOICE_B_TAX_EXEMPT, INVOICE_B_SELLER, INVOICE_B_BUYER
                )
        );
        var expectedInvoiceListResponse = new InvoiceListResponse(invoicesList);
        var gsonExpectedInvoiceListResponse = gson
                .toJson(expectedInvoiceListResponse);
        Assertions.assertEquals(
                gsonExpectedInvoiceListResponse, invoicesListResponse,
                "Check invoices list retrieved."
        );

        // Delete first invoice.
        mockMvc.perform(
                delete(
                        INVOICE_URL + "/" + INVOICE_A_SERIES + "/"
                                + INVOICE_A_NUMBER
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        // Delete second invoice.
        mockMvc.perform(
                delete(
                        INVOICE_URL + "/" + INVOICE_B_SERIES + "/"
                                + INVOICE_B_NUMBER
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        // Get all invoices, expect 0 invoices, all has been deleted.

        var controllerResponse = mockMvc
                .perform(
                        get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

        // Create the expected invoice list after delete all invoices, this is a
        // empty list.
        var invoiceResponseList = new ArrayList<InvoiceResponse>();
        var invoiceListExpectedResponse = new InvoiceListResponse(
                invoiceResponseList
        );
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
                        INVOICE_URL + "/" + INVOICE_B_SERIES + "/"
                                + INVOICE_B_NUMBER
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    void testCreateInvoiceSameNumberInSerieNotValidBadRequest()
            throws Exception {

        // Create first company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyARequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create Second company
        mockMvc.perform(
                post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(createCompanyBRequestJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create first invoice.
        var invoiceRequest = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
                INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT,
                INVOICE_A_SELLER, INVOICE_A_BUYER
        );
        var gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        var invoiceRequestJSon = gson.toJson(invoiceRequest);
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceRequestJSon)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Create second invoice
        var secondInvoiceRequest = new CreateInvoiceRequestForm(
                INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_B_PAYMENT_DATE,
                INVOICE_B_EXPEDITION_DATE, INVOICE_B_TAX_EXEMPT,
                INVOICE_B_SELLER, INVOICE_B_BUYER
        );

        var secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(secondInvoiceRequestJSon)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        // If we change only the invoice series invoice will be create
        secondInvoiceRequest.setSeries(INVOICE_B_SERIES);
        secondInvoiceRequestJSon = gson.toJson(secondInvoiceRequest);
        mockMvc.perform(
                post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(secondInvoiceRequestJSon)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
