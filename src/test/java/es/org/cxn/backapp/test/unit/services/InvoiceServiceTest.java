
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.InvoiceServiceException;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;
import es.org.cxn.backapp.service.DefaultInvoiceService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link DefaultInvoiceService}.
 *
 * This class contains tests to verify the behavior of the methods in
 * {@link DefaultInvoiceService} related to adding, removing,
 * and retrieving invoices.
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

  /**
   * Mocked repository for handling {@link PersistentInvoiceEntity} operations.
   *
   * This mock allows the unit test to simulate the behavior of the
   * {@link InvoiceEntityRepository} without requiring a real database.
   */
  @Mock
  private InvoiceEntityRepository invoiceRepository;

  /**
   * Service under test, which is injected with the mocked dependencies.
   *
   * The {@link DefaultInvoiceService} is the primary class being tested.
   * It relies on the {@link InvoiceEntityRepository} mock to perform
   * its operations in a controlled environment.
   */
  @InjectMocks
  private DefaultInvoiceService invoiceService;

  /**
   * Sample invoice entity used in the test cases.
   *
   * This instance represents an invoice that can be used to simulate
   * various scenarios in the test methods.
   */
  private PersistentInvoiceEntity invoice;

  /**
   * Sample seller company entity used in the test cases.
   *
   * This instance represents the seller in the invoice and is used to
   * test interactions between the {@link PersistentInvoiceEntity}
   * and the seller.
   */
  private PersistentCompanyEntity seller;

  /**
   * Sample buyer company entity used in the test cases.
   *
   * This instance represents the buyer in the invoice and is used to
   * test interactions between the {@link PersistentInvoiceEntity}
   * and the buyer.
   */
  private PersistentCompanyEntity buyer;

  /**
   * Sets up the test data before each test is executed.
   */
  @BeforeEach
  void setUp() {
    invoice = new PersistentInvoiceEntity();
    invoice.setNumber(1);
    invoice.setSeries("A");

    seller = new PersistentCompanyEntity();
    seller.setNif("12345678X");

    buyer = new PersistentCompanyEntity();
    buyer.setNif("87654321X");
  }

  /**
   * Tests that an invoice can be added successfully.
   *
   * @throws InvoiceServiceException if the invoice cannot be added
   */
  @Test
  void testAddInvoiceSuccess() throws InvoiceServiceException {
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.empty());
    // Return an empty Optional instead of an empty List
    when(invoiceRepository.save(any(PersistentInvoiceEntity.class)))
          .thenReturn(invoice);

    var result = invoiceService
          .add(1, "A", LocalDate.now(), LocalDate.now(), false, seller, buyer);

    assertNotNull(result, "Expected non-null invoice after adding");
    assertEquals(1, result.getNumber(), "Expected invoice number to be 1");
    assertEquals("A", result.getSeries(), "Expected invoice series to be 'A'");
  }

  /**
   * Tests that adding a duplicate invoice throws an exception.
   */
  @Test
  void testAddInvoiceThrowsExceptionWhenDuplicateExists() {
    // Mock the repository to return an Optional containing the invoice
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.of(invoice));

    // Attempt to add the invoice and expect an exception
    var thrown = assertThrows(
          InvoiceServiceException.class,
          () -> invoiceService.add(
                1, "A", LocalDate.now(), LocalDate.now(), false, seller, buyer
          ), "Expected add() to throw InvoiceServiceException, but it didn't"
    );

    // Verify the exception message
    assertEquals(
          DefaultInvoiceService.INVOICE_EXISTS_MESSAGE, thrown.getMessage(),
          "Expected exception message to be 'Invoice already exists.'"
    );
  }

  /**
   * Tests that an invoice can be removed successfully.
   */
  @Test
  void testRemoveInvoiceSuccess() {
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.of(invoice));

    assertDoesNotThrow(
          () -> invoiceService.remove("A", 1),
          "Expected remove() not to throw any exception"
    );

    verify(invoiceRepository, times(1)).delete(invoice);
  }

  /**
   * Tests that removing a non-existent invoice throws an exception.
   */
  @Test
  void testRemoveInvoiceThrowsExceptionWhenInvoiceNotFound() {
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.empty());

    var thrown = assertThrows(
          InvoiceServiceException.class, () -> invoiceService.remove("A", 1),
          "Expected remove() to throw InvoiceServiceException, but it didn't"
    );

    assertEquals(
          DefaultInvoiceService.INVOICE_NOT_FOUND_MESSAGE, thrown.getMessage(),
          "Expected exception message to be 'Invoice not found.'"
    );
  }

  /**
   * Tests that the service retrieves all invoices successfully.
   */
  @Test
  void testGetInvoices() {
    when(invoiceRepository.findAll())
          .thenReturn(Collections.singletonList(invoice));

    var result = invoiceService.getInvoices();

    assertNotNull(result, "Expected non-null invoice list");
    assertEquals(1, result.size(), "Expected invoice list size to be 1");
    assertEquals(
          1, result.get(0).getNumber(), "Expected invoice number to be 1"
    );
  }

  /**
   * Tests that an invoice can be found by its ID.
   *
   * @throws InvoiceServiceException if the invoice is not found
   */
  @Test
  void testFindByIdSuccess() throws InvoiceServiceException {
    when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));

    var result = invoiceService.findById(1);

    assertNotNull(result, "Expected non-null invoice");
    assertEquals(1, result.getNumber(), "Expected invoice number to be 1");
  }

  /**
   * Tests that finding an invoice by its ID throws an exception when not found.
   */
  @Test
  void testFindByIdThrowsExceptionWhenInvoiceNotFound() {
    when(invoiceRepository.findById(1)).thenReturn(Optional.empty());

    var thrown = assertThrows(
          InvoiceServiceException.class, () -> invoiceService.findById(1),
          "Expected findById() to throw InvoiceServiceException, but it didn't"
    );

    assertEquals(
          DefaultInvoiceService.INVOICE_NOT_FOUND_MESSAGE, thrown.getMessage(),
          "Expected exception message to be 'Invoice not found.'"
    );
  }

  /**
   * Tests that an invoice can be found by its series and number.
   *
   * @throws InvoiceServiceException if the invoice is not found
   */
  @Test
  void testFindBySeriesAndNumberSuccess() throws InvoiceServiceException {
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.of(invoice));

    var result = invoiceService.findBySeriesAndNumber("A", 1);

    assertNotNull(result, "Expected non-null invoice");
    assertEquals(1, result.getNumber(), "Expected invoice number to be 1");
    assertEquals("A", result.getSeries(), "Expected invoice series to be 'A'");
  }

  /**
   * Tests that finding an invoice by its series and number throws an exception
   *  when not found.
   */
  @Test
  void testFindBySeriesAndNumberThrowsExceptionWhenNotFound() {
    when(invoiceRepository.findByNumberAndSeries(1, "A"))
          .thenReturn(Optional.empty());

    var thrown = assertThrows(
          InvoiceServiceException.class,
          () -> invoiceService.findBySeriesAndNumber("A", 1),
          "Expected findBySeriesAndNumber() to throw InvoiceServiceException,"
                + " but it didn't"
    );

    assertEquals(
          DefaultInvoiceService.INVOICE_NOT_FOUND_MESSAGE, thrown.getMessage(),
          "Expected exception message to be 'Invoice not found.'"
    );
  }
}
