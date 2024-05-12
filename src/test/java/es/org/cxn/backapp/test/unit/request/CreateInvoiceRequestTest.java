
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CreateInvoiceRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de CreateInvoiceRequestForm
    var invoiceRequestForm = new CreateInvoiceRequestForm();

    // Establecer valores usando setters
    invoiceRequestForm.setNumber(123);
    invoiceRequestForm.setSeries("A");
    invoiceRequestForm.setAdvancePaymentDate(LocalDate.of(2022, 5, 1));
    invoiceRequestForm.setExpeditionDate(LocalDate.of(2022, 5, 15));
    invoiceRequestForm.setTaxExempt(true);
    invoiceRequestForm.setSellerNif("123456789A");
    invoiceRequestForm.setBuyerNif("987654321B");

    // Verificar los valores usando getters
    assertEquals(123, invoiceRequestForm.getNumber(), "getter");
    assertEquals("A", invoiceRequestForm.getSeries(), "getter");
    assertEquals(
          LocalDate.of(2022, 5, 1), invoiceRequestForm.getAdvancePaymentDate(),
          "getter"
    );
    assertEquals(
          LocalDate.of(2022, 5, 15), invoiceRequestForm.getExpeditionDate(),
          "getter"
    );
    assertTrue(invoiceRequestForm.getTaxExempt(), "getter");
    assertEquals("123456789A", invoiceRequestForm.getSellerNif(), "getter");
    assertEquals("987654321B", invoiceRequestForm.getBuyerNif(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de CreateInvoiceRequestForm con los mismos valores
    var invoiceRequestForm1 = CreateInvoiceRequestForm.builder().number(123)
          .series("A").advancePaymentDate(LocalDate.of(2022, 5, 1))
          .expeditionDate(LocalDate.of(2022, 5, 15)).taxExempt(true)
          .sellerNif("123456789A").buyerNif("987654321B").build();

    var invoiceRequestForm2 = CreateInvoiceRequestForm.builder().number(123)
          .series("A").advancePaymentDate(LocalDate.of(2022, 5, 1))
          .expeditionDate(LocalDate.of(2022, 5, 15)).taxExempt(true)
          .sellerNif("123456789A").buyerNif("987654321B").build();

    assertEquals(
          invoiceRequestForm1, invoiceRequestForm2,
          "las dos instancias son iguales usando equals"
    );

    assertEquals(
          invoiceRequestForm1.hashCode(), invoiceRequestForm2.hashCode(),
          "el hashcode de las dos instancias es el mismo"
    );

    CreateInvoiceRequestForm nullRequest = null;
    assertNotEquals(invoiceRequestForm1, nullRequest, "not equals with null");

    var otherObject = " ";
    assertNotEquals(
          invoiceRequestForm1, otherObject, "not equals with other object"
    );

  }

  @Test
  void testBuilder() {
    // Crear una instancia usando el builder
    var invoiceRequestForm = CreateInvoiceRequestForm.builder().number(123)
          .series("A").advancePaymentDate(LocalDate.of(2022, 5, 1))
          .expeditionDate(LocalDate.of(2022, 5, 15)).taxExempt(true)
          .sellerNif("123456789A").buyerNif("987654321B").build();

    assertEquals(
          123, invoiceRequestForm.getNumber(),
          "valores establecidos por el builder"
    );
    assertEquals(
          "A", invoiceRequestForm.getSeries(),
          "valores establecidos por el builder"
    );
    assertEquals(
          LocalDate.of(2022, 5, 1), invoiceRequestForm.getAdvancePaymentDate(),
          "valores establecidos por el builder"
    );
    assertEquals(
          LocalDate.of(2022, 5, 15), invoiceRequestForm.getExpeditionDate(),
          "valores establecidos por el builder"
    );
    assertTrue(
          invoiceRequestForm.getTaxExempt(),
          "valores establecidos por el builder"
    );
    assertEquals(
          "123456789A", invoiceRequestForm.getSellerNif(),
          "valores establecidos por el builder"
    );
    assertEquals(
          "987654321B", invoiceRequestForm.getBuyerNif(),
          "valores establecidos por el builder"
    );
  }

}
