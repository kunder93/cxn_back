
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.user.UserType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

class SignUpRequestTest {

    /**
     * A valid DNI (National Identification Number) used for testing. Format: 8
     * digits followed by a letter.
     */
    private static final String VALID_DNI = "12345678A";

    /**
     * A valid name for testing purposes. Represents a common given name.
     */
    private static final String VALID_NAME = "John";

    /**
     * A valid first surname (family name) for testing. Represents a common surname.
     */
    private static final String VALID_FIRST_SURNAME = "Doe";

    /**
     * A valid second surname (family name) for testing. Represents an additional
     * surname.
     */
    private static final String VALID_SECOND_SURNAME = "Smith";

    /**
     * A valid birth date for testing. Format: yyyy-MM-dd.
     */
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(1990, 1, 1);

    /**
     * A valid gender value for testing. Represents a common gender specification.
     */
    private static final String VALID_GENDER = "Male";

    /**
     * A valid password for testing purposes. Represents a typical password string.
     */
    private static final String VALID_PASSWORD = "password123";

    /**
     * A valid email address for testing purposes. Represents a standard email
     * format.
     */
    private static final String VALID_EMAIL = "john.doe@example.com";

    /**
     * A valid postal code for testing. Represents a standard postal code format.
     */
    private static final String VALID_POSTAL_CODE = "12345";

    /**
     * A valid apartment number for testing purposes. Represents a typical apartment
     * unit identifier.
     */
    private static final String VALID_APARTMENT_NUMBER = "1A";

    /**
     * A valid building name for testing purposes. Represents a typical building
     * identifier.
     */
    private static final String VALID_BUILDING = "Building 1";

    /**
     * A valid street name for testing purposes. Represents a common street name.
     */
    private static final String VALID_STREET = "Main Street";

    /**
     * A valid city name for testing purposes. Represents a common city name.
     */
    private static final String VALID_CITY = "City";

    /**
     * A valid kind of member type for testing. Represents a specific user type.
     */
    private static final UserType VALID_KIND_MEMBER = UserType.SOCIO_NUMERO;

    /**
     * A valid country numeric code for testing purposes. Represents a standard
     * country numeric identifier.
     */
    private static final int VALID_COUNTRY_NUMERIC_CODE = 1;

    /**
     * A valid country subdivision name for testing purposes. Represents a standard
     * administrative region or subdivision.
     */
    private static final String VALID_COUNTRY_SUBDIVISION_NAME = "Subdivision";

    /**
     * Sign up request with valid data.
     */
    private static final SignUpRequestForm SIGN_UP_REQUEST_FORM = new SignUpRequestForm(VALID_DNI, VALID_NAME,
            VALID_FIRST_SURNAME, VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL,
            VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
            VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testBirthDateValidation() {
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        // Create valid signUpRequestForm
        var signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        // Caso 1: Los datos son válidos
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 2: Fecha futura
        final var futureDate = LocalDate.now().plusDays(1);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                futureDate, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1 violation");
        var violation = violations.iterator().next();
        assertEquals(Constants.BIRTH_DATE_PAST, violation.getMessage(), "Birth date must be in the past");

        // Caso 3: Fecha presente
        final var todayDate = LocalDate.now();
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                todayDate, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1 violation");
        violation = violations.iterator().next();
        assertEquals(Constants.BIRTH_DATE_PAST, violation.getMessage(), "Birth date must be in the past");

        // Caso 4: Fecha pasada
        final var pastDate = LocalDate.now().minusYears(1);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                pastDate, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");
    }

    @Test
    void testBuilderAndGetters() {
        // Crear una instancia utilizando el constructor generado por Lombok
        var form = SIGN_UP_REQUEST_FORM;

        // Verificar que los valores se hayan establecido correctamente
        assertEquals(VALID_DNI, form.dni(), "dni getter.");
        assertEquals(VALID_NAME, form.name(), "name getter.");
        assertEquals(VALID_FIRST_SURNAME, form.firstSurname(), "first surname getter.");
        assertEquals(VALID_SECOND_SURNAME, form.secondSurname(), "second surname getter.");
        assertEquals(VALID_BIRTH_DATE, form.birthDate(), "birth date getter.");
        assertEquals(VALID_GENDER, form.gender(), "gender getter.");
        assertEquals(VALID_PASSWORD, form.password(), "password getter.");
        assertEquals(VALID_EMAIL, form.email(), "email getter.");
        assertEquals(VALID_POSTAL_CODE, form.postalCode(), "postal code getter");
        assertEquals(VALID_APARTMENT_NUMBER, form.apartmentNumber(), "Apartment number getter");
        assertEquals(VALID_BUILDING, form.building(), "building getter");
        assertEquals(VALID_STREET, form.street(), "street getter");
        assertEquals(VALID_CITY, form.city(), "city getter");
        assertEquals(VALID_KIND_MEMBER, form.kindMember(), "kind of member getter");
        assertEquals(VALID_COUNTRY_NUMERIC_CODE, form.countryNumericCode(), "country numeric code getter");
        assertEquals(VALID_COUNTRY_SUBDIVISION_NAME, form.countrySubdivisionName(), "country subdivision name getter");
    }

    @Test
    void testDniValidation() {
        // Create valid signUpRequestForm
        var signUpRequestForm = SIGN_UP_REQUEST_FORM;
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();

        // Caso 1: Los datos son validos:
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No constraint violations should occur");

        final String nullDni = null;
        // Caso 1: Cadena nula
        signUpRequestForm = new SignUpRequestForm(nullDni, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        // Validar y verificar la excepción
        violations = validator.validate(signUpRequestForm);
        var violation = violations.iterator().next();
        assertEquals(Constants.DNI_BAD_FORMAT, violation.getMessage(), "DNI should not be null.");

        // Caso 3: DNI con longitud incorrecta (menor que 9)
        final var shortDni = "1234567Z";
        signUpRequestForm = new SignUpRequestForm(shortDni, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        violation = violations.iterator().next();
        assertEquals(Constants.DNI_BAD_FORMAT, violation.getMessage(),
                "DNI must contain 8 numeric and 1 letter. Length is 9.");
        // Caso 4: DNI con longitud incorrecta (mayor que 9)
        final var longDni = "123456789Z0";
        signUpRequestForm = new SignUpRequestForm(longDni, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        violation = violations.iterator().next();
        assertEquals(Constants.DNI_BAD_FORMAT, violation.getMessage(),
                "DNI must contain 8 numeric and 1 letter. Length is 9.");

        // Caso 5: DNI con formato incorrecto (sin letra al final)

        final var dniWithoutLetter = "123456789";
        signUpRequestForm = new SignUpRequestForm(dniWithoutLetter, VALID_NAME, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        violation = violations.iterator().next();
        assertEquals(Constants.DNI_BAD_FORMAT, violation.getMessage(),
                "DNI must contain 8 numeric and 1 letter. Length is 9.");

        // Caso 6: DNI con formato correcto (valor mínimo numérico)
        final var minValidDni = "00000000Z";
        signUpRequestForm = new SignUpRequestForm(minValidDni, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No constraint violations should occur");

        // Caso 7: DNI con formato correcto (valor máximo numérico)
        final var maxValidDni = "99999999Z";
        signUpRequestForm = new SignUpRequestForm(maxValidDni, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No constraint violations should occur");

        // Caso 8: DNI con letra 'ñ' al final
        final var dniWithSpecialSpanishLetter = "12345678Ñ";
        signUpRequestForm = new SignUpRequestForm(dniWithSpecialSpanishLetter, VALID_NAME, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        violation = violations.iterator().next();
        assertEquals(Constants.DNI_BAD_FORMAT, violation.getMessage(),
                "DNI must contain 8 numeric and 1 letter. Length is 9.");
    }

    @Test
    void testEqualsAndHashCode() {
        // Crear dos instancias con los mismos valores
        var form1 = SIGN_UP_REQUEST_FORM;

        var form2 = SIGN_UP_REQUEST_FORM;

        //
        assertEquals(form1, form2, "Verificar que las instancias sean iguales según el método equals");
        assertEquals(form2, form1, "Verificar que las instancias sean iguales según el método equals");

        assertEquals(form1.hashCode(), form2.hashCode(), "Verificar que los hashCode de las instancias sean iguales");

    }

    @Test
    void testFirstSurnameValidation() {
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        // Create valid signUpRequestForm
        var signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        // Caso 1: Los datos son válidos
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 2: Cadena nula
        final String firstNameNull = null;
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstNameNull, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1");
        var violation = violations.iterator().next();
        assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(), "First surname should not be null.");
        // Caso 3: Primer apellido vacío
        final var firstSurnameEmpty = "";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameEmpty, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(), "First surname should not be null.");

        // Caso 4: Primer apellido con un solo espacio en blanco
        final var firstSurnameBlank = " ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameBlank, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(), "First surname should not be null.");
        // Caso 5: Primer apellido con múltiples espacios en blanco
        final var firstSurnameSeveralBlanks = "       ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameSeveralBlanks,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.FIRST_SURNAME_NOT_BLANK, violation.getMessage(), "First surname should not be null.");

        // Caso 6: Primer apellido con caracteres en blanco al inicio y al final
        final var firstSurnameBlanksStartEnd = "  Doe  ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameBlanksStartEnd,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 7: Primer apellido con longitud igual a FIRST_SURNAME_MAX_LENGTH
        final var firstSurnameMaxLength = "a".repeat(Constants.FIRST_SURNAME_MAX_LENGTH);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameMaxLength, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 8: Primer apellido con longitud mayor que FIRST_SURNAME_MAX_LENGTH
        final var firstSurnameGreaterMaxLength = "a".repeat(Constants.FIRST_SURNAME_MAX_LENGTH + 1);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, firstSurnameGreaterMaxLength,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
                "First surname length must be less than or equal to " + "FIRST_SURNAME_MAX_LENGTH");
    }

    @Test
    void testNameValidation() {
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        // Create valid signUpRequestForm
        var signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        // Caso 1: Los datos son válidos
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 2: Cadena nula
        final String nullName = null;
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nullName, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        var violation = violations.iterator().next();
        assertEquals(Constants.NAME_NOT_BLANK, violation.getMessage(), "Name should not be null.");

        // Caso 3: Nombre vacío
        final var emptyName = "";

        signUpRequestForm = new SignUpRequestForm(VALID_DNI, emptyName, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1 violation");
        violation = violations.iterator().next();
        assertEquals(Constants.NAME_NOT_BLANK, violation.getMessage(), "Name must not be blank");

        // Caso 4: Nombre con un solo espacio en blanco
        final var nameOnlyWhiteSpace = " ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nameOnlyWhiteSpace, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.NAME_NOT_BLANK, violation.getMessage(), "Name must not be blank");

        // Caso 5: Nombre con multiples espacios en blanco
        final var nameWithSeveralBlanks = "       ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nameWithSeveralBlanks, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.NAME_NOT_BLANK, violation.getMessage(), "Name must not be blank");

        // Caso 6: Nombre con caracteres en blanco al inicio y al final
        final var nameWithBlankStartEnd = "  John  ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nameWithBlankStartEnd, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 7: Nombre con longitud igual a NAME_MAX_LENGTH
        final var nameMaxLength = "a".repeat(Constants.NAME_MAX_LENGTH);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nameMaxLength, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 8: Nombre con longitud mayor que NAME_MAX_LENGTH
        final var nameGreaterMaxLength = "a".repeat(Constants.NAME_MAX_LENGTH + 1);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, nameGreaterMaxLength, VALID_FIRST_SURNAME,
                VALID_SECOND_SURNAME, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE,
                VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.NAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
                "Name length must be less than or equal to NAME_MAX_LENGTH");
    }

    @Test
    void testSecondSurnameValidation() {
        var factory = Validation.buildDefaultValidatorFactory();
        var validator = factory.getValidator();
        // Create valid signUpRequestForm
        var signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, VALID_SECOND_SURNAME,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        // Caso 1: Los datos son válidos
        Set<ConstraintViolation<SignUpRequestForm>> violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 2: Cadena nula
        final String secondSurnameNull = null;
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, secondSurnameNull,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "only 1");
        var violation = violations.iterator().next();
        assertEquals(Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(), "Second surname should not be null.");
        // Caso 3: Segundo apellido vacío
        final var secondSurnameEmpty = "";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, secondSurnameEmpty,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(), "Second surname should not be null.");

        // Caso 4: Segundo apellido con un solo espacio en blanco
        final var secondSurnameWhiteSpace = " ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, secondSurnameWhiteSpace,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(), "Second surname should not be null.");
        // Caso 5: Segundo apellido con múltiples espacios en blanco
        final var secondSurnameSeveralWhiteSpaces = "       ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME,
                secondSurnameSeveralWhiteSpaces, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL,
                VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.SECOND_SURNAME_NOT_BLANK, violation.getMessage(), "Second surname should not be null.");

        // Caso 6: Segundo apellido con caracteres en blanco al inicio y al final
        final var secondSurnameWhiteSpacesStartEnd = "  Doe  ";
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME,
                secondSurnameWhiteSpacesStartEnd, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL,
                VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);
        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 7: Segundo apellido con longitud igual a FIRST_SURNAME_MAX_LENGTH
        final var secondSurnameMaxLength = "a".repeat(Constants.SECOND_SURNAME_MAX_LENGTH);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME, secondSurnameMaxLength,
                VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL, VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER,
                VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER, VALID_COUNTRY_NUMERIC_CODE,
                VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertTrue(violations.isEmpty(), "No se deben producir violaciones de restricciones");

        // Caso 8: Segundo apellido con longitud mayor que FIRST_SURNAME_MAX_LENGTH
        final var secondSurnameGreaterMaxLength = "a".repeat(Constants.SECOND_SURNAME_MAX_LENGTH + 1);
        signUpRequestForm = new SignUpRequestForm(VALID_DNI, VALID_NAME, VALID_FIRST_SURNAME,
                secondSurnameGreaterMaxLength, VALID_BIRTH_DATE, VALID_GENDER, VALID_PASSWORD, VALID_EMAIL,
                VALID_POSTAL_CODE, VALID_APARTMENT_NUMBER, VALID_BUILDING, VALID_STREET, VALID_CITY, VALID_KIND_MEMBER,
                VALID_COUNTRY_NUMERIC_CODE, VALID_COUNTRY_SUBDIVISION_NAME);

        violations = validator.validate(signUpRequestForm);
        assertEquals(1, violations.size(), "violates only 1");
        violation = violations.iterator().next();
        assertEquals(Constants.SECOND_SURNAME_MAX_LENGTH_MESSAGE, violation.getMessage(),
                "Second surname length must be less than or equal to " + "SECOND_SURNAME_MAX_LENGTH");
    }

}
