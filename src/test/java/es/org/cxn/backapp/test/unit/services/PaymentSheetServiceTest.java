
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.FoodHousingRepository;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;
import es.org.cxn.backapp.repository.PaymentSheetEntityRepository;
import es.org.cxn.backapp.repository.RegularTransportRepository;
import es.org.cxn.backapp.repository.SelfVehicleRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultPaymentSheetService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the {@link DefaultPaymentSheetService} class.
 *
 * This test class verifies the functionality of the
 * {@link DefaultPaymentSheetService} service, including methods for adding,
 * updating, finding, and removing payment sheets.
 * It uses mocks to simulate interactions with repositories and ensure
 * the service behaves as expected in various scenarios.
 *
 * @author Santi
 */
class PaymentSheetServiceTest {

  /**
   * Mock for the {@link PaymentSheetEntityRepository} used to interact
   * with payment sheet entities.
   *
   * This mock simulates the behavior of the payment sheet repository,
   * allowing tests to focus on the service logic without requiring a real
   *  database.
   */
  @Mock
  private PaymentSheetEntityRepository paymentSheetRepository;

  /**
   * Mock for the {@link UserEntityRepository} used to interact with
   * user entities.
   *
   * This mock simulates the behavior of the user repository, providing
   * predefined responses for user-related operations during testing.
   */
  @Mock
  private UserEntityRepository userRepository;

  /**
   * Mock for the {@link InvoiceEntityRepository} used to interact with
   * invoice entities.
   *
   * This mock simulates the behavior of the invoice repository, enabling tests
   *  to handle invoice-related scenarios without accessing a real database.
   */
  @Mock
  private InvoiceEntityRepository invoiceRepository;

  /**
   * Mock for the {@link RegularTransportRepository} used to interact with
   *  regular transport entities.
   *
   * This mock simulates the behavior of the regular transport repository,
   *  allowing for the testing of transport-related functionality within
   *  the payment sheet service.
   */
  @Mock
  private RegularTransportRepository regularTransportRepository;

  /**
   * Mock for the {@link SelfVehicleRepository} used to interact with
   * self-vehicle entities.
   *
   * This mock simulates the behavior of the self-vehicle repository,
   *  providing controlled responses for self-vehicle operations during
   *  the tests.
   */
  @Mock
  private SelfVehicleRepository selfVehicleRepository;

  /**
   * Mock for the {@link FoodHousingRepository} used to interact with food
   * housing entities.
   *
   * This mock simulates the behavior of the food housing repository,
   * facilitating tests that involve food housing operations within the
   * payment sheet service.
   */
  @Mock
  private FoodHousingRepository foodHousingRepository;

  /**
   * Service under test, {@link DefaultPaymentSheetService}.
   *
   * This is the actual service class being tested. It is instantiated with
   * the mocked repositories to isolate the service logic and ensure
   * tests focus on its behavior.
   */
  @InjectMocks
  private DefaultPaymentSheetService paymentSheetService;

  /**
   * The ID used to identify a specific payment sheet in tests.
   */
  private static final int PAYMENT_SHEET_ID = 1;

  /**
   * The number associated with the invoice used in tests.
   */
  private static final int INVOICE_NUMBER = 123;

  /**
   * The series identifier for the invoice used in tests.
   */
  private static final String INVOICE_SERIES = "A";

  /**
   * The category of the regular transport used in tests.
   */
  private static final String TRANSPORT_CATEGORY = "category";

  /**
   * The description of the regular transport used in tests.
   */
  private static final String TRANSPORT_DESCRIPTION = "description";

  /**
   * A sample value for the distance in self-vehicle entity.
   */
  private static final BigDecimal VEHICLE_DISTANCE = BigDecimal.valueOf(100.0);

  /**
   * A sample value for the kilometer price in self-vehicle entity.
   */
  private static final BigDecimal VEHICLE_KM_PRICE = BigDecimal.valueOf(0.11);

  /**
   * A sample reason for the payment sheet entity.
   */
  private static final String PAYMENT_SHEET_REASON = "sample reason";

  /**
   * A sample place for the payment sheet entity.
   */
  private static final String PAYMENT_SHEET_PLACE = "Feorae";

  /**
   * The place associated with the self-vehicle.
   */
  private static final String SELF_VEHICLE_PLACES = "Some Place";

  /**
   * The quantity of food housing used in the test.
   */
  private static final int FOOD_HOUSING_QUANTITY = 5;

  /**
   * The amount for food housing used in the test.
   */
  private static final BigDecimal FOOD_HOUSING_AMOUNT =
        BigDecimal.valueOf(100.0);

  /**
   * A flag indicating whether the food housing is provided or not.
   */
  private static final boolean FOOD_HOUSING_PROVIDED = true;

  /**
   * The ID used to identify a specific regular transport in tests.
   */
  private static final int REGULAR_TRANSPORT_ID = 99;

  /**
   * Sets up the test environment before each test case.
   *
   * Initializes the mocks and prepares the test environment by opening
   * mock annotations.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddPaymentSheetSuccess() throws PaymentSheetServiceException {
    var reason = "Business trip";
    var place = "Office";
    var startDate = LocalDate.now();
    var endDate = LocalDate.now().plusDays(1);
    var userEmail = "user@example.com";
    var paymentSheet = PersistentPaymentSheetEntity.builder().reason(reason)
          .place(place).endDate(endDate).startDate(startDate)
          .userOwner(new PersistentUserEntity()).build();
    when(userRepository.findByEmail(userEmail))
          .thenReturn(Optional.of(new PersistentUserEntity()));
    when(paymentSheetRepository.save(any(PersistentPaymentSheetEntity.class)))
          .thenReturn(paymentSheet);

    var result =
          paymentSheetService.add(reason, place, startDate, endDate, userEmail);
    assertNotNull(result, "result is not null.");
    assertEquals(reason, result.getReason(), "result have expected reason.");
    verify(paymentSheetRepository, times(1))
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testAddPaymentSheetUserNotFound() {
    var userEmail = "user@example.com";
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.add(
            "reason", "place", LocalDate.now(), LocalDate.now().plusDays(1),
            userEmail
      );
    }, "Exception are thrown when userEmail is not found. ");
    assertEquals(
          DefaultPaymentSheetService.USER_NOT_FOUND, thrown.getMessage(),
          "Exception message is user not found message."
    );
  }

  @Test
  void testFindByIdSuccess() throws PaymentSheetServiceException {
    // Construcción de un objeto `PersistentPaymentSheetEntity`
    //con parámetros necesarios
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity())
          // Asegúrate de proporcionar un usuario válido
          .reason("Business trip") // Razón de la hoja de pago
          .place("Office") // Lugar asociado a la hoja de pago
          .startDate(LocalDate.now()) // Fecha de inicio
          .endDate(LocalDate.now().plusDays(1)) // Fecha de fin
          .build();

    // Configuración del comportamiento del mock
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Llamada al método bajo prueba
    var result = paymentSheetService.findById(1);

    // Verificación de que el resultado no es nulo
    assertNotNull(result, "result is not null.");

    // Verificación de que el repositorio fue llamado exactamente una vez
    verify(paymentSheetRepository, times(1)).findById(1);
  }

  @Test
  void testFindByIdNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.findById(1);
    }, "Exception is thrown when paytment sheet with id not found.");
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND,
          thrown.getMessage(), "Exception message is payment sheet not found."
    );
  }

  @Test
  void testRemoveSuccess() throws PaymentSheetServiceException {
    // Crear una instancia básica de PersistentPaymentSheetEntity.
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .reason("one reason").place("Feraeo").endDate(LocalDate.now())
          .startDate(LocalDate.now()).userOwner(new PersistentUserEntity())
          .build();

    // Configurar el mock para el repositorio de entidades de pago.
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Configurar el mock para el repositorio de entidades de pago para
    // que no haga nada en delete().
    doNothing().when(paymentSheetRepository).delete(paymentSheet);

    // Ejecutar el método bajo prueba.
    paymentSheetService.remove(1);

    // Verificar que el método delete() del repositorio de entidades de
    // pago fue llamado exactamente una vez.
    verify(paymentSheetRepository, times(1)).delete(paymentSheet);
  }

  @Test
  void testRemoveNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.remove(1);
    }, "Exception thrown when payment sheet with identifier not found.");
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND,
          thrown.getMessage(), "Exception message is payment sheet not found."
    );
  }

  @Test
  void testUpdatePaymentSheetSuccess() throws PaymentSheetServiceException {
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity())
          // Asegúrate de proporcionar un usuario válido
          .reason("Business trip") // Razón de la hoja de pago
          .place("Office") // Lugar asociado a la hoja de pago
          .startDate(LocalDate.now()) // Fecha de inicio
          .endDate(LocalDate.now().plusDays(1)) // Fecha de fin
          .build();
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(paymentSheetRepository.save(any(PersistentPaymentSheetEntity.class)))
          .thenReturn(paymentSheet);

    var result = paymentSheetService.updatePaymentSheet(
          1, "new reason", "new place", LocalDate.now(),
          LocalDate.now().plusDays(1)
    );
    assertNotNull(result, "result is not null.");
    verify(paymentSheetRepository, times(1)).save(paymentSheet);
  }

  @Test
  void testUpdatePaymentSheetNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.updatePaymentSheet(
            1, "new reason", "new place", LocalDate.now(),
            LocalDate.now().plusDays(1)
      );
    }, "Exception thrown when payment sheet with identifier not found.");
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND,
          thrown.getMessage(),
          "The exception message is payment sheet not found."
    );
  }

  @Test
  void testAddRegularTransportToPaymentSheetSuccess()
        throws PaymentSheetServiceException {
    // Crear un objeto User para el campo userOwner
    var user = new PersistentUserEntity();

    // Configuración de la entidad de pago con campos necesarios
    var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(user)
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).build();

    // Crear objetos necesarios para la entidad de factura
    var seller = new PersistentCompanyEntity();
    var buyer = new PersistentCompanyEntity();

    // Inicializar la entidad de factura
    var invoice = new PersistentInvoiceEntity(
          INVOICE_NUMBER, INVOICE_SERIES, LocalDate.now(),
          LocalDate.now().plusDays(1), false, seller, buyer
    );

    // Configuración del transporte regular con campos necesarios
    var regularTransport = PersistentRegularTransportEntity.builder()
          .transportInvoice(invoice).category(TRANSPORT_CATEGORY)
          .description(TRANSPORT_DESCRIPTION).build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(PAYMENT_SHEET_ID))
          .thenReturn(Optional.of(paymentSheet));
    when(
          invoiceRepository
                .findByNumberAndSeries(INVOICE_NUMBER, INVOICE_SERIES)
    ).thenReturn(Optional.of(invoice));
    when(
          regularTransportRepository
                .save(any(PersistentRegularTransportEntity.class))
    ).thenReturn(regularTransport);
    when(invoiceRepository.save(any(PersistentInvoiceEntity.class)))
          .thenReturn(invoice);
    when(paymentSheetRepository.save(any(PersistentPaymentSheetEntity.class)))
          .thenReturn(paymentSheet);

    // Ejecutar el método bajo prueba
    var result = paymentSheetService.addRegularTransportToPaymentSheet(
          PAYMENT_SHEET_ID, TRANSPORT_CATEGORY, TRANSPORT_DESCRIPTION,
          INVOICE_NUMBER, INVOICE_SERIES
    );

    // Verificar que el resultado no sea nulo
    assertNotNull(result, "result is not null.");

    // Verificar que el repositorio de pago fue guardado
    verify(paymentSheetRepository, times(1)).save(paymentSheet);

    // Verificar que el transporte regular fue guardado
    verify(regularTransportRepository, times(1))
          .save(any(PersistentRegularTransportEntity.class));
  }

  @Test
  void testAddRegularTransportToPaymentSheetPaymentSheetNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.addRegularTransportToPaymentSheet(
            1, "category", "description", 1, "series"
      );
    }, "Exception is thown when call service with payment sheet with"
          + " identifier not existing."
    );
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND,
          thrown.getMessage(), "Exception message is payment sheet not found."
    );
  }

  /**
   * Tests the successful removal of a self-vehicle from a payment sheet.
   *
   * This test verifies that a self-vehicle associated with a payment sheet is
   * correctly removed. It sets up mock responses for the repositories involved,
   * performs the removal operation, and asserts that the vehicle is deleted
   * and the payment sheet no longer contains the self-vehicle.
   *
   * @throws PaymentSheetServiceException if an error occurs during operation
   */
  @Test
  void testRemoveSelfVehicleSuccess() throws PaymentSheetServiceException {
    // Create a self-vehicle entity with associated values.
    var selfVehicle =
          PersistentSelfVehicleEntity.builder().places(SELF_VEHICLE_PLACES)
                // Use constant for 'places'
                .distance(VEHICLE_DISTANCE) // Use constant for 'distance'
                .kmPrice(VEHICLE_KM_PRICE) // Use constant for 'kmPrice'
                .build();

    var paymentSheet =
          PersistentPaymentSheetEntity.builder().reason(PAYMENT_SHEET_REASON)
                // Use constant for 'reason'
                .place(PAYMENT_SHEET_PLACE) // Use constant for 'place'
                .startDate(LocalDate.now()).endDate(LocalDate.now())
                .userOwner(new PersistentUserEntity()).selfVehicle(selfVehicle)
                // Assign the vehicle to the payment sheet
                .build();

    // Configure the mock for the payment sheet repository.
    when(paymentSheetRepository.findById(PAYMENT_SHEET_ID))
          .thenReturn(Optional.of(paymentSheet));

    // Configure mock for the self-vehicle repository to do nothing on delete().
    doNothing().when(selfVehicleRepository)
          .delete(any(PersistentSelfVehicleEntity.class));

    // Execute the method under test.
    paymentSheetService.removeSelfVehicle(PAYMENT_SHEET_ID);

    // Capture the argument passed to the delete() method.
    var captor = ArgumentCaptor.forClass(PersistentSelfVehicleEntity.class);
    verify(selfVehicleRepository, times(1)).delete(captor.capture());

    // Verify that the captured argument is the same as expected.
    assertEquals(
          selfVehicle, captor.getValue(),
          "The removed vehicle does not match the expected one."
    );

    // Verify that the vehicle was correctly removed from the payment sheet
    // (i.e., it should now be null in the payment sheet).
    assertNull(
          paymentSheet.getSelfVehicle(),
          "Payment sheet self vehicle cannot be null."
    );
  }

  @Test
  void testRemoveSelfVehicleNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.removeSelfVehicle(1);
    }, "Exception is thrown when service remove self "
          + "vehicle from payment sheet " + "with id not found."
    );
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND,
          thrown.getMessage(), "Exception message is payment sheet not found."
    );
  }

  /**
   * Tests the scenario where an invoice is not found when adding a regular
   * transport to a payment sheet.
   *
   * This test verifies that when the invoice is not found, a
   * PaymentSheetServiceException is thrown with the appropriate message.
   */
  @Test
  void testAddRegularTransportToPaymentSheetInvoiceNotFound() {
    // Create a User object for the userOwner field
    var user = new PersistentUserEntity();

    // Set up the payment sheet entity with necessary fields
    var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(user)
          .reason(PAYMENT_SHEET_REASON) // Use constant for reason
          .place(PAYMENT_SHEET_PLACE) // Use constant for place
          .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
          .build();

    // Configure mocks
    when(paymentSheetRepository.findById(PAYMENT_SHEET_ID))
          .thenReturn(Optional.of(paymentSheet));
    when(
          invoiceRepository
                .findByNumberAndSeries(INVOICE_NUMBER, INVOICE_SERIES)
    ).thenReturn(Optional.empty()); // Simulate invoice not found

    // Execute the method under test, verify that exception is thrown
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.addRegularTransportToPaymentSheet(
                PAYMENT_SHEET_ID, TRANSPORT_CATEGORY, TRANSPORT_DESCRIPTION,
                INVOICE_NUMBER, INVOICE_SERIES
          ),
          "Exception is thrown when service adds regular transport to payment "
                + "sheet but regular transport invoice "
                + "with series and number not found."
    );

    // Verify that the exception message is as expected
    assertEquals(
          DefaultPaymentSheetService.INVOICE_NOT_FOUND, exception.getMessage(),
          "Exception message is invoice not found."
    );

    // Verify that no regular transport was saved
    verify(regularTransportRepository, never())
          .save(any(PersistentRegularTransportEntity.class));
    verify(invoiceRepository, never()).save(any(PersistentInvoiceEntity.class));
    verify(paymentSheetRepository, never())
          .save(any(PersistentPaymentSheetEntity.class));
  }

  /**
   * Tests the scenario where a food housing already exists when adding
   * it to a payment sheet.
   *
   * This test verifies that when a food housing already exists
   * in the payment sheet, a PaymentSheetServiceException is thrown with
   * the appropriate message.
   */
  @Test
  void testAddFoodHousingToPaymentSheetFoodHousingExists() {
    // Create a User object for the userOwner field
    var user = new PersistentUserEntity();

    // Set up the payment sheet entity with an existing FoodHousing
    var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(user)
          .reason(PAYMENT_SHEET_REASON) // Use constant for reason
          .place(PAYMENT_SHEET_PLACE) // Use constant for place
          .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
          .foodHousing(new PersistentFoodHousingEntity())
          // Existing FoodHousing
          .build();

    // Configure mocks
    when(paymentSheetRepository.findById(PAYMENT_SHEET_ID))
          .thenReturn(Optional.of(paymentSheet));

    // Execute the method under test and verify expected exception thrown
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.addFoodHousingToPaymentSheet(
                PAYMENT_SHEET_ID, FOOD_HOUSING_QUANTITY, FOOD_HOUSING_AMOUNT,
                FOOD_HOUSING_PROVIDED
          ), "Exception is thrown when food housing already exists."
    );

    // Verify that the exception message is as expected
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_FOOD_HOUSING_EXISTS,
          exception.getMessage(),
          "Exception message indicates food housing exists."
    );

    // Verify that no FoodHousing was saved
    verify(foodHousingRepository, never())
          .save(any(PersistentFoodHousingEntity.class));
    verify(paymentSheetRepository, never())
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testRemoveSelfVehicleSelfVehicleNotFound() {
    // Crear una entidad PaymentSheet sin SelfVehicle
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity())
          // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).selfVehicle(null)
          // Simula que el PaymentSheet no tiene un SelfVehicle
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Ejecutar el método bajo prueba y verificar la excepción
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeSelfVehicle(1),
          "Exception is thrown when self vehicle with identifier not found."
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.SELF_VEHICLE_NOT_FOUND,
          exception.getMessage(), "Exception message is vehicle not found."
    );

    // Verificar que el SelfVehicle no fue eliminado
    verify(selfVehicleRepository, times(0))
          .delete(any(PersistentSelfVehicleEntity.class));

    //  PaymentSheet no fue guardado, la excepción fue lanzada antes
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));

  }

  @Test
  void testRemoveFoodHousingFoodHousingNotFound() {
    // Crear una entidad PaymentSheet sin FoodHousing
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity())
          // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).foodHousing(null)
          // Simula que el PaymentSheet no tiene FoodHousing
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Ejecutar el método y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeFoodHousing(1),
          "Exception is thrown when food housing with identifier not found."
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.FOOD_HOUSING_NOT_FOUND,
          exception.getMessage(), "Exception message is food housing not found."
    );

    // Verificar  FoodHousing no eliminado
    // (debe ser 0 ya que no había FoodHousing para eliminar)
    verify(foodHousingRepository, times(0))
          .delete(any(PersistentFoodHousingEntity.class));

    //  PaymentSheet no guardado,la excepción fue lanzada antes
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
  }

  /**
   * Tests the scenario where a regular transport cannot be found
   * when attempting to remove it from a payment sheet.
   *
   * This test verifies that when a regular transport does not exist
   * in the repository, a PaymentSheetServiceException is thrown
   * with the appropriate message.
   */
  @Test
  void testRemoveRegularTransportRegularTransportNotFound() {
    // Create a PaymentSheet entity with an empty list of RegularTransports
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Provide the necessary User
          .reason(PAYMENT_SHEET_REASON) // Use constant for reason
          .place(PAYMENT_SHEET_PLACE) // Use constant for place
          .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
          .regularTransports(new HashSet<>()) // Empty list
          .build();

    // Configure mocks
    when(paymentSheetRepository.findById(PAYMENT_SHEET_ID))
          .thenReturn(Optional.of(paymentSheet));
    when(regularTransportRepository.findById(REGULAR_TRANSPORT_ID))
          .thenReturn(Optional.empty());
    // Simulate that the transport is not found

    // Execute the method under test and verify expected exception thrown
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeRegularTransportFromPaymentSheet(
                PAYMENT_SHEET_ID, REGULAR_TRANSPORT_ID
          ),
          "Exception is thrown when regular transport with "
                + "identifier not found."
    );

    // Verify that the exception message is as expected
    assertEquals(
          DefaultPaymentSheetService.REGULAR_TRANSPORT_NOT_FOUND,
          exception.getMessage(),
          "Exception message indicates regular transport not found."
    );

    // Verify that the PaymentSheet was not modified or saved
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
    verify(regularTransportRepository, times(0))
          .delete(any(PersistentRegularTransportEntity.class));
  }

  @Test
  void testRemoveRegularTransportNotInPaymentSheet() {
    // Crear una entidad PaymentSheet con una lista que no contiene
    // el RegularTransport a eliminar
    var regularTransportNotInSheet = PersistentRegularTransportEntity.builder()
          .category("Category").description("Description")
          .transportInvoice(new PersistentInvoiceEntity()).build();

    var regularTransportToRemove = PersistentRegularTransportEntity.builder()
          .category("Different Category").description("Different Description")
          .transportInvoice(new PersistentInvoiceEntity()).build();

    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity())
          // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1))
          .regularTransports(Set.of(regularTransportNotInSheet))
          // Lista sin el RegularTransport a eliminar
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(regularTransportRepository.findById(2))
          .thenReturn(Optional.of(regularTransportToRemove));

    // Ejecutar el método bajo prueba y verificar
    // que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService
                .removeRegularTransportFromPaymentSheet(1, 2),
          "Exception is thrown when we delete regular transport from payment"
                + " sheet but regular transport is in other payment sheet."
    );

    // Verificar el mensaje de la excepción
    assertEquals(
          DefaultPaymentSheetService.REGULAR_TRANSPORT_NOT_IN_PAYMENT_SHEET,
          exception.getMessage(),
          "Exception have message regular transport not in payment sheet."
    );

    // Verificar que el RegularTransport no fue eliminado
    verify(regularTransportRepository, times(0))
          .delete(any(PersistentRegularTransportEntity.class));

    // Verificar que el PaymentSheet no fue modificado ni guardado
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
  }

}
