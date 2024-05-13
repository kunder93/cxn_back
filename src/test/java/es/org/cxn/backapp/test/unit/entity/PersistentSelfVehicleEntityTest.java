
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;

import org.junit.jupiter.api.Test;

class PersistentSelfVehicleEntityTest {

  @Test
  void testGettersAndSetters() {
    // Crear un mock para PersistentPaymentSheetEntity
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    // Crear una instancia de PersistentSelfVehicleEntity
    var selfVehicle = new PersistentSelfVehicleEntity();

    // Establecer valores utilizando los métodos setter
    selfVehicle.setPlaces("Example Places");
    selfVehicle.setDistance(100.0f);
    selfVehicle.setKmPrice(1.5);
    selfVehicle.setPaymentSheet(paymentSheet);

    // Verificar que los valores se establecieron correctamente utilizando los métodos getter
    assertEquals("Example Places", selfVehicle.getPlaces(), "getter");
    assertEquals(100.0f, selfVehicle.getDistance(), "getter");
    assertEquals(1.5, selfVehicle.getKmPrice(), "getter");
    assertEquals(paymentSheet, selfVehicle.getPaymentSheet(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentSelfVehicleEntity con los mismos atributos
    var selfVehicle1 = new PersistentSelfVehicleEntity();
    selfVehicle1.setPlaces("Example Places");
    selfVehicle1.setDistance(100.0f);
    selfVehicle1.setKmPrice(1.5);
    var paymentSheet1 = mock(PersistentPaymentSheetEntity.class);
    selfVehicle1.setPaymentSheet(paymentSheet1);

    var selfVehicle2 = new PersistentSelfVehicleEntity();
    selfVehicle2.setPlaces("Example Places");
    selfVehicle2.setDistance(100.0f);
    selfVehicle2.setKmPrice(1.5);
    selfVehicle2.setPaymentSheet(paymentSheet1);

    assertEquals(
          selfVehicle1, selfVehicle1,
          "Verifica que selfVehicle1 es equals con sigo misma."
    );

    assertEquals(
          selfVehicle1, selfVehicle2,
          "Verificar que selfVehicle1 y selfVehicle2 son iguales según equals()"
    );
    assertEquals(
          selfVehicle2, selfVehicle1,
          "Verificar que selfVehicle1 y selfVehicle2 son iguales según equals()"
    );

    assertEquals(
          selfVehicle1.hashCode(), selfVehicle2.hashCode(),
          "Verificar que los hash codes de selfVehicle1 y selfVehicle2 son iguales"
    );

    // Modificar un atributo en selfVehicle2
    selfVehicle2.setKmPrice(2.0);

    assertNotEquals(
          selfVehicle1, selfVehicle2,
          "Verificar que selfVehicle1 y selfVehicle2 ya no son iguales"
    );
    assertNotEquals(
          selfVehicle2, selfVehicle1,
          "Verificar que selfVehicle1 y selfVehicle2 ya no son iguales"
    );

    assertNotEquals(
          selfVehicle1.hashCode(), selfVehicle2.hashCode(),
          "Verificar que los hash codes de selfVehicle1 y selfVehicle2 son diferentes después de modificar selfVehicle2"
    );
    PersistentSelfVehicleEntity nullVehicle = null;
    assertNotEquals(
          selfVehicle1, nullVehicle, "Comprobar notEquals con un valor nulo"
    );

    var otherObject = "This is not a PersistentSelfVehicleEntity";
    assertNotEquals(
          selfVehicle1, otherObject,
          "Comprobar notEquals con otro tipo de objeto"
    );
  }

}
