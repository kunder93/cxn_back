
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

import org.junit.jupiter.api.Test;

class PersistentCountrySubdivisionEntityTest {

  @Test
  void testGettersAndSetters() {
    // Crear una instancia de PersistentCountrySubdivisionEntity
    var countrySubdivision = new PersistentCountrySubdivisionEntity();

    // Establecer valores utilizando los métodos setter
    countrySubdivision.setKindSubdivisionName("Example Kind Subdivision");
    countrySubdivision.setName("Example Name");
    countrySubdivision.setCode("ABC");

    assertEquals(
          "Example Kind Subdivision",
          countrySubdivision.getKindSubdivisionName(),
          "Verificar que los valores se establecieron correctamente "
                + "utilizando los métodos getter"
    );
    assertEquals("Example Name", countrySubdivision.getName(), "getter");
    assertEquals("ABC", countrySubdivision.getCode(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentCountrySubdivisionEntity con
    // los mismos atributos
    var countrySubdivision1 = new PersistentCountrySubdivisionEntity();
    countrySubdivision1.setKindSubdivisionName("Example Kind Subdivision");
    countrySubdivision1.setName("Example Name");
    countrySubdivision1.setCode("ABC");

    var countrySubdivision2 = new PersistentCountrySubdivisionEntity();
    countrySubdivision2.setKindSubdivisionName("Example Kind Subdivision");
    countrySubdivision2.setName("Example Name");
    countrySubdivision2.setCode("ABC");

    assertEquals(
          countrySubdivision1, countrySubdivision1,
          "Verificar que countrySubdivision1 es igual a si mismo según equals()"
    );

    assertEquals(
          countrySubdivision1, countrySubdivision2,
          "Verificar que countrySubdivision1 y countrySubdivision2 son "
                + "iguales según equals()"
    );
    assertEquals(
          countrySubdivision2, countrySubdivision1,
          "Verificar que countrySubdivision1 y countrySubdivision2 son "
                + "iguales según equals()"
    );

    assertEquals(
          countrySubdivision1.hashCode(), countrySubdivision2.hashCode(),
          "Verificar que los hash codes de countrySubdivision1 y "
                + "countrySubdivision2 son iguales"
    );

    // Modificar un atributo en countrySubdivision2
    countrySubdivision2.setCode("DEF");

    assertNotEquals(
          countrySubdivision1, countrySubdivision2,
          "Verificar que countrySubdivision1 y countrySubdivision2 "
                + "ya no son iguales"
    );
    assertNotEquals(
          countrySubdivision2, countrySubdivision1,
          "Verificar que countrySubdivision1 y countrySubdivision2 "
                + "ya no son iguales"
    );

    assertNotEquals(
          countrySubdivision1.hashCode(), countrySubdivision2.hashCode(),
          "Verificar que los hash codes de countrySubdivision1 y "
                + "countrySubdivision2 son diferentes después de modificar "
                + "countrySubdivision2"
    );
    PersistentCountrySubdivisionEntity nullCountrySubdivision = null;
    assertNotEquals(
          countrySubdivision1, nullCountrySubdivision,
          "Comprobar notEquals con un valor nulo"
    );

    var otherObject = "This is not a PersistentCountrySubdivisionEntity";
    assertNotEquals(
          countrySubdivision1, otherObject,
          "Comprobar notEquals con otro tipo de objeto"
    );
  }
}
