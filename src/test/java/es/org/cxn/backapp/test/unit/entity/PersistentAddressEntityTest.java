
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;

import org.junit.jupiter.api.Test;

class PersistentAddressEntityTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de PersistentAddressEntity
    var address = new PersistentAddressEntity();

    // Establecer valores utilizando los métodos setter
    address.setUserDni("12345678A");
    address.setPostalCode("12345");
    address.setApartmentNumber("A1");
    address.setBuilding("Building Name");
    address.setStreet("Example Street");
    address.setCity("Example City");

    // Verificar que los valores se establecieron correctamente utilizando los métodos getter
    assertEquals("12345678A", address.getUserDni(), "getter");
    assertEquals("12345", address.getPostalCode(), "getter");
    assertEquals("A1", address.getApartmentNumber(), "getter");
    assertEquals("Building Name", address.getBuilding(), "getter");
    assertEquals("Example Street", address.getStreet(), "getter");
    assertEquals("Example City", address.getCity(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentAddressEntity con los mismos atributos
    var address1 = new PersistentAddressEntity();
    address1.setUserDni("12345678A");
    address1.setPostalCode("12345");
    address1.setApartmentNumber("A1");
    address1.setBuilding("Building Name");
    address1.setStreet("Example Street");
    address1.setCity("Example City");

    var address2 = new PersistentAddressEntity();
    address2.setUserDni("12345678A");
    address2.setPostalCode("12345");
    address2.setApartmentNumber("A1");
    address2.setBuilding("Building Name");
    address2.setStreet("Example Street");
    address2.setCity("Example City");

    assertEquals(
          address1, address1, "Verificar que address1 es equals con si mismo"
    );

    assertEquals(
          address1, address2,
          "Verificar que address1 y address2 son iguales según equals()"
    );
    assertEquals(
          address2, address1,
          "Verificar que address1 y address2 son iguales según equals()"
    );

    assertEquals(
          address1.hashCode(), address2.hashCode(),
          "Verificar que los hash codes de address1 y address2 son iguales"
    );

    // Modificar un atributo en address2
    address2.setPostalCode("54321");

    assertNotEquals(
          address1, address2,
          "Verificar que address1 y address2 ya no son iguales"
    );
    assertNotEquals(
          address2, address1,
          "Verificar que address1 y address2 ya no son iguales"
    );

    assertNotEquals(
          address1.hashCode(), address2.hashCode(),
          "Verificar que los hash codes de address1 y address2 son diferentes después de modificar address2"
    );

    assertNotEquals(address1, null, "Comprobar notEquals con un valor nulo");

    var otherObject = "This is not a PersistentAddressEntity";
    assertNotEquals(
          address1, otherObject, "Comprobar notEquals con otro tipo de objeto"
    );
  }

}
