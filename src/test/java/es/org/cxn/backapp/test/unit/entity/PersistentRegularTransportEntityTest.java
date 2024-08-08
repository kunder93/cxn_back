
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import org.junit.jupiter.api.Test;

class PersistentRegularTransportEntityTest {
  @Test
  void testGettersAndSetters() {
    // Crear un mock para PersistentInvoiceEntity y PersistentPaymentSheetEntity
    var invoice = mock(PersistentInvoiceEntity.class);
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    // Crear una instancia de PersistentRegularTransportEntity
    var regularTransport = new PersistentRegularTransportEntity();

    // Establecer valores utilizando los métodos setter
    regularTransport.setCategory("Example Category");
    regularTransport.setDescription("Example Description");
    regularTransport.setTransportInvoice(invoice);
    regularTransport.setPaymentSheet(paymentSheet);

    // Verificar que los valores se establecieron correctamente utilizando los métodos getter
    assertEquals("Example Category", regularTransport.getCategory(), "getter");
    assertEquals(
          "Example Description", regularTransport.getDescription(), "getter"
    );
    assertEquals(invoice, regularTransport.getTransportInvoice(), "getter");
    assertEquals(paymentSheet, regularTransport.getPaymentSheet(), "getter");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de PersistentRegularTransportEntity con los mismos atributos
    var regularTransport1 = new PersistentRegularTransportEntity();
    regularTransport1.setCategory("Example Category");
    regularTransport1.setDescription("Example Description");
    var invoice1 = mock(PersistentInvoiceEntity.class);
    regularTransport1.setTransportInvoice(invoice1);
    var paymentSheet1 = mock(PersistentPaymentSheetEntity.class);
    regularTransport1.setPaymentSheet(paymentSheet1);

    var regularTransport2 = new PersistentRegularTransportEntity();
    regularTransport2.setCategory("Example Category");
    regularTransport2.setDescription("Example Description");
    regularTransport2.setTransportInvoice(invoice1);
    regularTransport2.setPaymentSheet(paymentSheet1);

    assertEquals(
          regularTransport1, regularTransport1,
          "Verificar que es equals con si mismo."
    );

    assertEquals(
          regularTransport1, regularTransport2,
          "regularTransport1 y regularTransport2 son iguales según equals()"
    );
    assertEquals(
          regularTransport2, regularTransport1,
          "regularTransport1 y regularTransport2 son iguales según equals()"
    );

    assertEquals(
          regularTransport1.hashCode(), regularTransport2.hashCode(),
          "hash codes de regularTransport1 y regularTransport2 son iguales"
    );

    // Modificar un atributo en regularTransport2
    regularTransport2.setCategory("Different Category");

    assertNotEquals(
          regularTransport1, regularTransport2,
          "regularTransport1 y regularTransport2 ya no son iguales"
    );
    assertNotEquals(
          regularTransport2, regularTransport1,
          "regularTransport1 y regularTransport2 ya no son iguales"
    );

    assertNotEquals(
          regularTransport1.hashCode(), regularTransport2.hashCode(),
          "hash codes de regularTransport1 y regularTransport2 son diferentes después de modificar regularTransport2"
    );

    PersistentRegularTransportEntity nullEntity = null;

    assertNotEquals(
          regularTransport1, nullEntity, "Comprobar notEquals con un valor nulo"
    );
    assertNotEquals(
          nullEntity, regularTransport1, "Comprobar notEquals con un valor nulo"
    );
    // Comprobar notEquals con otro tipo de objeto
    var otherObject = "This is not a PersistentRegularTransportEntity";
    assertNotEquals(
          regularTransport1, otherObject, "no son equals, diferente objeto."
    );
  }
}
