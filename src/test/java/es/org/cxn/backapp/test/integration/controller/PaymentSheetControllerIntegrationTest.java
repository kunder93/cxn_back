
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.service.JwtUtils;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.Month;

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
 * @author Santiago Paz. Payment Sheet controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class PaymentSheetControllerIntegrationTest {

  private final static String PAYMENT_SHEET_URL = "/api/paymentSheet";
  private final static String CREATE_USER_URL = "/api/auth/signup";

  private final static String USER_DNI = "32721859N";
  private final static String USER_EMAIL = "user@email.es";
  private final static String USER_NAME = "userName";
  private final static String USER_FIRST_SURNAME = "userFirstSurname";
  private final static String USER_SECOND_SURNAME = "userSecondSurname";
  private final static LocalDate USER_BIRTH_DATE =
        LocalDate.of(1993, Month.MAY, 8);
  private final static String PAYMENT_SHEET_REASON = "MyPaymentSheet reason";
  private final static String PAYMENT_SHEET_PLACE = "Padron, Espain";
  private final static LocalDate PAYMENT_SHEET_START_DATE =
        LocalDate.of(2014, Month.JANUARY, 1);
  private final static LocalDate PAYMENT_SHEET_END_DATE =
        LocalDate.of(2014, Month.JANUARY, 3);

  private final static String REGULAR_TRANSPORT_CATEGORY = "taxi";
  private final static String REGULAR_TRANSPORT_DESCRIPTION = "Description";

  private final static String INVOICE_URL = "/api/invoice";
  private final static String COMPANY_URL = "/api/company";

  private final static String COMPANY_A_NIFCIF = "45235234-G";
  private final static String COMPANY_A_NAME = "MyCompanyName";
  private final static String COMPANY_A_ADDRESS = "MyCompanyAddress";

  private final static String COMPANY_B_NIFCIF = "33344434-G";
  private final static String COMPANY_B_NAME = "OtherCompanyName";
  private final static String COMPANY_B_ADDRESS = "OtherCompanyAddress";
  private static String createCompanyARequestJson;
  private static String createCompanyBRequestJson;

  private final static int INVOICE_A_NUMBER = 0001;
  private final static String INVOICE_A_SERIES = "IAS";
  private final static LocalDate INVOICE_A_EXPEDITION_DATE =
        LocalDate.of(2020, 1, 8);
  private final static Boolean INVOICE_A_TAX_EXEMPT = Boolean.TRUE;
  private final static String INVOICE_A_BUYER = COMPANY_B_NIFCIF;
  private final static String INVOICE_A_SELLER = COMPANY_A_NIFCIF;
  private final static LocalDate INVOICE_A_PAYMENT_DATE =
        LocalDate.of(2020, 1, 24);

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  private static SignUpRequestForm createUserRequestForm =
        new SignUpRequestForm(
              USER_DNI, USER_NAME, USER_FIRST_SURNAME, USER_SECOND_SURNAME,
              USER_BIRTH_DATE, "male", "password123", USER_EMAIL, "postCode",
              "apartnumber", "building", "stret", "city", 724, "Lugo"
        );

  private String createUserRequestFormJson = gson.toJson(createUserRequestForm);

  private static CreatePaymentSheetRequestForm paymentSheetRequestForm =
        new CreatePaymentSheetRequestForm(
              USER_EMAIL, PAYMENT_SHEET_REASON, PAYMENT_SHEET_PLACE,
              PAYMENT_SHEET_START_DATE, PAYMENT_SHEET_END_DATE
        );
  private String createPaymentSheetRequestFormJson =
        gson.toJson(paymentSheetRequestForm);

  @BeforeAll
  public static void setup() {
    var createCompanyARequest = new CreateCompanyRequestForm(
          COMPANY_A_NIFCIF, COMPANY_A_NAME, COMPANY_A_ADDRESS
    );
    var createCompanyBRequest = new CreateCompanyRequestForm(
          COMPANY_B_NIFCIF, COMPANY_B_NAME, COMPANY_B_ADDRESS
    );
    createCompanyARequestJson = new Gson().toJson(createCompanyARequest);
    createCompanyBRequestJson = new Gson().toJson(createCompanyBRequest);
  }

  /**
   * Main class constructor
   */
  public PaymentSheetControllerIntegrationTest() {
    super();
  }

  @Test
  @Transactional
  void testCreatePaymentSheetResponseOkContainData() throws Exception {
    var numberOfPaymentSheets = 1;

    // Create user, expect 201 created.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestFormJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Do payment sheet request, expect controller status created
    var requestResult = mockMvc.perform(
          post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                .content(createPaymentSheetRequestFormJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    // Check response data.
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString().contains(USER_NAME),
          "Response contains user name."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(USER_FIRST_SURNAME),
          "Response contains user first surname."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(USER_SECOND_SURNAME),
          "Response contains user second surname."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(PAYMENT_SHEET_REASON),
          "Response contains payment sheet reason."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(PAYMENT_SHEET_PLACE),
          "Response contains payment sheet place."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(PAYMENT_SHEET_START_DATE.toString()),
          "Response contains payment sheet start date."
    );
    Assertions.assertTrue(
          requestResult.getResponse().getContentAsString()
                .contains(PAYMENT_SHEET_END_DATE.toString()),
          "Response contains payment sheet end date."
    );

    var paymentSheetGetResponse = mockMvc.perform(get(PAYMENT_SHEET_URL))
          .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var paymentSheetListResponse = gson
          .fromJson(paymentSheetGetResponse, PaymentSheetListResponse.class);
    Assertions.assertEquals(
          paymentSheetListResponse.getPaymentSheetsList().size(),
          numberOfPaymentSheets, "user dni."
    );

  }

  @Test
  @Transactional
  void testCreatePaymentSheetNotExistingUserBadRequest() throws Exception {

    // Do payment sheet request, no user created, cannot create.
    mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(createPaymentSheetRequestFormJson)
          ).andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andReturn();

  }

  @Test
  @Transactional
  void testPaymentSheetAddRegularTransportOk() throws Exception {
    // Create user.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestFormJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(createPaymentSheetRequestFormJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

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
          INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT, INVOICE_A_SELLER,
          INVOICE_A_BUYER
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

    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          INVOICE_A_NUMBER, INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);

    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.getPaymentSheetIdentifier();

    mockMvc
          .perform(
                post(
                      PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                            + "/addRegularTransport"
                ).contentType(MediaType.APPLICATION_JSON)
                      .content(addRegularTransportRequestJson)
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();
  }

  @Test
  @Transactional
  void testNotExistingPaymentSheetAddRegularTransportBadRequest()
        throws Exception {
    var paymentSheetIdentifier = 88;
    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          INVOICE_A_NUMBER, INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);
    // Add regular transport not existing payment sheet identifier.
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addRegularTransport"
          ).contentType(MediaType.APPLICATION_JSON)
                .content(addRegularTransportRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testPaymentSheetAddSelfVehicleOk() throws Exception {
    // Create user.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestFormJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(createPaymentSheetRequestFormJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // get payment sheet identifier from response
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.getPaymentSheetIdentifier();

    // add self vehicle
    // String places,  float distance,  double kmPrice
    var svrequest =
          new AddSelfVehicleRequestForm("place1 place2", (float) 145.24, 0.19);
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addSelfVehicle"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @Transactional
  void testNotExistingPaymentSheetAddSelfVehicleBadRequest() throws Exception {

    var paymentSheetIdentifier = 99;

    // add self vehicle
    // String places,  float distance,  double kmPrice
    var svrequest =
          new AddSelfVehicleRequestForm("place1 place2", (float) 145.24, 0.19);
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addSelfVehicle"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testPaymentSheetAddFoodHousingOk() throws Exception {
    // Create user.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestFormJson)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(createPaymentSheetRequestFormJson)
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // get payment sheet identifier from response
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.getPaymentSheetIdentifier();

    // add food housing.
    // Integer amountDays,  float dayPrice, Boolean overnight
    var svrequest =
          new AddFoodHousingToPaymentSheetRequestForm(5, (float) 22.20, true);
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addFoodHousing"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @Transactional
  void testNotExistingPaymentSheetAddFoodHousingOk() throws Exception {
    var paymentSheetIdentifier = 8;
    // add food housing.
    // Integer amountDays,  float dayPrice, Boolean overnight
    var svrequest =
          new AddFoodHousingToPaymentSheetRequestForm(5, (float) 22.20, true);
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addFoodHousing"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

}
