
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

class PaymentSheetServiceTest {

  @Mock
  private PaymentSheetEntityRepository paymentSheetRepository;

  @Mock
  private UserEntityRepository userRepository;

  @Mock
  private InvoiceEntityRepository invoiceRepository;

  @Mock
  private RegularTransportRepository regularTransportRepository;

  @Mock
  private SelfVehicleRepository selfVehicleRepository;

  @Mock
  private FoodHousingRepository foodHousingRepository;

  @InjectMocks
  private DefaultPaymentSheetService paymentSheetService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddPaymentSheet_Success() throws PaymentSheetServiceException {
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
    assertNotNull(result);
    assertEquals(reason, result.getReason());
    verify(paymentSheetRepository, times(1))
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testAddPaymentSheet_UserNotFound() {
    var userEmail = "user@example.com";
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.add(
            "reason", "place", LocalDate.now(), LocalDate.now().plusDays(1),
            userEmail
      );
    });
    assertEquals(
          DefaultPaymentSheetService.USER_NOT_FOUND_MESSAGE, thrown.getMessage()
    );
  }

  @Test
  void testFindById_Success() throws PaymentSheetServiceException {
    // Construcción de un objeto `PersistentPaymentSheetEntity` con parámetros necesarios
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Asegúrate de proporcionar un usuario válido
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
    assertNotNull(result);

    // Verificación de que el repositorio fue llamado exactamente una vez
    verify(paymentSheetRepository, times(1)).findById(1);
  }

  @Test
  void testFindById_NotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.findById(1);
    });
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND_MESSAGE,
          thrown.getMessage()
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

    // Configurar el mock para el repositorio de entidades de pago para que no haga nada en delete().
    doNothing().when(paymentSheetRepository).delete(paymentSheet);

    // Ejecutar el método bajo prueba.
    paymentSheetService.remove(1);

    // Verificar que el método delete() del repositorio de entidades de pago fue llamado exactamente una vez.
    verify(paymentSheetRepository, times(1)).delete(paymentSheet);
  }

  @Test
  void testRemove_NotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.remove(1);
    });
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND_MESSAGE,
          thrown.getMessage()
    );
  }

  @Test
  void testUpdatePaymentSheet_Success() throws PaymentSheetServiceException {
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Asegúrate de proporcionar un usuario válido
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
    assertNotNull(result);
    verify(paymentSheetRepository, times(1)).save(paymentSheet);
  }

  @Test
  void testUpdatePaymentSheet_NotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.updatePaymentSheet(
            1, "new reason", "new place", LocalDate.now(),
            LocalDate.now().plusDays(1)
      );
    });
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND_MESSAGE,
          thrown.getMessage()
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
          123, "A", LocalDate.now(), LocalDate.now().plusDays(1), false, seller,
          buyer
    );

    // Configuración del transporte regular con campos necesarios
    var regularTransport =
          PersistentRegularTransportEntity.builder().transportInvoice(invoice)
                .category("category").description("description").build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(invoiceRepository.findByNumberAndSeries(123, "A"))
          .thenReturn(Optional.of(invoice));
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
          1, "category", "description", 123, "A"
    );

    // Verificar que el resultado no sea nulo
    assertNotNull(result);

    // Verificar que el repositorio de pago fue guardado
    verify(paymentSheetRepository, times(1)).save(paymentSheet);

    // Verificar que el transporte regular fue guardado
    verify(regularTransportRepository, times(1))
          .save(any(PersistentRegularTransportEntity.class));
  }

  @Test
  void testAddRegularTransportToPaymentSheet_PaymentSheetNotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.addRegularTransportToPaymentSheet(
            1, "category", "description", 1, "series"
      );
    });
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND_MESSAGE,
          thrown.getMessage()
    );
  }

  @Test
  void testRemoveSelfVehicle_Success() throws PaymentSheetServiceException {
    // Crear una entidad de pago con un vehículo asociado.
    var selfVehicle = PersistentSelfVehicleEntity.builder().places("Some Place") // Proporciona un valor no nulo para 'places'
          .distance(100.0f) // Proporciona un valor para 'distance'
          .kmPrice(0.50) // Proporciona un valor para 'kmPrice'
          .build();

    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .reason("sample reason").place("Feorae").startDate(LocalDate.now())
          .endDate(LocalDate.now()).userOwner(new PersistentUserEntity())
          .selfVehicle(selfVehicle) // Asigna el vehículo al pago
          .build();

    // Configurar el mock para el repositorio de pago.
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Configurar el mock para el repositorio de vehículos para que no haga nada en delete().
    doNothing().when(selfVehicleRepository)
          .delete(any(PersistentSelfVehicleEntity.class));

    // Ejecutar el método bajo prueba.
    paymentSheetService.removeSelfVehicle(1);

    // Capturar el argumento pasado al método delete().
    var captor = ArgumentCaptor.forClass(PersistentSelfVehicleEntity.class);
    verify(selfVehicleRepository, times(1)).delete(captor.capture());

    // Verificar que el argumento capturado es el mismo objeto que se esperaba.
    assertEquals(
          selfVehicle, captor.getValue(),
          "El vehículo eliminado no coincide con el esperado."
    );

    // Verificar que el vehículo se eliminó correctamente (es decir, que ahora es nulo en la entidad de pago).
    assertNull(paymentSheet.getSelfVehicle());
  }

  @Test
  void testRemoveSelfVehicle_NotFound() {
    when(paymentSheetRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(PaymentSheetServiceException.class, () -> {
      paymentSheetService.removeSelfVehicle(1);
    });
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_NOT_FOUND_MESSAGE,
          thrown.getMessage()
    );
  }

  @Test
  void testAddRegularTransportToPaymentSheetInvoiceNotFound() {
    // Crear un objeto User para el campo userOwner
    var user = new PersistentUserEntity();

    // Configuración de la entidad de pago con campos necesarios
    var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(user)
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(invoiceRepository.findByNumberAndSeries(123, "A"))
          .thenReturn(Optional.empty()); // Simula que la factura no se encuentra

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.addRegularTransportToPaymentSheet(
                1, "category", "description", 123, "A"
          )
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.INVOICE_NOT_FOUND, exception.getMessage()
    );

    // Verificar que ningún transporte regular se guardó
    verify(regularTransportRepository, never())
          .save(any(PersistentRegularTransportEntity.class));
    verify(invoiceRepository, never()).save(any(PersistentInvoiceEntity.class));
    verify(paymentSheetRepository, never())
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testAddFoodHousingToPaymentSheetFoodHousingExists() {
    // Crear un objeto User para el campo userOwner
    var user = new PersistentUserEntity();

    // Configuración de la entidad de pago con un FoodHousing ya existente
    var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(user)
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1))
          .foodHousing(new PersistentFoodHousingEntity()) // Simula que ya existe un FoodHousing
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService
                .addFoodHousingToPaymentSheet(1, 5, 100.0f, true)
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.PAYMENT_SHEET_FOOD_HOUSING_EXISTS,
          exception.getMessage()
    );

    // Verificar que ningún FoodHousing fue guardado
    verify(foodHousingRepository, never())
          .save(any(PersistentFoodHousingEntity.class));
    verify(paymentSheetRepository, never())
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testRemoveSelfVehicleSelfVehicleNotFound()
        throws PaymentSheetServiceException {
    // Crear una entidad PaymentSheet sin SelfVehicle
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).selfVehicle(null) // Simula que el PaymentSheet no tiene un SelfVehicle
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeSelfVehicle(1)
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.SELF_VEHICLE_NOT_FOUND_MESSAGE,
          exception.getMessage()
    );

    // Verificar que el SelfVehicle no fue eliminado
    verify(selfVehicleRepository, times(0))
          .delete(any(PersistentSelfVehicleEntity.class));

    // Verificar que el PaymentSheet no fue guardado, ya que la excepción fue lanzada antes
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));

  }

  @Test
  void testRemoveFoodHousingFoodHousingNotFound()
        throws PaymentSheetServiceException {
    // Crear una entidad PaymentSheet sin FoodHousing
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1)).foodHousing(null) // Simula que el PaymentSheet no tiene FoodHousing
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeFoodHousing(1)
    );

    // Verificar que el mensaje de la excepción es el esperado
    assertEquals(
          DefaultPaymentSheetService.FOOD_HOUSING_NOT_FOUND_MESSAGE,
          exception.getMessage()
    );

    // Verificar que el FoodHousing no fue eliminado (debe ser 0 ya que no había FoodHousing para eliminar)
    verify(foodHousingRepository, times(0))
          .delete(any(PersistentFoodHousingEntity.class));

    // Verificar que el PaymentSheet no fue guardado, ya que la excepción fue lanzada antes
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
  }

  @Test
  void testRemoveRegularTransportRegularTransportNotFound() {
    // Crear una entidad PaymentSheet con una lista vacía de RegularTransports
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1))
          .regularTransports(new HashSet<>()) // Lista vacía
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(regularTransportRepository.findById(99)).thenReturn(Optional.empty());

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService
                .removeRegularTransportFromPaymentSheet(1, 99)
    );

    // Verificar el mensaje de la excepción
    assertEquals(
          DefaultPaymentSheetService.REGULAR_TRANSPORT_NOT_FOUND_MESSAGE,
          exception.getMessage()
    );

    // Verificar que el PaymentSheet no fue modificado ni guardado
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
    verify(regularTransportRepository, times(0))
          .delete(any(PersistentRegularTransportEntity.class));
  }

  @Test
  void testRemoveRegularTransportNotInPaymentSheet() {
    // Crear una entidad PaymentSheet con una lista que no contiene el RegularTransport a eliminar
    var regularTransportNotInSheet = PersistentRegularTransportEntity.builder()
          .category("Category").description("Description")
          .transportInvoice(new PersistentInvoiceEntity()).build();

    var regularTransportToRemove = PersistentRegularTransportEntity.builder()
          .category("Different Category").description("Different Description")
          .transportInvoice(new PersistentInvoiceEntity()).build();

    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(new PersistentUserEntity()) // Proporcionar el User necesario
          .reason("Business trip").place("Office").startDate(LocalDate.now())
          .endDate(LocalDate.now().plusDays(1))
          .regularTransports(Set.of(regularTransportNotInSheet)) // Lista sin el RegularTransport a eliminar
          .build();

    // Configuración de mocks
    when(paymentSheetRepository.findById(1))
          .thenReturn(Optional.of(paymentSheet));
    when(regularTransportRepository.findById(2))
          .thenReturn(Optional.of(regularTransportToRemove));

    // Ejecutar el método bajo prueba y verificar que se lanza la excepción esperada
    var exception = assertThrows(
          PaymentSheetServiceException.class,
          () -> paymentSheetService.removeRegularTransportFromPaymentSheet(1, 2)
    );

    // Verificar el mensaje de la excepción
    assertEquals(
          DefaultPaymentSheetService.REGULAR_TRANSPORT_NOT_IN_PAYMENT_SHEET_MESSAGE,
          exception.getMessage()
    );

    // Verificar que el RegularTransport no fue eliminado
    verify(regularTransportRepository, times(0))
          .delete(any(PersistentRegularTransportEntity.class));

    // Verificar que el PaymentSheet no fue modificado ni guardado
    verify(paymentSheetRepository, times(0))
          .save(any(PersistentPaymentSheetEntity.class));
  }

}
