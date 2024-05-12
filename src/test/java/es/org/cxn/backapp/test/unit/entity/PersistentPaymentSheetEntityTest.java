
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PersistentPaymentSheetEntityTest {
  @Test
  void testGetReason() {
    var userOwner = mock(PersistentUserEntity.class);
    // Crear una instancia de PersistentPaymentSheetEntity con valores de ejemplo
    var paymentSheet =
          PersistentPaymentSheetEntity.builder().reason("Event reason")
                .place("Event place").startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5)).userOwner(userOwner).build();

    assertEquals(
          "Event reason", paymentSheet.getReason(),
          "el motivo devuelto es el mismo que el establecido"
    );
  }

  @Test
  void testGetPlace() {
    // Crear una instancia de PersistentPaymentSheetEntity con valores de ejemplo
    var userOwner = mock(PersistentUserEntity.class);
    var paymentSheet =
          PersistentPaymentSheetEntity.builder().reason("Event reason")
                .place("Event place").startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5)).userOwner(userOwner).build();

    assertEquals(
          "Event place", paymentSheet.getPlace(),
          "el lugar devuelto es el mismo que el establecido"
    );
  }

  @Test
  void testGetStartDate() {
    // Crear una instancia de PersistentPaymentSheetEntity con valores de ejemplo
    var userOwner = mock(PersistentUserEntity.class);
    var paymentSheet =
          PersistentPaymentSheetEntity.builder().reason("Event reason")
                .place("Event place").startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5)).userOwner(userOwner).build();

    // Verificar que
    assertEquals(
          LocalDate.of(2024, 5, 1), paymentSheet.getStartDate(),
          "la fecha de inicio devuelta es la misma que la establecida"
    );
  }

  @Test
  void testGetEndDate() {
    var userOwner = mock(PersistentUserEntity.class);
    // Crear una instancia de PersistentPaymentSheetEntity con valores de ejemplo
    var paymentSheet =
          PersistentPaymentSheetEntity.builder().reason("Event reason")
                .place("Event place").startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5)).userOwner(userOwner).build();

    assertEquals(
          LocalDate.of(2024, 5, 5), paymentSheet.getEndDate(),
          "la fecha de finalización devuelta es la misma que la establecida"
    );
  }

  @Test
  void testGetUserOwner() {
    // Mockear la entidad externa PersistentUserEntity
    var userOwner = mock(PersistentUserEntity.class);

    // Crear una instancia de PersistentPaymentSheetEntity con el objeto mockeado
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .userOwner(userOwner).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .build();

    assertEquals(
          userOwner, paymentSheet.getUserOwner(),
          "el objeto devuelto es el mismo que el objeto mockeado"
    );
  }

  @Test
  void testGetRegularTransports() {
    var userOwner = mock(PersistentUserEntity.class);
    // Mockear la entidad externa PersistentRegularTransportEntity
    var transport1 = mock(PersistentRegularTransportEntity.class);
    var transport2 = mock(PersistentRegularTransportEntity.class);

    // Crear un conjunto de transportes simulado
    Set<PersistentRegularTransportEntity> regularTransports = new HashSet<>();
    regularTransports.add(transport1);
    regularTransports.add(transport2);

    // Crear una instancia de PersistentPaymentSheetEntity con el conjunto simulado
    var paymentSheet =
          PersistentPaymentSheetEntity.builder().userOwner(userOwner)
                .regularTransports(regularTransports).reason("Event reason")
                .place("Event place").startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 5)).build();

    assertTrue(
          paymentSheet.getRegularTransports().contains(transport1),
          "el conjunto devuelto contiene los transportes simulados"
    );
    assertTrue(
          paymentSheet.getRegularTransports().contains(transport2),
          "el conjunto devuelto contiene los transportes simulados"
    );
  }

  @Test
  void testGetSelfVehicle() {
    var userOwner = mock(PersistentUserEntity.class);
    // Mockear la entidad externa PersistentSelfVehicleEntity
    var selfVehicle = mock(PersistentSelfVehicleEntity.class);

    // Crear una instancia de PersistentPaymentSheetEntity con el objeto mockeado
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .selfVehicle(selfVehicle).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    assertEquals(
          selfVehicle, paymentSheet.getSelfVehicle(),
          "el objeto devuelto es el mismo que el objeto mockeado"
    );
  }

  @Test
  void testGetFoodHousing() {
    var userOwner = mock(PersistentUserEntity.class);
    // Mockear la entidad externa PersistentFoodHousingEntity
    var foodHousing = mock(PersistentFoodHousingEntity.class);

    // Crear una instancia de PersistentPaymentSheetEntity con el objeto mockeado
    var paymentSheet = PersistentPaymentSheetEntity.builder()
          .foodHousing(foodHousing).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    assertEquals(
          foodHousing, paymentSheet.getFoodHousing(),
          " el objeto devuelto es el mismo que el objeto mockeado"
    );
  }

  @Test
  void testHashCode() {
    var userOwner = mock(PersistentUserEntity.class);
    // Mockear la entidad externa PersistentSelfVehicleEntity
    var selfVehicle1 = mock(PersistentSelfVehicleEntity.class);
    var selfVehicle2 = mock(PersistentSelfVehicleEntity.class);

    // Crear instancias de PersistentPaymentSheetEntity con los objetos mockeados
    var paymentSheet1 = PersistentPaymentSheetEntity.builder()
          .selfVehicle(selfVehicle1).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    var paymentSheet2 = PersistentPaymentSheetEntity.builder()
          .selfVehicle(selfVehicle2).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    // Verificar que
    assertNotEquals(
          paymentSheet1.hashCode(), paymentSheet2.hashCode(),
          "los hash codes son diferentes para objetos con selfVehicle distintos"
    );
  }

  @Test
  void testEquals() {
    var userOwner = mock(PersistentUserEntity.class);
    // Mockear la entidad externa PersistentSelfVehicleEntity
    var selfVehicle1 = mock(PersistentSelfVehicleEntity.class);
    var selfVehicle2 = mock(PersistentSelfVehicleEntity.class);

    // Crear instancias de PersistentPaymentSheetEntity con los objetos mockeados
    var paymentSheet1 = PersistentPaymentSheetEntity.builder()
          .selfVehicle(selfVehicle1).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    var paymentSheet2 = PersistentPaymentSheetEntity.builder()
          .selfVehicle(selfVehicle2).reason("Event reason").place("Event place")
          .startDate(LocalDate.of(2024, 5, 1)).endDate(LocalDate.of(2024, 5, 5))
          .userOwner(userOwner).build();

    assertNotEquals(
          paymentSheet1, paymentSheet2,
          "los objetos son considerados diferentes debido a selfVehicle distintos"
    );
  }

  @Test
  void testGettersAndSetters() {
    // Crear una instancia de PersistentPaymentSheetEntity
    var paymentSheet = new PersistentPaymentSheetEntity();

    // Definir valores de prueba
    Integer id = 1;
    var reason = "Test reason";
    var place = "Test place";
    var startDate = LocalDate.of(2024, 5, 10);
    var endDate = LocalDate.of(2024, 5, 15);

    // Establecer los valores usando setters
    paymentSheet.setId(id);
    paymentSheet.setReason(reason);
    paymentSheet.setPlace(place);
    paymentSheet.setStartDate(startDate);
    paymentSheet.setEndDate(endDate);

    // Verificar que los getters devuelven los valores establecidos
    assertEquals(id, paymentSheet.getId(), "getter");
    assertEquals(reason, paymentSheet.getReason(), "getter");
    assertEquals(place, paymentSheet.getPlace(), "getter");
    assertEquals(startDate, paymentSheet.getStartDate(), "getter");
    assertEquals(endDate, paymentSheet.getEndDate(), "getter");
  }

  @Test
  void testGettersAndSettersWithMocks() {
    // Mockear las entidades externas
    var regularTransport = mock(PersistentRegularTransportEntity.class);
    var selfVehicle = mock(PersistentSelfVehicleEntity.class);
    var foodHousing = mock(PersistentFoodHousingEntity.class);

    // Crear una instancia de PersistentPaymentSheetEntity
    var paymentSheet = new PersistentPaymentSheetEntity();

    // Establecer los mocks
    paymentSheet.getRegularTransports().add(regularTransport);
    paymentSheet.setSelfVehicle(selfVehicle);
    paymentSheet.setFoodHousing(foodHousing);

    assertTrue(
          paymentSheet.getRegularTransports().contains(regularTransport),
          "los getters devuelvan los mocks establecidos"
    );
    assertEquals(
          selfVehicle, paymentSheet.getSelfVehicle(),
          "los getters devuelvan los mocks establecidos"
    );
    assertEquals(
          foodHousing, paymentSheet.getFoodHousing(),
          "los getters devuelvan los mocks establecidos"
    );
  }
}
