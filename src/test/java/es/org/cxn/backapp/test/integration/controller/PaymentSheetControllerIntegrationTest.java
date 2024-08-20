
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.test.utils.CompanyControllerFactory;
import es.org.cxn.backapp.test.utils.InvoicesControllerFactory;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;
import es.org.cxn.backapp.test.utils.PaymentSheetControllerFactory;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

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

/**
 * @author Santiago Paz. Payment Sheet controller integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class PaymentSheetControllerIntegrationTest {

  /**
   * The URL endpoint for interacting with payment sheets.
   * This endpoint is used for creating, retrieving, and managing
   * payment sheets.
   */
  private static final String PAYMENT_SHEET_URL = "/api/paymentSheet";

  /**
   * Payment sheet identifier that not exists.
   */
  private static final int PAYMENT_SHEET_NOT_EXISTING_ID = 88;

  /**
   * The URL endpoint for user creation.
   * This endpoint is used for signing up new users via the
   * authentication service.
   */
  private static final String CREATE_USER_URL = "/api/auth/signup";

  /**
   * The category of regular transport, specifically set to "taxi".
   * This value is used to categorize regular transport entries within
   * the system.
   */
  private static final String REGULAR_TRANSPORT_CATEGORY = "taxi";

  /**
   * The default description for regular transport.
   * This value is used as a placeholder or default description for regular
   * transport entries.
   */
  private static final String REGULAR_TRANSPORT_DESCRIPTION = "Description";

  /**
   * The URL endpoint for interacting with invoices.
   * This endpoint is used for creating, retrieving, and managing invoices.
   */
  private static final String INVOICE_URL = "/api/invoice";

  /**
   * The URL endpoint for interacting with company information.
   * This endpoint is used for creating, retrieving, and managing company data.
   */
  private static final String COMPANY_URL = "/api/company";

  /**
   * The URL endpoint for interacting adding self vehicles.
   */
  private static final String ADD_SELF_VEHICLE_ENDPOINT = "/addSelfVehicle";

  /**
   * The identifier for a payment sheet that does not exist.
   * This value is used to test scenarios where a payment sheet
   * with the given identifier is not found.
   */
  private static final int NON_EXISTING_PAYMENT_SHEET_ID = 99;

  /**
   * MockMvc instance used for performing HTTP requests and testing
   * the web layer.
   * This bean is automatically injected by the Spring framework.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * The places associated with the self vehicle for testing.
   */
  private static final String SELF_VEHICLE_PLACES = "place1 place2";

  /**
   * The distance of the self vehicle in kilometers used in testing.
   */
  private static final float SELF_VEHICLE_DISTANCE = 145.24f;

  /**
   * The price per kilometer of the self vehicle used in testing.
   */
  private static final double SELF_VEHICLE_KM_PRICE = 0.19;

  /**
   * The amount of days of self vehicle travel duration.
   */
  private static final int SELF_VEHICLE_AMOUNT_DAYS = 5;

  /**
   * The price of food and housing per day.
   */
  private static final float FOOD_HOUSING_DAY_PRICE = 22.30f;

  /**
   * If food housing have housing, this is true else false.
   */
  private static final boolean FOOD_HOUSING_OVERNIGHT = Boolean.TRUE;

  /**
   * Food housing duration in days.
   */
  private static final int FOOD_HOUSING_AMOUNT_DAYS = 5;

  /**
   * Gson instance used for JSON serialization and deserialization.
   * This static field provides a single instance of Gson for the entire class,
   *  ensuring consistency and avoiding repeated instantiation.
   */
  private static Gson gson;

  @BeforeAll
  public static void setup() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

  /**
   * Main class constructor.
   */
  PaymentSheetControllerIntegrationTest() {
    super();
  }

  @BeforeEach
  public void beforeTestCase() throws Exception {
    // Create user, expect 201 created.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(UsersControllerFactory.getUserARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  private void assertResponseContains(
        final String responseContent, final String... expectedValues
  ) {
    for (String expectedValue : expectedValues) {
      Assertions.assertTrue(
            responseContent.contains(expectedValue),
            "Response should contain: " + expectedValue
      );
    }
  }

  /**
   * Create payment sheet request, check data returned and request
   *  all payment sheets.
   * Check that are only one payment sheet and containing data.
   *
   * @throws Exception
   */
  @Test
  @Transactional
  void testCreatePaymentSheetResponseContainData() throws Exception {
    var numberOfPaymentSheets = 1;

    // Create payment sheet, expect 201 created
    var requestResult =
          mockMvc
                .perform(
                      post(PAYMENT_SHEET_URL)
                            .contentType(MediaType.APPLICATION_JSON).content(
                                  PaymentSheetControllerFactory
                                        .getPaymentSheetRequestFormJson()
                            )
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

    var responseContent = requestResult.getResponse().getContentAsString();

    // Check response data.
    assertResponseContains(
          responseContent, UsersControllerFactory.USER_A_NAME,
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          UsersControllerFactory.USER_A_SECOND_SURNAME,
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE.toString(),
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE.toString()
    );

    var paymentSheetGetResponse = mockMvc.perform(get(PAYMENT_SHEET_URL))
          .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetListResponse = gson
          .fromJson(paymentSheetGetResponse, PaymentSheetListResponse.class);

    var paymentSheetList = paymentSheetListResponse.paymentSheetsList();
    Assertions.assertEquals(
          paymentSheetList.size(), numberOfPaymentSheets,
          "Only one payment sheet."
    );

    var paymentSheetReturned = paymentSheetList.iterator().next();

    Assertions.assertEquals(
          UsersControllerFactory.USER_A_DNI, paymentSheetReturned.userDNI(),
          "user dni"
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          paymentSheetReturned.userFirstSurname(), "user first surname"
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE,
          paymentSheetReturned.startDate(), "payment sheet start date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE,
          paymentSheetReturned.endDate(), "payment sheet end date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          paymentSheetReturned.place(), "payment sheet place."
    );

    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          paymentSheetReturned.reason(), "payment sheet reason."
    );

  }

  /**
   * Create payment sheet with no valid user who no exists. Expect bad request.
   *
   * @throws Exception
   */
  @Test
  @Transactional
  void testCreatePaymentSheetNotExistingUserBadRequest() throws Exception {
    var paymentSheetRequestForm = new CreatePaymentSheetRequestForm(
          "NotExisting@Email.es",
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE,
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE
    );
    var createPaymentSheetRequestFormJson =
          gson.toJson(paymentSheetRequestForm);
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
  void testPaymentSheetCreateAndGetDataFromIdOk() throws Exception {

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.paymentSheetIdentifier();

    // get payment sheet data using id.
    var paymentSheetIdResponse = mockMvc
          .perform(
                get(
                      PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier

                )
          ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetIdObjectResponse =
          gson.fromJson(paymentSheetIdResponse, PaymentSheetResponse.class);

    // Check payment sheet response data obtained through identifier.
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_DNI,
          paymentSheetIdObjectResponse.userDNI(), "user dni"
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          paymentSheetIdObjectResponse.userFirstSurname(), "user first surname"
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE,
          paymentSheetIdObjectResponse.startDate(), "payment sheet start date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE,
          paymentSheetIdObjectResponse.endDate(), "payment sheet end date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          paymentSheetIdObjectResponse.place(), "payment sheet place."
    );

    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          paymentSheetIdObjectResponse.reason(), "payment sheet reason."
    );

  }

  /**
   * Get payment sheet with identifier that not exists is bad request.
   *
   * @throws Exception
   */
  @Test
  @Transactional
  void testGetDataFromIdNotExistingPaymentSheetBadRequest() throws Exception {
    mockMvc.perform(
          get(
                PAYMENT_SHEET_URL + "/" + NON_EXISTING_PAYMENT_SHEET_ID

          )
    ).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @Transactional
  void testPaymentSheetAddRegularTransportOk() throws Exception {

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

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

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.paymentSheetIdentifier();

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

    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);
    // Add regular transport not existing payment sheet identifier.
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + NON_EXISTING_PAYMENT_SHEET_ID
                      + "/addRegularTransport"
          ).contentType(MediaType.APPLICATION_JSON)
                .content(addRegularTransportRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testPaymentSheetAddSelfVehicleOk() throws Exception {

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // get payment sheet identifier from response

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.paymentSheetIdentifier();

    // add self vehicle
    // String places,  float distance,  double kmPrice
    var svrequest = new AddSelfVehicleRequestForm(
          "place1 place2", SELF_VEHICLE_DISTANCE, SELF_VEHICLE_KM_PRICE
    );
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addSelfVehicle"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

  }

  /**
   * Tests the scenario where an attempt is made to add a self vehicle
   * to a payment sheet with an identifier that does not exist. This
   * should result in a "Bad Request" status being returned.
   *
   * @throws Exception if an error occurs during the test execution
   */
  @Test
  @Transactional
  void testNotExistingPaymentSheetAddSelfVehicleBadRequest() throws Exception {
    // Prepare the request to add a self vehicle
    var svrequest = new AddSelfVehicleRequestForm(
          SELF_VEHICLE_PLACES, SELF_VEHICLE_DISTANCE, SELF_VEHICLE_KM_PRICE
    );
    var svrequestJson = gson.toJson(svrequest);

    // Perform the request and expect a Bad Request status
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + NON_EXISTING_PAYMENT_SHEET_ID
                      + ADD_SELF_VEHICLE_ENDPOINT
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testPaymentSheetAddFoodHousingOk() throws Exception {

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    // get payment sheet identifier from response

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.paymentSheetIdentifier();

    // add food housing.
    // Integer amountDays,  float dayPrice, Boolean overnight
    var svrequest = new AddFoodHousingToPaymentSheetRequestForm(
          SELF_VEHICLE_AMOUNT_DAYS, FOOD_HOUSING_DAY_PRICE,
          FOOD_HOUSING_OVERNIGHT
    );
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
  void testNotExistingPaymentSheetAddFoodHousingBadRequest() throws Exception {
    final var paymentSheetIdentifier = 8;
    // add food housing.
    // Integer amountDays,  float dayPrice, Boolean overnight
    var svrequest = new AddFoodHousingToPaymentSheetRequestForm(
          SELF_VEHICLE_AMOUNT_DAYS, FOOD_HOUSING_DAY_PRICE,
          FOOD_HOUSING_OVERNIGHT
    );
    var svrequestJson = gson.toJson(svrequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addFoodHousing"
          ).contentType(MediaType.APPLICATION_JSON).content(svrequestJson)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testDeletePaymentSheetAndAssociatedEntities() throws Exception {

    // Create payment sheet, expect 201 created
    var paymentSheetCreateResponse =
          mockMvc
                .perform(
                      post(PAYMENT_SHEET_URL)
                            .contentType(MediaType.APPLICATION_JSON).content(
                                  PaymentSheetControllerFactory
                                        .getPaymentSheetRequestFormJson()
                            )
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

    var paymentSheetResponse =
          paymentSheetCreateResponse.getResponse().getContentAsString();
    var paymentSheetId =
          gson.fromJson(paymentSheetResponse, PaymentSheetResponse.class)
                .paymentSheetIdentifier();

    // Delete payment sheet
    mockMvc.perform(delete(PAYMENT_SHEET_URL + "/" + paymentSheetId))
          .andExpect(MockMvcResultMatchers.status().isNoContent());

    // Verify payment sheet is deleted
    var paymentSheetGetResponse = mockMvc
          .perform(get(PAYMENT_SHEET_URL + "/" + paymentSheetId))
          .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

    var errorResponse =
          paymentSheetGetResponse.getResponse().getContentAsString();
    Assertions.assertTrue(
          errorResponse.contains("not found"), "Payment sheet should be deleted"
    );

  }

  @Test
  @Transactional
  void testCreateRegularTransportsWithExistingKeyBadRequest() throws Exception {

    // Create payment sheet
    var paymentSheetCreateResponse = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

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

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    //Prepare regular transport
    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);

    var paymentSheetResponseObject =
          gson.fromJson(paymentSheetCreateResponse, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponseObject.paymentSheetIdentifier();

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
  void testRemoveSelfVehicleFromPaymentSheetCheckPaymentsheet()
        throws Exception {
    // Create payment sheet
    var paymentSheetResponseJson = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);

    // Prepare self vehicle request.
    final var selfVehicleDistance = 100;
    final var selfVehiclePlaces = "Naron - Ferrol - Pontevedra : Ida y vuelta.";
    final var selfVehicleForm = new AddSelfVehicleRequestForm(
          selfVehiclePlaces, selfVehicleDistance, SELF_VEHICLE_KM_PRICE
    );

    var selfVehicleFormJson = gson.toJson(selfVehicleForm);
    // Add self vehicle to payment Sheet
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.paymentSheetIdentifier()
                      + "/addSelfVehicle"
          ).contentType(MediaType.APPLICATION_JSON).content(selfVehicleFormJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    //Check payment sheet. It have self vehicle added.

    var paymentSheetWithSelfVehicleResponseJson =
          mockMvc
                .perform(
                      get(
                            PAYMENT_SHEET_URL + "/"
                                  + paymentSheetResponse
                                        .paymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithSelfVehicleResponse = gson.fromJson(
          paymentSheetWithSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    //Checking payment sheet with self vehicle added.
    Assertions.assertEquals(
          selfVehicleDistance,
          paymentSheetWithSelfVehicleResponse.selfVehicle().distance(),
          "self vehicle have same distance as used when created."
    );
    Assertions.assertEquals(
          SELF_VEHICLE_KM_PRICE,
          paymentSheetWithSelfVehicleResponse.selfVehicle().kmPrice(),
          "self vehicle have same km price as used when created."
    );
    Assertions.assertEquals(
          selfVehiclePlaces,
          paymentSheetWithSelfVehicleResponse.selfVehicle().places(),
          "self vehicle have same place as used when created."
    );

    // Remove self vehicle from payment sheet.

    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.paymentSheetIdentifier()
                      + "/removeSelfVehicle"
          )
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Get payment sheet and check that not have selfVehicle.

    var paymentSheetWithOutSelfVehicleResponseJson =
          mockMvc
                .perform(
                      get(
                            PAYMENT_SHEET_URL + "/"
                                  + paymentSheetResponse
                                        .paymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithOutSelfVehicleResponse = gson.fromJson(
          paymentSheetWithOutSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    Assertions.assertNull(
          paymentSheetWithOutSelfVehicleResponse.selfVehicle(),
          "Self vehicle has been removed from payment sheet."
    );

  }

  @Test
  @Transactional
  void testRemoveSelfVehicleFromNotExistentPaymentSheet() throws Exception {
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + PAYMENT_SHEET_NOT_EXISTING_ID
                      + "/removeSelfVehicle"
          )
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testRemoveNotExistentPaymentSheetBadRequest() throws Exception {
    mockMvc.perform(
          delete(PAYMENT_SHEET_URL + "/" + PAYMENT_SHEET_NOT_EXISTING_ID)
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testRemoveFoodHousingFromNotExistentPaymentSheetBadRequest()
        throws Exception {
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + PAYMENT_SHEET_NOT_EXISTING_ID
                      + "/removeFoodHousing"
          )
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testRemoveFoodHousingFromPaymentSheetCheckPaymentsheet()
        throws Exception {
    // Create payment sheet
    var paymentSheetResponseJson = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);

    // Prepare self food housing request.

    var foodHousingForm = new AddFoodHousingToPaymentSheetRequestForm(
          FOOD_HOUSING_AMOUNT_DAYS, FOOD_HOUSING_DAY_PRICE,
          FOOD_HOUSING_OVERNIGHT
    );

    var foodHousingFormJson = gson.toJson(foodHousingForm);
    // Add self vehicle to payment Sheet
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.paymentSheetIdentifier()
                      + "/addFoodHousing"
          ).contentType(MediaType.APPLICATION_JSON).content(foodHousingFormJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    //Check payment sheet. It have food housing added.

    var paymentSheetWithFoodHousingResponseJson =
          mockMvc
                .perform(
                      get(
                            PAYMENT_SHEET_URL + "/"
                                  + paymentSheetResponse
                                        .paymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithFoodHousingResponse = gson.fromJson(
          paymentSheetWithFoodHousingResponseJson, PaymentSheetResponse.class
    );

    //Checking payment sheet with self vehicle added.
    Assertions.assertEquals(
          FOOD_HOUSING_AMOUNT_DAYS,
          paymentSheetWithFoodHousingResponse.foodHousing().amountDays(),
          "food housing have same amount of days as used when created."
    );
    Assertions.assertEquals(
          FOOD_HOUSING_DAY_PRICE,
          paymentSheetWithFoodHousingResponse.foodHousing().dayPrice(),
          "food housing have same price per day as used when created."
    );
    Assertions.assertEquals(
          FOOD_HOUSING_OVERNIGHT,
          paymentSheetWithFoodHousingResponse.foodHousing().overnight(),
          "food housing have same overnight value as used when created."
    );

    // Remove self vehicle from payment sheet.

    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.paymentSheetIdentifier()
                      + "/removeFoodHousing"
          )
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Get payment sheet and check that not have food housing.

    var paymentSheetWithOutSelfVehicleResponseJson =
          mockMvc
                .perform(
                      get(
                            PAYMENT_SHEET_URL + "/"
                                  + paymentSheetResponse
                                        .paymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithOutSelfVehicleResponse = gson.fromJson(
          paymentSheetWithOutSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    Assertions.assertNull(
          paymentSheetWithOutSelfVehicleResponse.foodHousing(),
          "Food housing has been removed from payment sheet."
    );

  }

  @Test
  @Transactional
  void testRemoveRegularTransportFromNotExistentPaymentSheet()
        throws Exception {
    final var notExistentRegularTransportId = 13;
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"

                      + PAYMENT_SHEET_NOT_EXISTING_ID + "/"
                      + notExistentRegularTransportId
          ).contentType(MediaType.APPLICATION_JSON)

    ).andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testAddTwoRegularTransportToPaymentSheetRemoveFirst() throws Exception {
    // Create 2 companies for the invoice buyer and seller
    // Create first company.
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(CompanyControllerFactory.getCompanyARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create Second company.
    mockMvc.perform(
          post(COMPANY_URL).contentType(MediaType.APPLICATION_JSON)
                .content(CompanyControllerFactory.getCompanyBRequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice with same buyer and seller
    var invoiceRequestForm = new CreateInvoiceRequestForm(
          InvoicesControllerFactory.INVOICE_B_NUMBER,
          InvoicesControllerFactory.INVOICE_B_SERIES,
          InvoicesControllerFactory.INVOICE_B_PAYMENT_DATE,
          InvoicesControllerFactory.INVOICE_B_EXPEDITION_DATE,
          InvoicesControllerFactory.INVOICE_B_TAX_EXEMPT,
          InvoicesControllerFactory.INVOICE_A_SELLER,
          InvoicesControllerFactory.INVOICE_A_BUYER
    );
    var invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(invoiceRequestFormJSon)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create payment sheet
    var paymentSheetResponseJson = mockMvc
          .perform(
                post(PAYMENT_SHEET_URL).contentType(MediaType.APPLICATION_JSON)
                      .content(
                            PaymentSheetControllerFactory
                                  .getPaymentSheetRequestFormJson()
                      )
          ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
          .getResponse().getContentAsString();

    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);
    var paymentSheetIdentifier = paymentSheetResponse.paymentSheetIdentifier();
    // Add first regular transport

    var addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES
    );
    var addRegularTransportRequestJson =
          gson.toJson(addRegularTransportRequest);

    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addRegularTransport"
          ).contentType(MediaType.APPLICATION_JSON)
                .content(addRegularTransportRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());
    // Second regular transport need second invoice.
    addRegularTransportRequest = new AddRegularTransportRequestForm(
          REGULAR_TRANSPORT_CATEGORY, REGULAR_TRANSPORT_DESCRIPTION,
          InvoicesControllerFactory.INVOICE_B_NUMBER,
          InvoicesControllerFactory.INVOICE_B_SERIES
    );
    addRegularTransportRequestJson = gson.toJson(addRegularTransportRequest);
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier
                      + "/addRegularTransport"
          ).contentType(MediaType.APPLICATION_JSON)
                .content(addRegularTransportRequestJson)
    ).andExpect(MockMvcResultMatchers.status().isOk());

    // Get payment sheet and check 2 invoices.
    // Verify payment sheets
    var paymentSheetWithRegularTransportResponseJson =
          mockMvc.perform(get(PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithRegularTransportResponse = gson.fromJson(
          paymentSheetWithRegularTransportResponseJson,
          PaymentSheetResponse.class
    );

    var regularTransportListSize = paymentSheetWithRegularTransportResponse
          .regularTransportList().regularTransportList().size();

    Assertions.assertEquals(
          2, regularTransportListSize, "payment sheet have 2 regular transports"
    );

    // Remove first regular transport. Check payment sheet and related invoice.
    var regularTransportToRemoveIdentifier =
          paymentSheetWithRegularTransportResponse.regularTransportList()
                .regularTransportList().get(0).identifier();
    var paymentSheetToRemoveIdentifier =
          paymentSheetWithRegularTransportResponse.paymentSheetIdentifier();
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + paymentSheetToRemoveIdentifier + "/"
                      + regularTransportToRemoveIdentifier
          )
    ).andExpect(MockMvcResultMatchers.status().isOk());

    //Check payment sheet regular transports

    paymentSheetWithRegularTransportResponseJson =
          mockMvc.perform(get(PAYMENT_SHEET_URL + "/" + paymentSheetIdentifier))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    paymentSheetWithRegularTransportResponse = gson.fromJson(
          paymentSheetWithRegularTransportResponseJson,
          PaymentSheetResponse.class
    );

    regularTransportListSize = paymentSheetWithRegularTransportResponse
          .regularTransportList().regularTransportList().size();

    Assertions.assertEquals(
          1, regularTransportListSize, "payment sheet have 1 regular transports"
    );
    // Check that regular transport
    final var regularTransportsList = paymentSheetWithRegularTransportResponse
          .regularTransportList().regularTransportList();
    final var regularTransport = regularTransportsList.get(0);

    // Check regular transport data
    Assertions.assertEquals(
          REGULAR_TRANSPORT_CATEGORY, regularTransport.category(),
          "Los valores deberían ser iguales"
    );
    Assertions.assertEquals(
          REGULAR_TRANSPORT_DESCRIPTION, regularTransport.description(),
          "Los valores deberían ser iguales"
    );
  }
}
