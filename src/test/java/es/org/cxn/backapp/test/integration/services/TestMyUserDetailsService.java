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

package es.org.cxn.backapp.test.integration.services;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.service.MyUserDetailsService;
import es.org.cxn.backapp.service.UserService;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Unit tests for {@link DefaultUserService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz
 */
@SpringBootTest(
      classes = { MyUserDetailsService.class, UserEntityRepository.class,
          UserService.class, DefaultUserService.class }
)
final class TestMyUserDetailsService {

  @MockBean
  private UserEntityRepository userEntityRepository;

  @MockBean
  private RoleEntityRepository roleEntityRepository;

  LocalDate date = LocalDate.now();
  String userName = "fake user Name";
  String first_surname = "fake first surname";
  String second_surname = "fake second surname";
  String email = "email@test.es";
  String gender = "male";
  String dni = "32721859N";
  PersistentAddressEntity address = new PersistentAddressEntity();

  /**
   * Sets up the validator for the tests.
   */
  @BeforeEach
  public final void setUpValidator() {
  }

  /**
   * Default constructor.
   */
  public TestMyUserDetailsService() {
    super();
  }

  // /**
  // * Verifies that service load user by username(email) and check data
  // *
  // * @throws UsernameNotFoundException when user with provided email not
  // found
  // *
  // */
  // @DisplayName("Load user by userName (email)")
  // @Test
  // final void testLoadUserByUserNameCheckResult()
  // throws UsernameNotFoundException {
  //
  // var userEntity = new PersistentUserEntity(
  // dni, userName, first_surname, second_surname, date, gender,
  // "password", email, address
  // );
  // Optional<PersistentUserEntity> userOptional = Optional.of(userEntity);
  // Mockito.when(userEntityRepository.findByEmail(email))
  // .thenReturn(userOptional);
  // userDetailsService.loadUserByUsername(email);
  // Mockito.verify(userEntityRepository, times(1)).findByEmail(email);
  // }
  //
  // /**
  // * Verifies that service load user by username(email) when no exists throw
  // * exception
  // *
  // * @throws UsernameNotFoundException when user with provided email not
  // found
  // */
  // @DisplayName("Load user by userName (email) not exists throw exception")
  // @Test
  // final void testLoadUserByUserNameNotExists()
  // throws UsernameNotFoundException {
  //
  // Optional<PersistentUserEntity> userOptional = Optional.empty();
  // Mockito.when(userEntityRepository.findByEmail(email))
  // .thenReturn(userOptional);
  // Assertions.assertThrows(UsernameNotFoundException.class, () -> {
  // userDetailsService.loadUserByUsername(email);
  // }, "Assert Exception");
  // }

}
