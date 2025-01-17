
package es.org.cxn.backapp.test.unit.entity;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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

    // Verificar que los valores se establecieron correctamente
    // utilizando los métodos getter
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
          "Verificar que los hash codes de address1 y address2 son diferentes"
                + " después de modificar address2"
    );

    PersistentAddressEntity nullAddress = null;
    assertNotEquals(
          address1, nullAddress, "Comprobar notEquals con un valor nulo"
    );

    var otherObject = "This is not a PersistentAddressEntity";
    assertNotEquals(
          address1, otherObject, "Comprobar notEquals con otro tipo de objeto"
    );
  }

}
