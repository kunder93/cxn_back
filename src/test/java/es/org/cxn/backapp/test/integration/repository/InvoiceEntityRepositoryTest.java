
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

@SpringBootTest
class InvoiceEntityRepositoryTest {

  @Autowired
  private InvoiceEntityRepository invoiceRepository;

  @Autowired
  private CompanyEntityRepository companyRepository;

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
