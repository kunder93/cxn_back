
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CreatePaymentSheetRequestTest {

  @Test
  void testGettersAndSetters() {
    // Crear una instancia de CreatePaymentSheetRequestForm
    var paymentSheetRequestForm = new CreatePaymentSheetRequestForm();

    // Establecer valores usando setters
    paymentSheetRequestForm.setUserEmail("user@example.com");
    paymentSheetRequestForm.setReason("Event reason");
    paymentSheetRequestForm.setPlace("Event place");
    paymentSheetRequestForm.setStartDate(LocalDate.now());
    paymentSheetRequestForm.setEndDate(LocalDate.now().plusDays(7));

    assertEquals(
          "user@example.com", paymentSheetRequestForm.getUserEmail(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          "Event reason", paymentSheetRequestForm.getReason(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          "Event place", paymentSheetRequestForm.getPlace(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          LocalDate.now(), paymentSheetRequestForm.getStartDate(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          LocalDate.now().plusDays(7), paymentSheetRequestForm.getEndDate(),
          "Verifica los valores usando getters"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de CreatePaymentSheetRequestForm con los mismos valores
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          "user@example.com", "Event reason", "Event place", LocalDate.now(),
          LocalDate.now().plusDays(7)
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          "user@example.com", "Event reason", "Event place", LocalDate.now(),
          LocalDate.now().plusDays(7)
    );

    assertEquals(
          paymentSheetRequestForm1, paymentSheetRequestForm2,
          "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de CreatePaymentSheetRequestForm con diferentes valores
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          "user1@example.com", "Event reason 1", "Event place 1",
          LocalDate.now(), LocalDate.now().plusDays(7)
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          "user2@example.com", "Event reason 2", "Event place 2",
          LocalDate.now(), LocalDate.now().plusDays(7)
    );

    assertNotEquals(
          paymentSheetRequestForm1, paymentSheetRequestForm2,
          "las instancias no son iguales usando equals"
    );
    assertNotEquals(
          paymentSheetRequestForm2, paymentSheetRequestForm1,
          "las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de CreatePaymentSheetRequestForm con los mismos valores
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          "user@example.com", "Event reason", "Event place", LocalDate.now(),
          LocalDate.now().plusDays(7)
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          "user@example.com", "Event reason", "Event place", LocalDate.now(),
          LocalDate.now().plusDays(7)
    );

    assertEquals(
          paymentSheetRequestForm1.hashCode(),
          paymentSheetRequestForm2.hashCode(), "los hashCodes son iguales"
    );
  }

}
