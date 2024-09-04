
package es.org.cxn.backapp.test.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.repository.CompanyEntityRepository;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for the {@link InvoiceEntityRepository}.
 * <p>
 * This class contains tests that verify the correct behavior of
 * the {@link InvoiceEntityRepository} in a Spring Boot application context.
 * </p>
 *
 * <p>
 * Tests are performed within a transactional context to ensure
 * database changes are rolled back after the test execution.
 * </p>
 *
 * <p>
 * Each test verifies the functionality of a specific method in the repository,
 * such as finding an invoice by its number and series.
 * </p>
 *
 * <p>
 * This test class uses JUnit 5 and Spring Boot's testing framework.
 * </p>
 *
 * @author Santi
 */
@SpringBootTest
class InvoiceEntityRepositoryTest {

  /**
   * Repository for performing CRUD operations on
   * {@link PersistentInvoiceEntity}.
   * <p>
   * This repository is auto-wired by Spring, and it is used in the tests
   * to save, retrieve, and verify {@link PersistentInvoiceEntity} instances.
   * </p>
   */
  @Autowired
  private InvoiceEntityRepository invoiceRepository;

  /**
   * Repository for performing CRUD operations on
   * {@link PersistentCompanyEntity}.
   * <p>
   * This repository is auto-wired by Spring, and it is used in the tests
   * to save {@link PersistentCompanyEntity} instances that are referenced
   * by {@link PersistentInvoiceEntity}.
   * </p>
   */
  @Autowired
  private CompanyEntityRepository companyRepository;

  /**
   * Tests the
   * {@link InvoiceEntityRepository#findByNumberAndSeries(int, String)} method.
   * <p>
   * This test verifies that an invoice can be successfully retrieved
   * from the database by its number and series after being saved.
   * </p>
   * <p>
   * The test performs the following steps:
   * </p>
   * <ul>
   *   <li>Saves a seller and a buyer {@link PersistentCompanyEntity}.</li>
   *   <li>Saves a {@link PersistentInvoiceEntity} with a specific number and
   *   series.</li>
   *   <li>Retrieves the saved invoice using the {@code findByNumberAndSeries}
   *   method.</li>
   *   <li>Asserts that the retrieved invoice is present and matches the saved
   *   invoice.</li>
   * </ul>
   */
  @Test
  @Transactional
  void testFindByNumberAndSeries() {
    // Given
    var seller = PersistentCompanyEntity.builder().address("address")
          .name("firstCompanyName").nif("32721859N").build();
    companyRepository.save(seller);
    var buyer = PersistentCompanyEntity.builder().address("address2")
          .name("secondCompanyName").nif("32721860J").build();
    companyRepository.save(buyer);

    var number = 1;
    var series = "FA"; // Match this with the saved invoice series
    var invoiceEntity = new PersistentInvoiceEntity(
          number, "FA", LocalDate.now(), LocalDate.now(), true, buyer, seller
    );

    var savedInvoice = invoiceRepository.save(invoiceEntity);

    // When
    var expectedInvoice =
          invoiceRepository.findByNumberAndSeries(number, series);

    // Then
    assertTrue(expectedInvoice.isPresent(), "Invoice should be present");
    assertEquals(
          expectedInvoice.get(), savedInvoice,
          "Found invoice should match expected invoice"
    );
  }
}
