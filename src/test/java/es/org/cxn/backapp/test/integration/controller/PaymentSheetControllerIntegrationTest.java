
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.AddFoodHousingToPaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;
import es.org.cxn.backapp.model.form.requests.AddSelfVehicleRequestForm;
import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;
import es.org.cxn.backapp.model.form.responses.PaymentSheetListResponse;
import es.org.cxn.backapp.model.form.responses.PaymentSheetResponse;
import es.org.cxn.backapp.service.JwtUtils;
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
import org.springframework.security.core.userdetails.UserDetailsService;
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

  private final static String PAYMENT_SHEET_URL = "/api/paymentSheet";
  private final static String CREATE_USER_URL = "/api/auth/signup";

  private final static String REGULAR_TRANSPORT_CATEGORY = "taxi";
  private final static String REGULAR_TRANSPORT_DESCRIPTION = "Description";

  private final static String INVOICE_URL = "/api/invoice";
  private final static String COMPANY_URL = "/api/company";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  UserDetailsService myUserDetailsService;
  @Autowired
  JwtUtils jwtUtils;

  private static Gson gson;

  @BeforeAll
  public static void setup() {
    gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .create();
  }

  /**
   * Main class constructor
   */
  public PaymentSheetControllerIntegrationTest() {
    super();
  }

  @BeforeEach
  public void BeforeTestCase() throws Exception {
    // Create user, expect 201 created.
    mockMvc.perform(
          post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(UsersControllerFactory.getUserARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());
  }

  private void assertResponseContains(
        String responseContent, String... expectedValues
  ) {
    for (String expectedValue : expectedValues) {
      Assertions.assertTrue(
            responseContent.contains(expectedValue),
            "Response should contain: " + expectedValue
      );
    }
  }

  /**
   * Create payment sheet request, check data returned and request all payment sheets.
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

    var paymentSheetList = paymentSheetListResponse.getPaymentSheetsList();
    Assertions.assertEquals(
          paymentSheetList.size(), numberOfPaymentSheets,
          "Only one payment sheet."
    );

    var paymentSheetReturned = paymentSheetList.iterator().next();

    Assertions.assertEquals(
          UsersControllerFactory.USER_A_DNI, paymentSheetReturned.getUserDNI(),
          "user dni"
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          paymentSheetReturned.getUserFirstSurname(), "user first surname"
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE,
          paymentSheetReturned.getStartDate(), "payment sheet start date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE,
          paymentSheetReturned.getEndDate(), "payment sheet end date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          paymentSheetReturned.getPlace(), "payment sheet place."
    );

    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          paymentSheetReturned.getReason(), "payment sheet reason."
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
          paymentSheetResponseObject.getPaymentSheetIdentifier();

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
          paymentSheetIdObjectResponse.getUserDNI(), "user dni"
    );
    Assertions.assertEquals(
          UsersControllerFactory.USER_A_FIRST_SURNAME,
          paymentSheetIdObjectResponse.getUserFirstSurname(),
          "user first surname"
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_START_DATE,
          paymentSheetIdObjectResponse.getStartDate(),
          "payment sheet start date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_END_DATE,
          paymentSheetIdObjectResponse.getEndDate(), "payment sheet end date."
    );
    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_PLACE,
          paymentSheetIdObjectResponse.getPlace(), "payment sheet place."
    );

    Assertions.assertEquals(
          PaymentSheetControllerFactory.PAYMENT_SHEET_REASON,
          paymentSheetIdObjectResponse.getReason(), "payment sheet reason."
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
    var paymentSheetNotExistingIdentifier = 99;
    mockMvc
          .perform(
                get(
                      PAYMENT_SHEET_URL + "/"
                            + paymentSheetNotExistingIdentifier

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
          InvoicesControllerFactory.INVOICE_A_NUMBER,
          InvoicesControllerFactory.INVOICE_A_SERIES
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
  void testNotExistingPaymentSheetAddFoodHousingBadRequest() throws Exception {
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
                .getPaymentSheetIdentifier();

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

    // You would need to implement findByPaymentSheetId method in respective repositories
    //    assertTrue(paymentSheetService.findRegularTransportByPaymentSheetId(paymentSheetId).isEmpty(), "RegularTransport should be deleted");
    //  assertTrue(paymentSheetService.findSelfVehicleByPaymentSheetId(paymentSheetId).isEmpty(), "SelfVehicle should be deleted");
    // assertTrue(paymentSheetService.findFoodHousingByPaymentSheetId(paymentSheetId).isEmpty(), "FoodHousing should be deleted");
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
    ;

    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);

    // Prepare self vehicle request.
    var selfVehicleForm = new AddSelfVehicleRequestForm();
    var selfVehicleDistance = 100;
    var selfVehiclePrice = 0.19;
    var selfVehiclePlaces = "Naron - Ferrol - Pontevedra : Ida y vuelta.";
    selfVehicleForm.setDistance(selfVehicleDistance);
    selfVehicleForm.setKmPrice(selfVehiclePrice);
    selfVehicleForm.setPlaces(selfVehiclePlaces);

    var selfVehicleFormJson = gson.toJson(selfVehicleForm);
    // Add self vehicle to payment Sheet
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.getPaymentSheetIdentifier()
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
                                        .getPaymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithSelfVehicleResponse = gson.fromJson(
          paymentSheetWithSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    //Checking payment sheet with self vehicle added.
    Assertions.assertEquals(
          selfVehicleDistance,
          paymentSheetWithSelfVehicleResponse.getSelfVehicle().getDistance(),
          "self vehicle have same distance as used when created."
    );
    Assertions.assertEquals(
          selfVehiclePrice,
          paymentSheetWithSelfVehicleResponse.getSelfVehicle().getKmPrice(),
          "self vehicle have same km price as used when created."
    );
    Assertions.assertEquals(
          selfVehiclePlaces,
          paymentSheetWithSelfVehicleResponse.getSelfVehicle().getPlaces(),
          "self vehicle have same place as used when created."
    );

    // Remove self vehicle from payment sheet.

    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.getPaymentSheetIdentifier()
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
                                        .getPaymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithOutSelfVehicleResponse = gson.fromJson(
          paymentSheetWithOutSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    Assertions.assertNull(
          paymentSheetWithOutSelfVehicleResponse.getSelfVehicle(),
          "Self vehicle has been removed from payment sheet."
    );

  }

  @Test
  @Transactional
  void testRemoveSelfVehicleFromNotExistentPaymentSheet() throws Exception {
    var notExistingPaymentSheetId = 88;
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + notExistingPaymentSheetId
                      + "/removeSelfVehicle"
          )
    ).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @Transactional
  void testRemoveNotExistentPaymentSheetBadRequest() throws Exception {
    var notExistingPaymentSheetId = 88;
    mockMvc.perform(delete(PAYMENT_SHEET_URL + "/" + notExistingPaymentSheetId))
          .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  @Transactional
  void testRemoveFoodHousingFromNotExistentPaymentSheetBadRequest()
        throws Exception {
    var notExistingPaymentSheetId = 88;
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/" + notExistingPaymentSheetId
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
    ;

    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);

    // Prepare self food housing request.
    var foodHousingForm = new AddFoodHousingToPaymentSheetRequestForm();
    var foodHousingAmountDays = 20;
    var foodHousingDayPrice = 12;
    var foodHousingOvernight = Boolean.TRUE;
    foodHousingForm.setAmountDays(foodHousingAmountDays);
    foodHousingForm.setDayPrice(foodHousingDayPrice);
    foodHousingForm.setOvernight(foodHousingOvernight);

    var foodHousingFormJson = gson.toJson(foodHousingForm);
    // Add self vehicle to payment Sheet
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.getPaymentSheetIdentifier()
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
                                        .getPaymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithFoodHousingResponse = gson.fromJson(
          paymentSheetWithFoodHousingResponseJson, PaymentSheetResponse.class
    );

    //Checking payment sheet with self vehicle added.
    Assertions.assertEquals(
          foodHousingAmountDays,
          paymentSheetWithFoodHousingResponse.getFoodHousing().getAmountDays(),
          "food housing have same amount of days as used when created."
    );
    Assertions.assertEquals(
          foodHousingDayPrice,
          paymentSheetWithFoodHousingResponse.getFoodHousing().getDayPrice(),
          "food housing have same price per day as used when created."
    );
    Assertions.assertEquals(
          foodHousingOvernight,
          paymentSheetWithFoodHousingResponse.getFoodHousing().isOvernight(),
          "food housing have same overnight value as used when created."
    );

    // Remove self vehicle from payment sheet.

    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"
                      + paymentSheetResponse.getPaymentSheetIdentifier()
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
                                        .getPaymentSheetIdentifier()
                      ).contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    var paymentSheetWithOutSelfVehicleResponse = gson.fromJson(
          paymentSheetWithOutSelfVehicleResponseJson, PaymentSheetResponse.class
    );

    Assertions.assertNull(
          paymentSheetWithOutSelfVehicleResponse.getFoodHousing(),
          "Food housing has been removed from payment sheet."
    );

  }

  @Test
  @Transactional
  void testRemoveRegularTransportFromNotExistentPaymentSheet()
        throws Exception {
    final var notExistentPaymentSheetId = 88;
    final var notExistentRegularTransportId = 13;
    mockMvc.perform(
          post(
                PAYMENT_SHEET_URL + "/"

                      + notExistentPaymentSheetId + "/"
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

    // Create invoices for first and second regular transport.
    var invoiceRequestFormJSon =
          InvoicesControllerFactory.getInvoiceARequestJson();

    // Create invoice
    mockMvc.perform(
          post(INVOICE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(InvoicesControllerFactory.getInvoiceARequestJson())
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    // Create second invoice with same buyer and seller
    var invoiceRequestForm = InvoicesControllerFactory.createInvoiceARequest();
    invoiceRequestForm.setNumber(InvoicesControllerFactory.INVOICE_B_NUMBER);
    invoiceRequestForm.setSeries(InvoicesControllerFactory.INVOICE_B_SERIES);
    invoiceRequestFormJSon = gson.toJson(invoiceRequestForm);
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
    ;
    var paymentSheetResponse =
          gson.fromJson(paymentSheetResponseJson, PaymentSheetResponse.class);
    var paymentSheetIdentifier =
          paymentSheetResponse.getPaymentSheetIdentifier();
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
          .getRegularTransportList().getRegularTransportList().size();

    Assertions.assertEquals(
          2, regularTransportListSize, "payment sheet have 2 regular transports"
    );

    // Remove first regular transport. Check payment sheet and related invoice.
    var regularTransportToRemoveIdentifier =
          paymentSheetWithRegularTransportResponse.getRegularTransportList()
                .getRegularTransportList().get(0).getIdentifier();
    var paymentSheetToRemoveIdentifier =
          paymentSheetWithRegularTransportResponse.getPaymentSheetIdentifier();
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
          .getRegularTransportList().getRegularTransportList().size();

    Assertions.assertEquals(
          1, regularTransportListSize, "payment sheet have 1 regular transports"
    );
    // Check that regular transport
    final var regularTransportsList = paymentSheetWithRegularTransportResponse
          .getRegularTransportList().getRegularTransportList();
    final var regularTransport = regularTransportsList.get(0);

    // Check regular transport data
    Assertions.assertEquals(
          REGULAR_TRANSPORT_CATEGORY, regularTransport.getCategory(),
          "Los valores deberían ser iguales"
    );
    Assertions.assertEquals(
          REGULAR_TRANSPORT_DESCRIPTION, regularTransport.getDescription(),
          "Los valores deberían ser iguales"
    );
    /* Assertions.assertEquals(
          InvoicesControllerFactory.INVOICE_B_NUMBER,
          regularTransport.getInvoiceResponse().getNumber(),
          "El numero de factura asignado es el B, de la segunda"
    );
    COMPROBAR ESTO
    *
    */
    //    Assertions.assertEquals(
    //          InvoicesControllerFactory.INVOICE_B_SERIES,
    //          regularTransport.getInvoiceResponse().getSeries(),
    //          "La serie de la factura asignada al transporte regular es el de la segunda."
    //    );
  }
}
