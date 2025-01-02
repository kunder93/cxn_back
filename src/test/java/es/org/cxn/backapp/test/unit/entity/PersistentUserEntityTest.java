
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;

/**
 * Unit tests for the {@link PersistentUserEntity} class. This class tests the
 * getter and setter methods, equality comparison, hash code computation, role
 * addition and removal functionalities of the {@link PersistentUserEntity}
 * class.
 */
class PersistentUserEntityTest {

    /** Mock of {@link PersistentRoleEntity} used for role-related tests. */
    @Mock
    private PersistentRoleEntity role;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        role = mock(PersistentRoleEntity.class);
    }

    /**
     * Tests adding and removing roles from a {@link PersistentUserEntity}. Verifies
     * that a role can be added, checked for existence, and removed. Also checks
     * that attempting to add or remove null roles throws a
     * {@link NullPointerException}.
     */
    @Test
    void testAddRole() {
        // Define test data
        final var testDni = "12345678A";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(1990, 5, 15);
        final var testGender = "Male";
        final var testPassword = "password123";
        final var testEmail = "john@example.com";

        // Create and configure the user entity
        UserProfile userProfile = new UserProfile();
        userProfile.setName(testName);
        userProfile.setFirstSurname(testFirstSurname);
        userProfile.setSecondSurname(testSecondSurname);
        userProfile.setBirthDate(testBirthDate);
        userProfile.setGender(testGender);
        var user = PersistentUserEntity.builder().dni(testDni).profile(userProfile).password(testPassword)
                .email(testEmail).build();

        // Test role addition
        assertTrue(user.addRole(role), "Adding a new role should return true");
        assertTrue(user.getRoles().contains(role), "Role should be added successfully");

        // Test adding the same role again
        assertFalse(user.addRole(role), "Adding the same role again should return false");

        // Test adding a null role
        assertThrows(NullPointerException.class, () -> user.addRole(null),
                "Attempt to add a null role should throw NullPointerException");
    }

    /**
     * Tests the {@link PersistentUserEntity#equals(Object)} method with different
     * attribute values. Verifies that two objects with different attributes are not
     * considered equal.
     */
    @Test
    void testEqualsWithDifferentAttributes() {
        // Define test data
        final var testDni1 = "12345678A";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(1990, 5, 15);
        final var testGender = "Male";
        final var testPassword = "password123";
        final var testEmail1 = "john@example.com";
        final var testEmail2 = "jane@example.com";
        final var testDni2 = "87654321B";

        // Create and configure entities
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setName(testName);
        userProfile1.setFirstSurname(testFirstSurname);
        userProfile1.setSecondSurname(testSecondSurname);
        userProfile1.setBirthDate(testBirthDate);
        userProfile1.setGender(testGender);
        var entity1 = PersistentUserEntity.builder().dni(testDni1).profile(userProfile1).password(testPassword)
                .email(testEmail1).build();

        UserProfile userProfile2 = new UserProfile();
        userProfile2.setName(testName);
        userProfile2.setFirstSurname(testFirstSurname);
        userProfile2.setSecondSurname(testSecondSurname);
        userProfile2.setBirthDate(testBirthDate);
        userProfile2.setGender(testGender);

        var entity2 = PersistentUserEntity.builder().dni(testDni1).profile(userProfile2).password(testPassword)
                .email(testEmail2) // Different email
                .build();

        UserProfile userProfile3 = new UserProfile();
        userProfile3.setName(testName);
        userProfile3.setFirstSurname(testFirstSurname);
        userProfile3.setSecondSurname(testSecondSurname);
        userProfile3.setBirthDate(testBirthDate);
        userProfile3.setGender(testGender);

        var entity3 = PersistentUserEntity.builder().dni(testDni2) // Different DNI
                .profile(userProfile3).password(testPassword).email(testEmail1).build();

        // Verify inequality with different attributes
        assertNotEquals(entity1, entity2, "Two objects with the same DNI and different email should" + " not be equal");
        assertNotEquals(entity1, entity3, "Two objects with the same email and different DNI should" + " not be equal");
    }

    /**
     * Tests the {@link PersistentUserEntity#equals(Object)} and
     * {@link PersistentUserEntity#hashCode()} methods. Verifies that two instances
     * with the same values are considered equal and have the same hash code. Also
     * checks that an object is not equal to null or an object of a different class.
     */
    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Define test data
        final var testDni = "12345678A";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(1990, 5, 15);
        final var testGender = "Male";
        final var testPassword = "password123";
        final var testEmail = "john@example.com";

        // Create and configure entities
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setName(testName);
        userProfile1.setFirstSurname(testFirstSurname);
        userProfile1.setSecondSurname(testSecondSurname);
        userProfile1.setBirthDate(testBirthDate);
        userProfile1.setGender(testGender);
        var entity1 = PersistentUserEntity.builder().dni(testDni).profile(userProfile1).password(testPassword)
                .email(testEmail).build();

        UserProfile userProfile2 = new UserProfile();
        userProfile2.setName(testName);
        userProfile2.setFirstSurname(testFirstSurname);
        userProfile2.setSecondSurname(testSecondSurname);
        userProfile2.setBirthDate(testBirthDate);
        userProfile2.setGender(testGender);
        var entity2 = PersistentUserEntity.builder().dni(testDni).profile(userProfile2).password(testPassword)
                .email(testEmail).build();

        // Verify equality and hashCode
        assertEquals(entity1, entity1, "An object should be equal to itself");
        assertEquals(entity1, entity2, "Two objects with the same values should be equal");
        assertEquals(entity1.hashCode(), entity2.hashCode(), "Hash codes of equal objects should be equal");

        PersistentUserEntity entity3 = null;
        var otherObject = "some string";
        assertNotEquals(entity1, entity3, "An object should not be equal to null");
        assertNotEquals(entity1, otherObject, "An object should not be equal to an object of a different class");
    }

    /**
     * Tests getter and setter methods of the {@link PersistentUserEntity} class.
     * Verifies that values set using setters are correctly retrieved using getters.
     */
    @Test
    void testGettersAndSetters() {
        // Define test data
        final var testDni = "12345678A";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(1990, 5, 15);
        final var testGender = "Male";
        final var testPassword = "password123";
        final var testEmail = "john@example.com";
        final var testUserType = UserType.SOCIO_NUMERO;
        final var testEnabled = true;

        // Create and configure the entity
        var entity = new PersistentUserEntity();
        entity.setDni(testDni);
        UserProfile profile = new UserProfile();
        profile.setName(testName);
        profile.setFirstSurname(testFirstSurname);
        profile.setSecondSurname(testSecondSurname);
        profile.setBirthDate(testBirthDate);
        profile.setGender(testGender);
        entity.setProfile(profile);
        entity.setPassword(testPassword);
        entity.setEmail(testEmail);
        entity.setKindMember(testUserType);
        entity.setEnabled(testEnabled);

        // Verify getter methods
        assertEquals(testDni, entity.getDni(), "DNI getter/setter");
        assertEquals(testName, entity.getProfile().getName(), "Name getter/setter");
        assertEquals(testFirstSurname, entity.getProfile().getFirstSurname(), "First surname getter/setter");
        assertEquals(testSecondSurname, entity.getProfile().getSecondSurname(), "Second surname getter/setter");
        assertEquals(testBirthDate, entity.getProfile().getBirthDate(), "Birth date getter/setter");
        assertEquals(testGender, entity.getProfile().getGender(), "Gender getter/setter");
        assertEquals(testPassword, entity.getPassword(), "Password getter/setter");
        assertEquals(testEmail, entity.getEmail(), "Email getter/setter");
        assertEquals(testUserType, entity.getKindMember(), "Kind member getter/setter");
        assertTrue(entity.isEnabled(), "Enabled getter/setter");
    }

    /**
     * Tests removing roles from a {@link PersistentUserEntity}. Verifies that a
     * role can be removed, checked for non-existence, and that removing a role that
     * does not exist returns false. Also checks that attempting to remove a null
     * role throws a {@link NullPointerException}.
     */
    @Test
    void testRemoveRole() {
        // Define test data
        final var testDni = "12345678A";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(1990, 5, 15);
        final var testGender = "Male";
        final var testPassword = "password123";
        final var testEmail = "john@example.com";

        // Create and configure the user entity
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setName(testName);
        userProfile1.setFirstSurname(testFirstSurname);
        userProfile1.setSecondSurname(testSecondSurname);
        userProfile1.setBirthDate(testBirthDate);
        userProfile1.setGender(testGender);
        var user = PersistentUserEntity.builder().dni(testDni).profile(userProfile1).password(testPassword)
                .email(testEmail).build();

        // Add a role for testing removal
        user.addRole(role);

        // Test role removal
        assertTrue(user.removeRole(role), "Removing an existing role should return true");
        assertFalse(user.getRoles().contains(role), "Role should be removed successfully");

        // Test removing a non-existent role
        assertFalse(user.removeRole(role), "Removing a non-existent role should return false");

        // Test removing a null role
        assertThrows(NullPointerException.class, () -> user.removeRole(null),
                "Attempt to remove a null role should throw NullPointerException");
    }
}
