
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import org.junit.jupiter.api.Test;

class PersistentCountryEntityTest {

  @Test
  void testGetters() {
    // Crear una instancia de PersistentCountryEntity
    var country = new PersistentCountryEntity();

    // Establecer valores utilizando los métodos setter
    country.setNumericCode(123);
    country.setFullName("Example Full Name");
    country.setShortName("Example Short Name");
    country.setAlpha2Code("AB");
    country.setAlpha3Code("ABC");

    // Verificar que los valores se establecieron correctamente utilizando los métodos getter
    assertEquals(123, country.getNumericCode(), "getter");
    assertEquals("Example Full Name", country.getFullName(), "getter");
    assertEquals("Example Short Name", country.getShortName(), "getter");
    assertEquals("AB", country.getAlpha2Code(), "getter");
    assertEquals("ABC", country.getAlpha3Code(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentCountryEntity con los mismos atributos
    var country1 = new PersistentCountryEntity();
    country1.setNumericCode(123);
    country1.setFullName("Example Full Name");
    country1.setShortName("Example Short Name");
    country1.setAlpha2Code("AB");
    country1.setAlpha3Code("ABC");

    var country2 = new PersistentCountryEntity();
    country2.setNumericCode(123);
    country2.setFullName("Example Full Name");
    country2.setShortName("Example Short Name");
    country2.setAlpha2Code("AB");
    country2.setAlpha3Code("ABC");

    assertEquals(
          country1, country2, "country1 y country2 son iguales según equals()"
    );
    assertEquals(
          country2, country1, "country1 y country2 son iguales según equals()"
    );

    assertEquals(
          country1.hashCode(), country2.hashCode(),
          "hash codes de country1 y country2 son iguales"
    );

    // Modificar un atributo en country2
    country2.setFullName("Different Full Name");

    assertNotEquals(
          country1, country2, "country1 y country2 ya no son iguales"
    );
    assertNotEquals(
          country2, country1, "country1 y country2 ya no son iguales"
    );

    assertNotEquals(
          country1.hashCode(), country2.hashCode(),
          "hash codes de country1 y country2 son diferentes después de modificar country2"
    );

    PersistentCountryEntity country3 = null;
    assertNotEquals(country1, country3, "notEquals con un valor nulo");

    var otherObject = "This is not a PersistentCountryEntity";
    assertNotEquals(country1, otherObject, "notEquals con otro tipo de objeto");
  }

}
