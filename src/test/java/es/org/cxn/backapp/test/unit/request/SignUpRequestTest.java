
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class SignUpRequestTest {

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

  }

  @Test
  void testBuilderAndGettersSetters() {
    // Crear una instancia utilizando el constructor generado por Lombok
    var form = SignUpRequestForm.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision").build();

    // Verificar que los valores se hayan establecido correctamente
    assertEquals("12345678A", form.getDni(), "getter");
    assertEquals("John", form.getName(), "getter");
    assertEquals("Doe", form.getFirstSurname(), "getter");
    assertEquals("Smith", form.getSecondSurname(), "getter");
    assertEquals(LocalDate.of(1990, 1, 1), form.getBirthDate(), "getter");
    assertEquals("Male", form.getGender(), "getter");
    assertEquals("password123", form.getPassword(), "getter");
    assertEquals("john.doe@example.com", form.getEmail(), "getter");
    assertEquals("12345", form.getPostalCode(), "getter");
    assertEquals("1A", form.getApartmentNumber(), "getter");
    assertEquals("Building 1", form.getBuilding(), "getter");
    assertEquals("Main Street", form.getStreet(), "getter");
    assertEquals("City", form.getCity(), "getter");
    assertEquals(UserType.SOCIO_NUMERO, form.getKindMember(), "getter");
    assertEquals(1, form.getCountryNumericCode(), "getter");
    assertEquals("Subdivision", form.getCountrySubdivisionName(), "getter");

    form.setDni("87654321B");
    assertEquals("87654321B", form.getDni(), "verifica setter");

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
          "Cambiar un valor en una instancia y verificar que los equals no sean iguales"
    );

    form2.setName("John");
    form2.setDni("87654321B");
    assertNotEquals(
          form1.hashCode(), form2.hashCode(),
          "Cambiar un valor en una instancia y verificar que los hashCode no sean iguales"
    );
  }

  @Test
  void testDniValidation() {
    // Create valid signUpRequestForm
    var signUpRequestFormBuilder = SignUpRequestForm.builder().dni("12345678A")
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision");
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
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
          "DNI should not be null."
    );

    // Caso 3: DNI con longitud incorrecta (menor que 9)
    signUpRequestForm.setDni("1234567Z");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 4: DNI con longitud incorrecta (mayor que 9)
    signUpRequestForm.setDni("123456789Z0");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 5: DNI con formato incorrecto (sin letra al final)
    signUpRequestForm.setDni("123456789");
    violations = validator.validate(signUpRequestForm);
    violation = violations.iterator().next();
    assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
    // Regresamos a un estado valido
    signUpRequestForm = signUpRequestFormBuilder.build();

    // Caso 6: DNI con formato incorrecto (letra incorrecta al final)
    signUpRequestForm.setDni("12345678A");
    assertEquals(
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
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
          Constants.DNI_BAD_FORMAT_MESSAGE, violation.getMessage(),
          "DNI must contain 8 numeric and 1 letter. Length is 9."
    );
  }

  @Test
  void testNameValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestFormBuilder = SignUpRequestForm.builder().dni("12345678A")
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision");

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
          Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Name should not be null."
    );

    signUpRequestForm.setName("");
    // Caso 3: Nombre vacío
    signUpRequestForm.setName("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1 violation");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Name must not be blank"
    );

    // Caso 4: Nombre con un solo espacio en blanco
    signUpRequestForm.setName(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Name must not be blank"
    );

    // Caso 5: Nombre con multiples espacios en blanco
    signUpRequestForm.setName("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.NAME_NOT_BLANK_MESSAGE, violation.getMessage(),
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
    var signUpRequestFormBuilder = SignUpRequestForm.builder().dni("12345678A")
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision");

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
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "First surname should not be null."
    );
    // Caso 3: Primer apellido vacío
    signUpRequestForm.setFirstSurname("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "First surname should not be null."
    );

    // Caso 4: Primer apellido con un solo espacio en blanco
    signUpRequestForm.setFirstSurname(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "First surname should not be null."
    );
    // Caso 5: Primer apellido con múltiples espacios en blanco
    signUpRequestForm.setFirstSurname("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.FIRST_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
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
          "First surname length must be less than or equal to FIRST_SURNAME_MAX_LENGTH"
    );
  }

  @Test
  void testSecondSurnameValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestFormBuilder = SignUpRequestForm.builder().dni("12345678A")
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision");

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
          Constants.SECOND_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Second surname should not be null."
    );
    // Caso 3: Segundo apellido vacío
    signUpRequestForm.setSecondSurname("");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Second surname should not be null."
    );

    // Caso 4: Segundo apellido con un solo espacio en blanco
    signUpRequestForm.setSecondSurname(" ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
          "Second surname should not be null."
    );
    // Caso 5: Segundo apellido con múltiples espacios en blanco
    signUpRequestForm.setSecondSurname("       ");
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "violates only 1");
    violation = violations.iterator().next();
    assertEquals(
          Constants.SECOND_SURNAME_NOT_BLANK_MESSAGE, violation.getMessage(),
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
          "Second surname length must be less than or equal to SECOND_SURNAME_MAX_LENGTH"
    );
  }

  @Test
  void testBirthDateValidation() {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    // Create valid signUpRequestForm
    var signUpRequestFormBuilder = SignUpRequestForm.builder().dni("12345678A")
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 1, 1)).gender("Male")
          .password("password123").email("john.doe@example.com")
          .postalCode("12345").apartmentNumber("1A").building("Building 1")
          .street("Main Street").city("City").kindMember(UserType.SOCIO_NUMERO)
          .countryNumericCode(1).countrySubdivisionName("Subdivision");

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
          Constants.BIRTH_DATE_PAST_MESSAGE, violation.getMessage(),
          "Birth date must be in the past"
    );

    // Caso 3: Fecha presente
    signUpRequestForm.setBirthDate(LocalDate.now());
    violations = validator.validate(signUpRequestForm);
    assertEquals(1, violations.size(), "only 1 violation");
    violation = violations.iterator().next();
    assertEquals(
          Constants.BIRTH_DATE_PAST_MESSAGE, violation.getMessage(),
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
