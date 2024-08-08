
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PersistentFoodHousingEntityTest {
  @Test
  void testGettersAndSetters() {
    // Crear instancia de PersistentFoodHousingEntity
    var foodHousing = new PersistentFoodHousingEntity();

    // Establecer valores
    foodHousing.setAmountDays(5);
    foodHousing.setDayPrice(50.0);
    foodHousing.setOvernight(true);

    // Verificar valores obtenidos
    assertEquals(5, foodHousing.getAmountDays(), "getter");
    assertEquals(50.0, foodHousing.getDayPrice(), "getter");
    assertTrue(foodHousing.getOvernight(), "getter");
  }

  @Test
  void testHashCode() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);
    // Crear dos instancias con los mismos valores
    var foodHousing1 = PersistentFoodHousingEntity.builder().amountDays(5)
          .dayPrice(50.0).overnight(true).paymentSheet(paymentSheet).build();

    var foodHousing2 = PersistentFoodHousingEntity.builder().amountDays(5)
          .dayPrice(50.0).overnight(true).paymentSheet(paymentSheet).build();

    assertEquals(
          foodHousing1.hashCode(), foodHousing2.hashCode(),
          "los hashcodes son iguales"
    );
  }

  @Test
  void testEquals() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);
    // Crear dos instancias con los mismos valores
    var foodHousing1 = PersistentFoodHousingEntity.builder().amountDays(5)
          .dayPrice(50.0).overnight(true).paymentSheet(paymentSheet).build();

    var foodHousing2 = PersistentFoodHousingEntity.builder().amountDays(5)
          .dayPrice(50.0).overnight(true).paymentSheet(paymentSheet).build();

    assertEquals(foodHousing1, foodHousing2, "las instancias son iguales");
    assertEquals(foodHousing2, foodHousing1, "las instancias son iguales");

    assertEquals(
          foodHousing1, foodHousing1, "una instancia es igual a sí misma"
    );

    PersistentFoodHousingEntity foodHousingNull = null;
    assertNotEquals(
          foodHousing1, foodHousingNull, "una instancia no es igual a null"
    );

    var otherObject = "other";
    assertNotEquals(
          foodHousing1, otherObject,
          "una instancia no es igual a una clase diferente"
    );
  }

  @Test
  void testGetPaymentSheet() {
    // Mockear la entidad externa PersistentPaymentSheetEntity
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    // Crear una instancia de PersistentFoodHousingEntity con el objeto mockeado
    var foodHousing =
          PersistentFoodHousingEntity.builder().paymentSheet(paymentSheet)
                .amountDays(5).dayPrice(50.0).overnight(true).build();

    assertEquals(
          paymentSheet, foodHousing.getPaymentSheet(),
          "el objeto devuelto es el mismo que el objeto mockeado"
    );
  }

  @Test
  void testSetPaymentSheet() {
    // Mockear la entidad externa PersistentPaymentSheetEntity
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    // Crear una instancia de PersistentFoodHousingEntity
    var foodHousing = new PersistentFoodHousingEntity();

    // Establecer el objeto mockeado
    foodHousing.setPaymentSheet(paymentSheet);

    assertEquals(
          paymentSheet, foodHousing.getPaymentSheet(),
          "el objeto devuelto es el mismo que el objeto mockeado"
    );
  }

  @Test
  void testGetInvoices() {
    // Mockear la entidad externa PersistentInvoiceEntity
    var invoice1 = mock(PersistentInvoiceEntity.class);
    var invoice2 = mock(PersistentInvoiceEntity.class);
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);
    // Crear un conjunto de facturas simulado
    Set<PersistentInvoiceEntity> invoices = new HashSet<>();
    invoices.add(invoice1);
    invoices.add(invoice2);

    // Crear una instancia de PersistentFoodHousingEntity con el conjunto simulado
    var foodHousing = PersistentFoodHousingEntity.builder().invoices(invoices)
          .amountDays(5).dayPrice(50.0).overnight(true)
          .paymentSheet(paymentSheet).build();

    assertTrue(
          foodHousing.getInvoices().contains(invoice1),
          "el conjunto devuelto contiene las facturas simuladas"
    );
    assertTrue(
          foodHousing.getInvoices().contains(invoice2),
          "el conjunto devuelto contiene las facturas simuladas"
    );
  }

}
