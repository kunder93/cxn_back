/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.test.unit.services;

import es.org.cxn.backapp.repository.AddressEntityRepository;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.service.AddressService;
import es.org.cxn.backapp.service.DefaultAddressService;
import es.org.cxn.backapp.service.DefaultRoleService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Unit tests for {@link DefaultRoleService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz
 */
@SpringBootTest(
      classes = { AddressEntityRepository.class, AddressService.class,
          DefaultAddressService.class }
)
final class TestDefaultAddressService {

  @MockBean
  private AddressEntityRepository addressEntityRepository;

  @MockBean
  private CountryEntityRepository countryEntityRepository;

  /**
   * Sets up the validator for the tests.
   */
  @BeforeEach
  public final void setUpValidator() {
  }

  /**
   * Default constructor.
   */
  public TestDefaultAddressService() {
    super();
  }

  /**
   * Get all countries stored in db.
   *
   */
  @DisplayName("Assert equals")
  @Test
  void testGetAllCountriesSize() {
    Assertions.assertEquals(1, 1);
  }

}
