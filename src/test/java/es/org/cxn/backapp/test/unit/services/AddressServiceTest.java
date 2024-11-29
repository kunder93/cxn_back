
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.AddressServiceException;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.service.impl.DefaultAddressService;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link DefaultAddressService} class.
 *
 * This test class verifies the behavior of the {@link DefaultAddressService}
 *  methods
 * such as retrieving all countries and getting a specific country by its
 * numeric code.
 *
 * @author Santiago Paz.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

  /**
   * Mock instance of {@link CountryEntityRepository} to simulate repository
   *  behavior
   * for testing purposes.
   */
  @Mock
  private CountryEntityRepository mockRepository;

  /**
   * Instance of {@link DefaultAddressService} with mocks injected.
   * This service is under test in this class.
   */
  @InjectMocks
  private DefaultAddressService addressService;

  /**
   * Country numeric code represents a country through a number.
   */
  private static final int COUNTRY_NUMERIC_CODE = 123;

  /**
   * Sets up the test environment before each test method.
   * Initializes mock objects and the service instance.
   */
  @BeforeEach
  void setUp() {
    addressService = new DefaultAddressService(mockRepository);
  }

  /**
   * Tests the behavior of {@link DefaultAddressService#getCountries()}.
   * <p>
   * This test verifies that the service correctly retrieves the list of
   * countries
   * from the repository.
   * </p>
   */
  @Test
  void testGetCountries() {
    // Arrange
    var country1 = new PersistentCountryEntity();
    var country2 = new PersistentCountryEntity();
    when(mockRepository.findAll())
          .thenReturn(Arrays.asList(country1, country2));

    // Act
    var countries = addressService.getCountries();

    // Assert
    assertNotNull(countries, "The list of countries should not be null");
    assertEquals(
          2, countries.size(),
          "The list of countries should contain two elements"
    );
    verify(mockRepository, times(1)).findAll();
  }

  /**
   * Tests the behavior of {@link DefaultAddressService#getCountry(Integer)}
   *  when the country is found in the repository.
   * <p>
   * This test verifies that the service correctly returns the country entity
   *  when it exists in the repository.
   * </p>
   */
  @Test
  void testGetCountryFound() throws AddressServiceException {
    // Arrange
    var expectedCountry = new PersistentCountryEntity();
    when(mockRepository.findById(COUNTRY_NUMERIC_CODE))
          .thenReturn(Optional.of(expectedCountry));

    // Act
    var country = addressService.getCountry(COUNTRY_NUMERIC_CODE);

    // Assert
    assertNotNull(country, "The country should not be null");
    assertEquals(
          expectedCountry, country,
          "The returned country should match the expected country"
    );
    verify(mockRepository, times(1)).findById(COUNTRY_NUMERIC_CODE);
  }

  /**
   * Tests the behavior of {@link DefaultAddressService#getCountry(Integer)}
   * when the country is not found in the repository.
   * <p>
   * This test verifies that the service throws an
   * {@link AddressServiceException}
   * when the country does not exist in the repository.
   * </p>
   */
  @Test
  void testGetCountryNotFound() {
    // Arrange
    when(mockRepository.findById(COUNTRY_NUMERIC_CODE))
          .thenReturn(Optional.empty());

    // Act & Assert
    var exception = assertThrows(
          AddressServiceException.class,
          () -> addressService.getCountry(COUNTRY_NUMERIC_CODE),
          "Expected AddressServiceException when country is not found"
    );
    assertTrue(
          exception.getMessage()
                .contains("Country with numeric code: 123 not found"),
          "Exception message should contain the correct country numeric code"
    );
    verify(mockRepository, times(1)).findById(COUNTRY_NUMERIC_CODE);
  }
}
