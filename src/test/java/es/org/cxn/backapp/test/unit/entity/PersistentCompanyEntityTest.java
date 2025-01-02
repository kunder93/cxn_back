
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class PersistentCompanyEntityTest {

  @Test
  void testGettersAndSetters() {
    // Create a PersistentCompanyEntity instance
    var company = PersistentCompanyEntity.builder().nif("12345678A")
          .name("Example Company").address("123 Example Street").build();

    assertEquals("12345678A", company.getNif(), "Test getter methods");
    assertEquals("Example Company", company.getName(), "Test getter methods");
    assertEquals(
          "123 Example Street", company.getAddress(), "Test getter methods"
    );

    // Test setter methods
    company.setNif("87654321B");
    company.setName("New Company Name");
    company.setAddress("456 New Street");

    assertEquals("87654321B", company.getNif(), "setter/getter");
    assertEquals("New Company Name", company.getName(), "setter/getter");
    assertEquals("456 New Street", company.getAddress(), "setter/getter");
  }

  @Test
  void testInvoiceLists() {
    // Create a mock PersistentInvoiceEntity
    var invoice1 = mock(PersistentInvoiceEntity.class);
    var invoice2 = mock(PersistentInvoiceEntity.class);

    // Create a PersistentCompanyEntity instance
    var company = PersistentCompanyEntity.builder().nif("12345678A")
          .name("Example Company").address("123 Example Street").build();

    // Add invoices as buyer
    company.addInvoicesAsBuyer(invoice1);
    company.addInvoicesAsBuyer(invoice2);

    assertTrue(
          company.getInvoicesAsBuyer()
                .containsAll(Arrays.asList(invoice1.getId(), invoice2.getId())),
          "Verify that invoices are added correctly"
    );

    // Remove an invoice as buyer
    company.removeInvoiceAsBuyer(invoice1);

    assertTrue(
          company.getInvoicesAsBuyer().contains(invoice2.getId()),
          "Verify that the removed invoice is no longer in the list"
    );
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentCompanyEntity con los mismos atributos
    var company1 = PersistentCompanyEntity.builder().nif("12345678A")
          .name("Example Company").address("123 Example Street").build();

    var company2 = PersistentCompanyEntity.builder().nif("12345678A")
          .name("Example Company").address("123 Example Street").build();

    assertEquals(
          company1, company2,
          "Verificar que company1 y company2 son iguales según equals()"
    );
    assertEquals(
          company2, company1,
          "Verificar que company1 y company2 son iguales según equals()"
    );

    assertEquals(
          company1.hashCode(), company2.hashCode(),
          "Verificar que los hash codes de company1 y company2 son iguales"
    );

    // Modificar un atributo en company2
    company2.setAddress("456 New Street");

    assertNotEquals(
          company1, company2,
          "Verificar que company1 y company2 ya no son iguales"
    );
    assertNotEquals(
          company2, company1,
          "Verificar que company1 y company2 ya no son iguales"
    );

    assertNotEquals(
          company1.hashCode(), company2.hashCode(),
          "Verificar que los hash codes de company1 y company2 son diferentes"
                + " después de modificar company2"
    );
    PersistentCompanyEntity nullCompany = null;
    assertNotEquals(company1, nullCompany, "Verificar no es equals con null");

    var otherObject = "This is not a PersistentCompanyEntity";
    assertNotEquals(
          company1, otherObject, "Comprobar notEquals con otro tipo de objeto"
    );
  }

}
