
package es.org.cxn.backapp.test.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PersistentInvoiceEntityTest {

  /**
   * Tests the constructor of {@link PersistentInvoiceEntity} with valid
   * arguments.
   * <p>
   * Verifies that an instance is created correctly and that its fields are
   * initialized as expected.
   */
  @Test
  void testConstructorWithValidArguments() {
    // Given
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();

    // When
    final var invoice = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller, buyer
    );

    // Then
    assertThat(invoice).as("Invoice should be created and not null")
          .isNotNull();
    assertThat(invoice.getNumber())
          .as("Invoice number should be initialized correctly").isEqualTo(1);
    assertThat(invoice.getSeries())
          .as("Invoice series should be initialized correctly").isEqualTo("A");
    assertThat(invoice.getExpeditionDate())
          .as("Invoice expedition date should be initialized to current date")
          .isNotNull();
    assertThat(invoice.getAdvancePaymentDate())
          .as("Advance payment date should be null").isNull();
    assertThat(invoice.getTaxExempt()).as("Tax exempt should be true").isTrue();
    assertThat(invoice.getSeller()).as("Seller should be set correctly")
          .isEqualTo(seller);
    assertThat(invoice.getBuyer()).as("Buyer should be set correctly")
          .isEqualTo(buyer);
  }

  /**
   * Tests the constructor of {@link PersistentInvoiceEntity} with null
   * arguments.
   * <p>
   * Verifies that a {@link NullPointerException} is thrown when any required
   * field is null.
   */
  @Test
  void testConstructorWithNullArguments() {
    // Given
    final var number = 1;
    final var series = "A";
    final var expeditionDate = LocalDate.now();
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();

    // When & Then
    assertThrows(NullPointerException.class, () -> {
      new PersistentInvoiceEntity(
            number, series, expeditionDate, null, null, seller, buyer
      );
    }, "Expected constructor to throw NullPointerException for null taxExempt."
    );
  }

  /**
   * Tests the {@code equals} and {@code hashCode} methods of
   * {@link PersistentInvoiceEntity}.
   * <p>
   * Verifies that two invoices with the same attributes are equal and produce
   * the same hash code,
   * and that two invoices with different attributes are not equal and produce
   * different hash codes.
   */
  @Test
  void testEqualsAndHashCode() {
    // Given
    final var seller1 = new PersistentCompanyEntity();
    final var buyer1 = new PersistentCompanyEntity();
    final var seller2 = new PersistentCompanyEntity();
    final var buyer2 = new PersistentCompanyEntity();

    final var invoice1 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller1, buyer1
    );
    final var invoice2 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller1, buyer1
    );
    final var invoice3 = new PersistentInvoiceEntity(
          2, "B", LocalDate.now().minusDays(1), null, false, seller2, buyer2
    );

    // When & Then
    assertThat(invoice1).as("Invoice1 should be equal to Invoice2")
          .isEqualTo(invoice2)
          .as("Invoice1 and Invoice2 should have the same hashCode")
          .hasSameHashCodeAs(invoice2)
          .as("Invoice1 should not be equal to Invoice3").isNotEqualTo(invoice3)
          .as("Invoice1 and Invoice3 should have different hashCodes")
          .doesNotHaveSameHashCodeAs(invoice3);
  }

  /**
   * Tests the {@code equals} method of {@link PersistentInvoiceEntity} with
   * itself.
   * <p>
   * Verifies that an invoice is equal to itself.
   */
  @Test
  void testEqualsWithItself() {
    // Given
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();
    final var invoice = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller, buyer
    );

    // When & Then
    assertThat(invoice).as("Invoice should be equal to itself")
          .isEqualTo(invoice);
  }

  /**
   * Tests the {@code equals} method of {@link PersistentInvoiceEntity} with
   * an object of a different class.
   * <p>
   * Verifies that an invoice is not equal to an object of a different class.
   */
  @Test
  void testEqualsWithDifferentClass() {
    // Given
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();
    final var invoice = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller, buyer
    );

    // When & Then
    assertThat(invoice)
          .as("Invoice should not be equal to an object of a different class")
          .isNotEqualTo(new Object());
  }

  /**
   * Tests the {@code equals} method of {@link PersistentInvoiceEntity} with
   * a different number.
   * <p>
   * Verifies that two invoices with different numbers are not equal.
   */
  @Test
  void testEqualsWithDifferentNumber() {
    // Given
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();
    final var invoice1 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller, buyer
    );
    final var invoice2 = new PersistentInvoiceEntity(
          2, "A", LocalDate.now(), null, true, seller, buyer
    );

    // When & Then
    assertThat(invoice1).as(
          "Invoice1 should not be equal to Invoice2 with a different number"
    ).isNotEqualTo(invoice2);
  }

  /**
   * Tests the {@code equals} method of {@link PersistentInvoiceEntity} with
   * a different series.
   * <p>
   * Verifies that two invoices with different series are not equal.
   */
  @Test
  void testEqualsWithDifferentSeries() {
    // Given
    final var seller = new PersistentCompanyEntity();
    final var buyer = new PersistentCompanyEntity();
    final var invoice1 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller, buyer
    );
    final var invoice2 = new PersistentInvoiceEntity(
          1, "B", LocalDate.now(), null, true, seller, buyer
    );

    // When & Then
    assertThat(invoice1).as(
          "Invoice1 should not be equal to Invoice2 with a different series"
    ).isNotEqualTo(invoice2);
  }

  /**
   * Tests the {@code equals} method of {@link PersistentInvoiceEntity} with
   * a different seller.
   * <p>
   * Verifies that two invoices with different sellers are not equal.
   */
  @Test
  void testEqualsWithDifferentSeller() {
    // Given
    final var seller1 = new PersistentCompanyEntity();
    seller1.setNif("4234234D");
    final var seller2 = new PersistentCompanyEntity();
    seller2.setNif("4312423E");
    final var buyer = new PersistentCompanyEntity();
    final var invoice1 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller1, buyer
    );
    final var invoice2 = new PersistentInvoiceEntity(
          1, "A", LocalDate.now(), null, true, seller2, buyer
    );

    // When & Then
    assertThat(invoice1).as(
          "Invoice1 should not be equal to Invoice2 with a different seller"
    ).isNotEqualTo(invoice2);
  }

}
