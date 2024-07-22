
package es.org.cxn.backapp.tes.unit.validation;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link SignUpRequestValidation} bean validation.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz Perez
 */
final class TestSignUpRequestValidation {

  private Validator validator;

  private static final String validName = "Jacinto";
  private static final String validFirstSurname = "Anacletio";
  private static final String validSecondSurname = "Juartillo";
  private static final LocalDate validBirthDate = LocalDate.ofYearDay(1993, 2);
  private static final String validGender = "male";
  private static final String validPassword = "OneValidPassword";
  private static final String validEmail = "santi@santi.es";
  private static final String validDni = "32721859N";
  private static final String validPostalCode = "15570";
  private static final String validApartmentNumber = "4";
  private static final String validBuilding = "fed";
  private static final String validStreet = "Rua Marina Española";
  private static final String validCity = "Naron";
  private static final Integer validCountryNumericCode = Integer.valueOf(340);
  private static final String validCountrySubdivisionName = "Angunio";
  private static final UserType validKindMember = UserType.SOCIO_NUMERO;

  /**
   * Default constructor.
   */
  public TestSignUpRequestValidation() {
    super();
  }

  /**
   * Sets up the validator for the tests.
   */
  @BeforeEach
  public void setUpValidator() {
    final var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Verifies that DNI with 8 numbers and Letter (TRWAGMYFPDXBNJZSQVHLCKE) are valid.
   *
   * @param dniValue the DNI values for validate.
   */
  @DisplayName("Validate valid values for DNI.")
  @ParameterizedTest
  @ValueSource(
        strings = { "32721859N", "32721860J", "00000000T", "99999999R",
            "12345678A" }
  )
  void testValidDni(final String dniValue) {
    var signUpRequest = SignUpRequestForm.builder().dni(dniValue)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(), "No dni violations."
    );
  }

  /**
   * Verifies that DNI with more or less 8 numbers and Letter in other order generates validation error.
   *
   * @param dniValue the DNI values for validate.
   */
  @DisplayName("Validate not valid format values for DNI.")
  @ParameterizedTest
  @ValueSource(
        strings = { "3272189N", "332721860J", "T00000000", "99999R9999",
            "12A34567A8", "12345678", "123456789", "AAAAAAAAA" }
  )
  void testNotValidDni(final String dniValue) {
    var signUpRequest = SignUpRequestForm.builder().dni(dniValue)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One dni violation."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, constraintViolation.getMessage(),
          "The message is about dni format."
    );
    Assertions.assertEquals(
          signUpRequest.getDni(), constraintViolation.getInvalidValue(),
          "The message is about dni format."
    );
  }

  /**
  * Verifies validation null dni is not valid.
  */
  @DisplayName("Validate not valid null value for DNI.")
  @Test
  void testNullDniNotValid() {
    final String dniValue = null;
    var signUpRequest = SignUpRequestForm.builder().dni(dniValue)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, constraintViolation.getMessage(),
          "The message is about dni format."
    );
    Assertions.assertEquals(
          signUpRequest.getDni(), constraintViolation.getInvalidValue(),
          "The message is about dni format."
    );
  }

  /**
   * Verifies validation Not blank in Name field.
   *
   * @param name The name not valid value.
   */
  @DisplayName("Validate blank name values are not valid.")
  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void testNameBlankNotValid(final String name) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni).name(name)
          .firstSurname(validFirstSurname).secondSurname(validSecondSurname)
          .birthDate(validBirthDate).gender(validGender).password(validPassword)
          .email(validEmail).postalCode(validPostalCode)
          .apartmentNumber(validApartmentNumber).building(validBuilding)
          .street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One constraint violation."
    );
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One dni violation."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.NAME_NOT_BLANK_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about name not blank."
    );
    Assertions.assertEquals(
          signUpRequest.getName(), constraintViolation.getInvalidValue(),
          "The constraint message is about name."
    );
  }

  /**
  * Verifies validation null name is not valid.
  */
  @DisplayName("Validate not valid null value for DNI.")
  @Test
  void testNullNameNotValid() {
    final String nameValue = null;
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(nameValue).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.NAME_NOT_BLANK_MESSAGE, constraintViolation.getMessage(),
          "The message is about dni format."
    );
    Assertions.assertEquals(
          signUpRequest.getName(), constraintViolation.getInvalidValue(),
          "The message is about dni format."
    );
  }

  /**
   * Verifies validation Name field length.
   *
   * @param name The name not valid value.
   */
  @DisplayName("Validate name length must not be upper than 25.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*26*/ "12345678901234567890123456",
            /*27*/"12345678901234567890123456",
            /*with spaces*/"14  56789012345     012345",
            /*26 with special characters */"dsadsadadwd€€14@|#|4€#€¬~@" }
  )
  void testNameLengthNotValid(final String name) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni).name(name)
          .firstSurname(validFirstSurname).secondSurname(validSecondSurname)
          .birthDate(validBirthDate).gender(validGender).password(validPassword)
          .email(validEmail).postalCode(validPostalCode)
          .apartmentNumber(validApartmentNumber).building(validBuilding)
          .street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One constraint violation."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.NAME_MAX_LENGTH_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about name not blank."
    );
    Assertions.assertEquals(
          signUpRequest.getName(), constraintViolation.getInvalidValue(),
          "The constraint message is about name."
    );
  }

  /**
   * Verifies validation Name field length.
   *
   * @param name The name valid value.
   */
  @DisplayName("Validate name length must not be upper than 25.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*25*/ "1234678901234567890123456",
            /*24*/"12345678904567890123456", /*1*/"1",
            /*1 special character*/"€",
            /*25 with special characters */"dsdsadadwd€€14@|#|4€#€¬~@" }
  )
  void testNameValid(final String name) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni).name(name)
          .firstSurname(validFirstSurname).secondSurname(validSecondSurname)
          .birthDate(validBirthDate).gender(validGender).password(validPassword)
          .email(validEmail).postalCode(validPostalCode)
          .apartmentNumber(validApartmentNumber).building(validBuilding)
          .street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(), "No constraint violations."
    );
  }

  /**
  * Verifies validation null first surname is not valid.
  */
  @DisplayName("Validate not valid null value for first surname.")
  @Test
  void testNullFirstSurnameNotValid() {
    final String firstSurname = null;
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(firstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE,
          constraintViolation.getMessage(),
          "The message is about first surname."
    );
    Assertions.assertEquals(
          signUpRequest.getFirstSurname(),
          constraintViolation.getInvalidValue(),
          "The message is about first surname format."
    );
  }

  /**
   * Verifies validation Not blank in first surname field.
   *
   * @param firstSurname The name not valid value.
   */
  @DisplayName("Validate blank name values are not valid.")
  @ParameterizedTest
  @ValueSource(strings = { "", " ", "    " })
  void testFirstSurnameBlankNotValid(final String firstSurname) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(firstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();

    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One constraint violation."
    );

    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE,
          constraintViolation.getMessage(),
          "The constraint message is about first surname not blank."
    );
    Assertions.assertEquals(
          signUpRequest.getFirstSurname(),
          constraintViolation.getInvalidValue(),
          "The constraint message is about first surname."
    );
  }

  /**
   * Verifies validation first surname field length.
   *
   * @param firstSurname The firstSurname not valid value.
   */
  @DisplayName("Validate firstSurname length must not be upper than 25.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*26*/ "12345678901234567890123456",
            /*27*/"12345678901234567890123456",
            /*26 with spaces*/"14  56789012345     012345",
            /*26 with special characters */"dsadsadadwd€€14@|#|4€#€¬~@" }
  )
  void testFirstSurnameLengthNotValid(final String firstSurname) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(firstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(1), violations.size(), "One constraint violation."
    );
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE,
          constraintViolation.getMessage(),
          "The constraint message is about first surname length."
    );
    Assertions.assertEquals(
          signUpRequest.getFirstSurname(),
          constraintViolation.getInvalidValue(),
          "The constraint message is about first surname."
    );
  }

  /**
   * @return LocalDate cases for birth date field test.
   */
  private static Stream<LocalDate> datePastProvider() {
    return Stream.of(/* before today*/ LocalDate.now().minusDays(1),
          /*others*/ LocalDate.of(1993, 6, 15), LocalDate.of(1930, 3, 10)
    );
  }

  /**
  * Verifies validation birth must be past.
  */
  @DisplayName("Validate birthday date must be past.")
  @ParameterizedTest
  @MethodSource("datePastProvider")
  void testOnlyPastBirthDateAreValid(final LocalDate date) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(date).gender(validGender)
          .password(validPassword).email(validEmail).postalCode(validPostalCode)
          .apartmentNumber(validApartmentNumber).building(validBuilding)
          .street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions.assertEquals(
          Integer.valueOf(0), violations.size(), "No constraint violations."
    );
  }

  /**
   * @return LocalDate cases for birth date field test.
   */
  private static Stream<LocalDate> dateFutureProvider() {
    return Stream.of(/*after today*/ LocalDate.now().plusDays(1),
          /*others*/ LocalDate.now(), LocalDate.now().plusMonths(1)
    );
  }

  /**
  * Verifies validation birth must be past.
  */
  @DisplayName("Validate birthday date must be past.")
  @ParameterizedTest
  @MethodSource("dateFutureProvider")
  void testFutureBirthDateNotValid(final LocalDate date) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(date).gender(validGender)
          .password(validPassword).email(validEmail).postalCode(validPostalCode)
          .apartmentNumber(validApartmentNumber).building(validBuilding)
          .street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.BIRTH_DATE_PAST_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about birth date."
    );
    Assertions.assertEquals(
          signUpRequest.getBirthDate(), constraintViolation.getInvalidValue(),
          "The constraint message is about birth date."
    );

  }

  /**
  * Verifies validation password must not be blank.
  */
  @DisplayName("Validate password cannot be blank or spaces.")
  @ParameterizedTest
  @ValueSource(strings = { "", " ", "                 " })
  void testBlankPasswordNotValid(final String password) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(password).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);

    var violationsSizeMatch = violations.size() == 2 || violations.size() == 1;
    Assertions.assertTrue(violationsSizeMatch, "2 or 1 constraint violation");

    violations.forEach(constraintViolation -> {
      var match = constraintViolation.getMessage()
            .equals(Constants.PASSWORD_NOT_BLANK_MESSAGE)
            || constraintViolation.getMessage()
                  .equals(Constants.PASSWORD_SIZE_MESSAGE);
      Assertions.assertTrue(
            match, "Must be one of two constrant validation messages."
      );
      Assertions.assertEquals(
            signUpRequest.getPassword(), constraintViolation.getInvalidValue(),
            "The constraint message is about password."
      );

    });

  }

  /**
  * Verifies validation password must be between min(6) and max(20) length.
  */
  @DisplayName("Validate password no valid length.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*4*/"1234", /*22*/ "12345", /*21*/"123456789012345678901",
            /*22*/ "1234567890123456789012" }
  )
  void testPasswordLengthOutBoundsNotValid(final String password) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(password).email(validEmail)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.PASSWORD_SIZE_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about password."
    );
    Assertions.assertEquals(
          signUpRequest.getPassword(), constraintViolation.getInvalidValue(),
          "The constraint message is about password."
    );

  }

  /**
   * Verifies validation email cannot be more than 50 characters.
   */
  @DisplayName("Validate email no valid upper max length.")
  @ParameterizedTest
  @ValueSource(
        strings = { /*51*/"aaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com",
            /*52*/ "baaaaaaaaaaaaaverylongEmailExample@VeryLongEmail.com" }
  )
  void testEmailLengthOutBoundsNotValid(final String email) {
    var signUpRequest = SignUpRequestForm.builder().dni(validDni)
          .name(validName).firstSurname(validFirstSurname)
          .secondSurname(validSecondSurname).birthDate(validBirthDate)
          .gender(validGender).password(validPassword).email(email)
          .postalCode(validPostalCode).apartmentNumber(validApartmentNumber)
          .building(validBuilding).street(validStreet).city(validCity)
          .countryNumericCode(validCountryNumericCode)
          .countrySubdivisionName(validCountrySubdivisionName)
          .kindMember(validKindMember).build();
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequest);
    Assertions
          .assertEquals(Integer.valueOf(1), violations.size(), "One error.");
    var constraintViolation = violations.iterator().next();
    Assertions.assertEquals(
          Constants.MAX_SIZE_EMAIL_MESSAGE, constraintViolation.getMessage(),
          "The constraint message is about email."
    );
    Assertions.assertEquals(
          signUpRequest.getEmail(), constraintViolation.getInvalidValue(),
          "The constraint message is about email."
    );
  }

}
