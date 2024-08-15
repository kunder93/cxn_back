
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm.SignUpRequestFormBuilder;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class SignUpRequestTest {

  /**
   * A valid DNI (National Identification Number) used for testing.
   * Format: 8 digits followed by a letter.
   */
  private static final String VALID_DNI = "12345678A";

  /**
   * A valid name for testing purposes.
   * Represents a common given name.
   */
  private static final String VALID_NAME = "John";

  /**
   * A valid first surname (family name) for testing.
   * Represents a common surname.
   */
  private static final String VALID_FIRST_SURNAME = "Doe";

  /**
   * A valid second surname (family name) for testing.
   * Represents an additional surname.
   */
  private static final String VALID_SECOND_SURNAME = "Smith";

  /**
   * A valid birth date for testing.
   * Format: yyyy-MM-dd.
   */
  private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(1990, 1, 1);

  /**
   * A valid gender value for testing.
   * Represents a common gender specification.
   */
  private static final String VALID_GENDER = "Male";

  /**
   * A valid password for testing purposes.
   * Represents a typical password string.
   */
  private static final String VALID_PASSWORD = "password123";

  /**
   * A valid email address for testing purposes.
   * Represents a standard email format.
   */
  private static final String VALID_EMAIL = "john.doe@example.com";

  /**
   * A valid postal code for testing.
   * Represents a standard postal code format.
   */
  private static final String VALID_POSTAL_CODE = "12345";

  /**
   * A valid apartment number for testing purposes.
   * Represents a typical apartment unit identifier.
   */
  private static final String VALID_APARTMENT_NUMBER = "1A";

  /**
   * A valid building name for testing purposes.
   * Represents a typical building identifier.
   */
  private static final String VALID_BUILDING = "Building 1";

  /**
   * A valid street name for testing purposes.
   * Represents a common street name.
   */
  private static final String VALID_STREET = "Main Street";

  /**
   * A valid city name for testing purposes.
   * Represents a common city name.
   */
  private static final String VALID_CITY = "City";

  /**
   * A valid kind of member type for testing.
   * Represents a specific user type.
   */
  private static final UserType VALID_KIND_MEMBER = UserType.SOCIO_NUMERO;

  /**
   * A valid country numeric code for testing purposes.
   * Represents a standard country numeric identifier.
   */
  private static final int VALID_COUNTRY_NUMERIC_CODE = 1;

  /**
   * A valid country subdivision name for testing purposes.
   * Represents a standard administrative region or subdivision.
   */
  private static final String VALID_COUNTRY_SUBDIVISION_NAME = "Subdivision";

  /**
   *  Instancia estática del constructor de SignUpRequestForm.
   *  Utilizado para construir objetos de tipo SignUpRequestForm en las
   *  pruebas unitarias.
   */
  private static SignUpRequestFormBuilder signUpRequestFormBuilder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    signUpRequestFormBuilder = SignUpRequestForm.builder().dni(VALID_DNI)
          .name(VALID_NAME).firstSurname(VALID_FIRST_SURNAME)
          .secondSurname(VALID_SECOND_SURNAME).birthDate(VALID_BIRTH_DATE)
          .gender(VALID_GENDER).password(VALID_PASSWORD).email(VALID_EMAIL)
          .postalCode(VALID_POSTAL_CODE).apartmentNumber(VALID_APARTMENT_NUMBER)
          .building(VALID_BUILDING).street(VALID_STREET).city(VALID_CITY)
          .kindMember(VALID_KIND_MEMBER)
          .countryNumericCode(VALID_COUNTRY_NUMERIC_CODE)
          .countrySubdivisionName(VALID_COUNTRY_SUBDIVISION_NAME);

  }

  @Test
  void testBuilderAndGetters() {
    // Crear una instancia utilizando el constructor generado por Lombok
    var form = signUpRequestFormBuilder.build();

    // Verificar que los valores se hayan establecido correctamente
    assertEquals(VALID_DNI, form.getDni(), "dni getter.");
    assertEquals(VALID_NAME, form.getName(), "name getter.");
    assertEquals(
          VALID_FIRST_SURNAME, form.getFirstSurname(), "first surname getter."
    );
    assertEquals(
          VALID_SECOND_SURNAME, form.getSecondSurname(),
          "second surname getter."
    );
    assertEquals(VALID_BIRTH_DATE, form.getBirthDate(), "birth date getter.");
    assertEquals(VALID_GENDER, form.getGender(), "gender getter.");
    assertEquals(VALID_PASSWORD, form.getPassword(), "password getter.");
    assertEquals(VALID_EMAIL, form.getEmail(), "email getter.");
    assertEquals(VALID_POSTAL_CODE, form.getPostalCode(), "postal code getter");
    assertEquals(
          VALID_APARTMENT_NUMBER, form.getApartmentNumber(),
          "Apartment number getter"
    );
    assertEquals(VALID_BUILDING, form.getBuilding(), "building getter");
    assertEquals(VALID_STREET, form.getStreet(), "street getter");
    assertEquals(VALID_CITY, form.getCity(), "city getter");
    assertEquals(
          VALID_KIND_MEMBER, form.getKindMember(), "kind of member getter"
    );
    assertEquals(
          VALID_COUNTRY_NUMERIC_CODE, form.getCountryNumericCode(),
          "country numeric code getter"
    );
    assertEquals(
          VALID_COUNTRY_SUBDIVISION_NAME, form.getCountrySubdivisionName(),
          "country subdivision name getter"
    );
  }

  @Test
  void testBuilderAndSetters() {
    // Crear una instancia utilizando el constructor generado por Lombok
    var form = signUpRequestFormBuilder.build();

    // Definir constantes para los valores de prueba en camelCase
    final var expectedDni = "87654321B";
    final var expectedName = "Jane";
    final var expectedFirstSurname = "Smith";
    final var expectedSecondSurname = "Johnson";
    final var expectedBirthDate = LocalDate.of(1995, 5, 5);
    final var expectedGender = "Female";
    final var expectedPassword = "newpassword456";
    final var expectedEmail = "jane.smith@example.com";
    final var expectedPostalCode = "54321";
    final var expectedApartmentNumber = "2B";
    final var expectedBuilding = "Building 2";
    final var expectedStreet = "Second Street";
    final var expectedCity = "New City";
    final var expectedKindMember = UserType.SOCIO_HONORARIO;
    final var expectedCountryNumericCode = 2;
    final var expectedCountrySubdivisionName = "New Subdivision";

    // Establecer y verificar los valores de prueba
    form.setDni(expectedDni);
    assertEquals(expectedDni, form.getDni(), "verifica setter");

    form.setName(expectedName);
    assertEquals(expectedName, form.getName(), "verifica setter");

    form.setFirstSurname(expectedFirstSurname);
    assertEquals(
          expectedFirstSurname, form.getFirstSurname(), "verifica setter"
    );

    form.setSecondSurname(expectedSecondSurname);
    assertEquals(
          expectedSecondSurname, form.getSecondSurname(), "verifica setter"
    );

    form.setBirthDate(expectedBirthDate);
    assertEquals(expectedBirthDate, form.getBirthDate(), "verifica setter");

    form.setGender(expectedGender);
    assertEquals(expectedGender, form.getGender(), "verifica setter");

    form.setPassword(expectedPassword);
    assertEquals(expectedPassword, form.getPassword(), "verifica setter");

    form.setEmail(expectedEmail);
    assertEquals(expectedEmail, form.getEmail(), "verifica setter");

    form.setPostalCode(expectedPostalCode);
    assertEquals(expectedPostalCode, form.getPostalCode(), "verifica setter");

    form.setApartmentNumber(expectedApartmentNumber);
    assertEquals(
          expectedApartmentNumber, form.getApartmentNumber(), "verifica setter"
    );

    form.setBuilding(expectedBuilding);
    assertEquals(expectedBuilding, form.getBuilding(), "verifica setter");

    form.setStreet(expectedStreet);
    assertEquals(expectedStreet, form.getStreet(), "verifica setter");

    form.setCity(expectedCity);
    assertEquals(expectedCity, form.getCity(), "verifica setter");

    form.setKindMember(expectedKindMember);
    assertEquals(expectedKindMember, form.getKindMember(), "verifica setter");

    form.setCountryNumericCode(expectedCountryNumericCode);
    assertEquals(
          expectedCountryNumericCode, form.getCountryNumericCode(),
          "verifica setter"
    );

    form.setCountrySubdivisionName(expectedCountrySubdivisionName);
    assertEquals(
          expectedCountrySubdivisionName, form.getCountrySubdivisionName(),
          "verifica setter"
    );

    // Verificar que la representación en cadena del objeto no sea nula
    assertNotNull(form.toString(), "verifica no nulo");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias con los mismos valores
    var form1 = SignUpRequestForm.builder().dni("12345678A").name("John")
          .firstSurname("Doe").build();

    var form2 = SignUpRequestForm.builder().dni("12345678A").name("John")
          .firstSurname("Doe").build();

    //
    assertEquals(
          form1, form2,
          "Verificar que las instancias sean iguales según el método equals"
    );
    assertEquals(
          form2, form1,
          "Verificar que las instancias sean iguales según el método equals"
    );

    assertEquals(
          form1.hashCode(), form2.hashCode(),
          "Verificar que los hashCode de las instancias sean iguales"
    );

    form2.setName("Jane");
    assertNotEquals(
          form1, form2,
          "Cambiar un valor en una instancia y verificar que los "
                + "equals no sean iguales"
    );

    form2.setName("John");
    form2.setDni("87654321B");
    assertNotEquals(
          form1.hashCode(), form2.hashCode(),
          "Cambiar un valor en una instancia y verificar que los "
                + "hashCode no sean iguales"
    );
  }

  @Test
  void testDniValidation() {
    // Create valid signUpRequestForm
    var signUpRequestForm = signUpRequestFormBuilder.build();
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();

    // Caso 1: Los datos son validos:
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequestForm);
    assertTrue(violations.isEmpty(), "No constraint violations should occur");

    // Caso 1: Cadena nula
    signUpRequestForm.setDni(null);
    // Validar y verificar la excepción
    violations = validator.validate(signUpRequestForm);
    var violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI should not be null."
    );

    // Caso 3: DNI con longitud incorrecta (menor que 9)
    signUpRequestForm.setDni("1234567Z");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 4: DNI con longitud incorrecta (mayor que 9)
    signUpRequestForm.setDni("123456789Z0");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 5: DNI con formato incorrecto (sin letra al final)
    signUpRequestForm.setDni("123456789");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 6: DNI con formato incorrecto (letra incorrecta al final)
    signUpRequestForm.setDni("12345678A");
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 7: DNI con formato correcto (valor mínimo numérico)
    signUpRequestForm.setDni("00000000Z");
    violations = validator.validate(signUpRequestForm);
    assertTrue(violations.isEmpty(), "No constraint violations should occur");

    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 8: DNI con formato correcto (valor máximo numérico)
    signUpRequestForm.setDni("99999999Z");
    violations = validator.validate(signUpRequestForm);
    assertTrue(violations.isEmpty(), "No constraint violations should occur");

    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 9: DNI con letra 'ñ' al final
    signUpRequestForm.setDni("12345678Ñ");
    assertEquals(
          Constants.DNI_BAD_FORMAT, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
  }

  @Test
  void testNameValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 1: Los datos son válidos
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 2: Cadena nula
    signUpRequestForm.setName(null);
    violations = validator.validate(signUpRequestForm);
    var violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK, violation.getMessage(),
          "Name should not be null."
    );

    signUpRequestForm.setName("");
    // Caso 3: Nombre vacío
    signUpRequestForm.setName("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1 violation");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK, violation.getMessage(),
          "Name must not be blank"
    );

    // Caso 4: Nombre con un solo espacio en blanco
    signUpRequestForm.setName(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK, violation.getMessage(),
          "Name must not be blank"
    );

    // Caso 5: Nombre con multiples espacios en blanco
    signUpRequestForm.setName("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK, violation.getMessage(),
          "Name must not be blank"
    );

    // Caso 6: Nombre con caracteres en blanco al inicio y al final
    signUpRequestForm.setName("  John  ");
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 7: Nombre con longitud igual a NAME_MAX_LENGTH
    signUpRequestForm.setName("a".repeat(Constants.NAME_MAX_LENGTH));
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 8: Nombre con longitud mayor que NAME_MAX_LENGTH
    signUpRequestForm.setName("a".repeat(Constants.NAME_MAX_LENGTH + 1));
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
          "Name length must be less than or equal to NAME_MAX_LENGTH"
    );
  }

  @Test
  void testFirstSurnameValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 1: Los datos son válidos
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 2: Cadena nula
    signUpRequestForm.setFirstSurname(null);
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    var violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(),
          "First surname should not be null."
    );
    // Caso 3: Primer apellido vacío
    signUpRequestForm.setFirstSurname("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(),
          "First surname should not be null."
    );

    // Caso 4: Primer apellido con un solo espacio en blanco
    signUpRequestForm.setFirstSurname(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(),
          "First surname should not be null."
    );
    // Caso 5: Primer apellido con múltiples espacios en blanco
    signUpRequestForm.setFirstSurname("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(),
          "First surname should not be null."
    );

    // Caso 6: Primer apellido con caracteres en blanco al inicio y al final
    signUpRequestForm.setFirstSurname("  Doe  ");
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 7: Primer apellido con longitud igual a FIRST_SURNAME_MAX_LENGTH
    signUpRequestForm
          .setFirstSurname("a".repeat(Constants.FIRST_SURNAME_MAX_LENGTH));
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 8: Primer apellido con longitud mayor que FIRST_SURNAME_MAX_LENGTH
    signUpRequestForm
          .setFirstSurname("a".repeat(Constants.FIRST_SURNAME_MAX_LENGTH + 1));
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
          "First surname length must be less than or equal to "
                + "FIRST_SURNAME_MAX_LENGTH"
    );
  }

  @Test
  void testSecondSurnameValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 1: Los datos son válidos
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 2: Cadena nula
    signUpRequestForm.setSecondSurname(null);
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    var violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(),
          "Second surname should not be null."
    );
    // Caso 3: Segundo apellido vacío
    signUpRequestForm.setSecondSurname("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(),
          "Second surname should not be null."
    );

    // Caso 4: Segundo apellido con un solo espacio en blanco
    signUpRequestForm.setSecondSurname(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(),
          "Second surname should not be null."
    );
    // Caso 5: Segundo apellido con múltiples espacios en blanco
    signUpRequestForm.setSecondSurname("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(),
          "Second surname should not be null."
    );

    // Caso 6: Segundo apellido con caracteres en blanco al inicio y al final
    signUpRequestForm.setSecondSurname("  Doe  ");
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 7: Segundo apellido con longitud igual a FIRST_SURNAME_MAX_LENGTH
    signUpRequestForm
          .setSecondSurname("a".repeat(Constants.SECOND_SURNAME_MAX_LENGTH));
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 8: Segundo apellido con longitud mayor que FIRST_SURNAME_MAX_LENGTH
    signUpRequestForm.setSecondSurname(
          "a".repeat(Constants.SECOND_SURNAME_MAX_LENGTH + 1)
    );
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
          "Second surname length must be less than or equal to "
                + "SECOND_SURNAME_MAX_LENGTH"
    );
  }

  @Test
  void testBirthDateValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 1: Los datos son válidos
    Set<ConstraintViolation<SignUpRequestForm>> violations =
          validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );

    // Caso 2: Fecha futura
    signUpRequestForm.setBirthDate(LocalDate.now().plusDays(1));
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1 violation");
    var violation = violations.iterator().next();
    assertEquals(
          Constants.BIRTH_DATE_PAST, violation.getMessage(),
          "Birth date must be in the past"
    );

    // Caso 3: Fecha presente
    signUpRequestForm.setBirthDate(LocalDate.now());
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1 violation");
    violation = violations.iterator().next();
    assertEquals(
          Constants.BIRTH_DATE_PAST, violation.getMessage(),
          "Birth date must be in the past"
    );

    // Caso 4: Fecha pasada
    signUpRequestForm.setBirthDate(LocalDate.now().minusYears(1));
    violations = validator.validate(signUpRequestForm);
    assertTrue(
          violations.isEmpty(),
          "No se deben producir violaciones de restricciones"
    );
  }

}
