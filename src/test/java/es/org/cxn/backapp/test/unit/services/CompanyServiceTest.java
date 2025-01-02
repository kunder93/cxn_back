
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.repository.CompanyEntityRepository;
import es.org.cxn.backapp.service.exceptions.CompanyServiceException;
import es.org.cxn.backapp.service.impl.DefaultCompanyService;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link DefaultCompanyService} class, focusing on
 * the core functionalities related to handling companies.
 * <p>
 * This test class uses Mockito for mocking dependencies and JUnit
 * for asserting the expected behavior.
 * </p>
 *
 * <p>
 * The {@link CompanyServiceTest} class contains test methods that validate
 * the behavior of the service methods, including scenarios where exceptions
 * are thrown and proper handling of valid and invalid input data.
 * </p>
 *
 * <p>
 * The following test cases are covered:
 * <ul>
 *   <li>Adding a new company.</li>
 *   <li>Handling existing companies when attempting to add a new one.</li>
 *   <li>Finding a company by its NIF.</li>
 *   <li>Handling cases where a company is not found.</li>
 *   <li>Removing a company, including cases where the company has
 *   associated invoices.</li>
 *   <li>Updating an existing company's details.</li>
 * </ul>
 * </p>
 *
 * @author
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

  /**
   * Mocked instance of the {@link CompanyEntityRepository} used to simulate
   * database operations.
   */
  @Mock
  private CompanyEntityRepository companyRepository;

  /**
   * Injected instance of {@link DefaultCompanyService} where dependencies
   * are mocked.
   */
  @InjectMocks
  private DefaultCompanyService companyService;

  /**
   * Tests the {@link DefaultCompanyService#add(String, String, String)}
   * method to ensure a new company can be successfully added.
   *
   * @throws CompanyServiceException if there is an issue with adding
   * the company.
   */
  @Test
  void testAddCompany() throws CompanyServiceException {
    // Given
    var nif = "12345678X";
    var name = "Test Company";
    var address = "123 Test Street";
    when(companyRepository.existsByNif(nif)).thenReturn(false);
    var company = PersistentCompanyEntity.builder().nif(nif).name(name)
          .address(address).build();
    when(companyRepository.save(any(PersistentCompanyEntity.class)))
          .thenReturn(company);

    // When
    var result = companyService.add(nif, name, address);

    // Then
    assertNotNull(result, "The result should not be null");
    assertEquals(nif, result.getNif(), "The NIF should be '12345678X'");
    assertEquals(name, result.getName(), "The name should be 'Test Company'");
    assertEquals(
          address, result.getAddress(),
          "The address should be '123 Test Street'"
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#add(String, String, String)}
   * method to ensure that an exception is thrown when trying to add a company
   * that already exists.
   */
  @Test
  void testAddCompanyThrowsExceptionWhenCompanyExists() {
    // Given
    when(companyRepository.existsByNif(anyString())).thenReturn(true);

    // When & Then
    var thrownException = assertThrows(
          CompanyServiceException.class,
          () -> companyService
                .add("12345678X", "New Company", "456 New Street"),
          "Expected add() to throw, but it didn't"
    );

    // Optional: Add additional assertions to verify the exception message
    assertEquals(
          DefaultCompanyService.COMPANY_EXISTS, thrownException.getMessage(),
          "The exception message should indicate that the company "
                + "already exists"
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#findById(String)} method to
   * ensure that a company can be found by its NIF and the correct
   * data is returned.
   *
   * @throws CompanyServiceException if the company is not found.
   */
  @Test
  void testFindById() throws CompanyServiceException {
    // Given
    var company = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Test Company").address("123 Test Street").build();

    when(companyRepository.findByNif("12345678X"))
          .thenReturn(Optional.of(company));

    // When
    var result = companyService.findById("12345678X");

    // Then
    assertNotNull(
          result,
          "The result should not be null when the company exists in the"
                + " repository."
    );
    assertEquals(
          "12345678X", result.getNif(),
          "The NIF of the returned company should match the requested NIF."
    );
    assertEquals(
          "Test Company", result.getName(),
          "The name of the returned company should match the expected name."
    );
    assertEquals(
          "123 Test Street", result.getAddress(),
          "The address of the returned company should match the expected "
                + "address."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#findById(String)} method to
   * ensure that an exception is thrown when a company with the given NIF
   * does not exist.
   */
  @Test
  void testFindByIdThrowsExceptionWhenCompanyNotFound() {
    // Given
    when(companyRepository.findByNif(anyString())).thenReturn(Optional.empty());

    // When & Then
    var thrownException = assertThrows(
          CompanyServiceException.class,
          () -> companyService.findById("12345678X"),
          "Expected findById() to throw CompanyServiceException when company"
                + " is not found"
    );

    assertEquals(
          "Company not found.", thrownException.getMessage(),
          "Exception message should indicate that the company was not found."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#remove(String)} method to ensure
   * a company can be removed when it exists in the repository and has
   * no associated invoices.
   *
   * @throws CompanyServiceException if the company cannot be removed.
   */
  @Test
  void testRemove() throws CompanyServiceException {
    var company = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Test Company").address("123 Test Street").build();

    when(companyRepository.findByNif(anyString()))
          .thenReturn(Optional.of(company));

    companyService.remove("12345678X");

    verify(companyRepository, times(1)).delete(company);
  }

  /**
   * Tests the {@link DefaultCompanyService#remove(String)} method to ensure
   * that an exception is thrown when trying to remove a company that does
   * not exist.
   */
  @Test
  void testRemoveThrowsExceptionWhenCompanyNotFound() {
    // Given
    when(companyRepository.findByNif(anyString())).thenReturn(Optional.empty());

    // When & Then
    var thrownException = assertThrows(
          CompanyServiceException.class,
          () -> companyService.remove("12345678X"),
          "Expected remove() to throw CompanyServiceException when company"
                + " is not found"
    );

    assertEquals(
          "Company not found.", thrownException.getMessage(),
          "Exception message should indicate that the company was not found."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#remove(String)} method to ensure
   * that an exception is thrown when trying to remove a company that has
   * associated invoices.
   */
  @Test
  void testRemoveThrowsExceptionWhenCompanyHasInvoices() {
    // Given
    var company = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Test Company").address("123 Test Street").build();
    company.setInvoicesAsBuyer(
          Collections.singletonList(mock(PersistentInvoiceEntity.class))
    ); // assume invoices exist

    when(companyRepository.findByNif(anyString()))
          .thenReturn(Optional.of(company));

    // When & Then
    var thrownException = assertThrows(
          CompanyServiceException.class,
          () -> companyService.remove("12345678X"),
          "Expected remove() to throw CompanyServiceException when "
                + "company has invoices"
    );

    assertEquals(
          "Company can not be deleted.", thrownException.getMessage(),
          "Exception message should indicate that the company cannot be "
                + "deleted due to associated invoices."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#getCompanies()} method to
   * ensure that all companies can be retrieved successfully.
   */
  @Test
  void testGetCompanies() {
    // Given
    var company = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Test Company").address("123 Test Street").build();

    when(companyRepository.findAll())
          .thenReturn(Collections.singletonList(company));

    // When
    var result = companyService.getCompanies();

    // Then
    assertNotNull(result, "Result should not be null.");
    assertEquals(1, result.size(), "The size of the result list should be 1.");
    assertEquals(
          "12345678X", result.get(0).getNif(),
          "The NIF of the first company should be '12345678X'."
    );
    assertEquals(
          "Test Company", result.get(0).getName(),
          "The name of the first company should be 'Test Company'."
    );
    assertEquals(
          "123 Test Street", result.get(0).getAddress(),
          "The address of the first company should be '123 Test Street'."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#updateCompany
   * (String, String, String)}
   * method to ensure that an existing company can be updated with new
   * details successfully.
   *
   * @throws CompanyServiceException if there is an issue with updating
   * the company.
   */
  @Test
  void testUpdateCompany() throws CompanyServiceException {
    // Given
    var existingCompany = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Old Company").address("123 Old Street").build();
    var updatedCompany = PersistentCompanyEntity.builder().nif("12345678X")
          .name("Updated Company").address("123 Updated Street").build();

    when(companyRepository.findByNif(anyString()))
          .thenReturn(Optional.of(existingCompany));
    when(companyRepository.save(any(PersistentCompanyEntity.class)))
          .thenReturn(updatedCompany);

    // When
    var result = companyService
          .updateCompany("12345678X", "Updated Company", "123 Updated Street");

    // Then
    assertNotNull(
          result, "The result of the update operation should not be null."
    );
    assertEquals(
          "12345678X", result.getNif(),
          "The NIF of the updated company should be '12345678X'."
    );
    assertEquals(
          "Updated Company", result.getName(),
          "The name of the updated company should be 'Updated Company'."
    );
    assertEquals(
          "123 Updated Street", result.getAddress(),
          "The address of the updated company should be '123 Updated Street'."
    );
  }

  /**
   * Tests the {@link DefaultCompanyService#updateCompany
   * (String, String, String)}
   * method to ensure that an exception is thrown when trying to update a
   * company that does not exist.
   */
  @Test
  void testUpdateCompanyThrowsExceptionWhenCompanyNotFound() {
    // Given
    when(companyRepository.findByNif(anyString())).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
          CompanyServiceException.class,
          () -> companyService
                .updateCompany("12345678X", "New Company", "456 New Street"),
          "Expected updateCompany() to throw CompanyServiceException "
                + "when company is not found"
    );
  }
}
