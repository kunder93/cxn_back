
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;

import org.junit.jupiter.api.Test;

class CreateCompanyRequestTest {

  @Test
  void testGettersAndSetters() {
    final var nif = "123456789A";
    final var name = "Company ABC";

    final var address = "123 Main St";

    // Crear una instancia de CreateCompanyRequestForm
    var companyRequestForm = new CreateCompanyRequestForm(nif, name, address);

    //
    assertEquals(
          "123456789A", companyRequestForm.nif(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          "Company ABC", companyRequestForm.name(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          "123 Main St", companyRequestForm.address(),
          "Verifica los valores usando getters"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de CreateCompanyRequestForm con los mismos valores
    var companyRequestForm1 = new CreateCompanyRequestForm(
          "123456789A", "Company ABC", "123 Main St"
    );
    var companyRequestForm2 = new CreateCompanyRequestForm(
          "123456789A", "Company ABC", "123 Main St"
    );

    assertEquals(
          companyRequestForm1.hashCode(), companyRequestForm2.hashCode(),
          "los hashCodes son iguales"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de CreateCompanyRequestForm con los mismos valores
    var companyRequestForm1 = new CreateCompanyRequestForm(
          "123456789A", "Company ABC", "123 Main St"
    );
    var companyRequestForm2 = new CreateCompanyRequestForm(
          "123456789A", "Company ABC", "123 Main St"
    );

    // Verificar que
    assertEquals(
          companyRequestForm1, companyRequestForm2,
          " las instancias son iguales usando equals"
    );
    assertEquals(
          companyRequestForm2, companyRequestForm1,
          " las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de CreateCompanyRequestForm con diferentes valores
    var companyRequestForm1 = new CreateCompanyRequestForm(
          "123456789A", "Company ABC", "123 Main St"
    );
    var companyRequestForm2 = new CreateCompanyRequestForm(
          "987654321B", "Company XYZ", "456 Elm St"
    );

    assertNotEquals(
          companyRequestForm1, companyRequestForm2,
          "las instancias no son iguales usando equals"
    );
    assertNotEquals(
          companyRequestForm2, companyRequestForm1,
          "las instancias no son iguales usando equals"
    );
  }

}
