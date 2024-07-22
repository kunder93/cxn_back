
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PersistentUserEntityTest {

  @Mock
  private PersistentRoleEntity role;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    role = mock(PersistentRoleEntity.class);
  }

  @Test
  void testGettersAndSetters() {
    var entity = new PersistentUserEntity();
    entity.setDni("12345678A");
    entity.setName("John");
    entity.setFirstSurname("Doe");
    entity.setSecondSurname("Smith");
    entity.setBirthDate(LocalDate.of(1990, 5, 15));
    entity.setGender("Male");
    entity.setPassword("password123");
    entity.setEmail("john@example.com");
    entity.setKindMember(PersistentUserEntity.UserType.SOCIO_NUMERO);
    entity.setEnabled(true);

    assertEquals("12345678A", entity.getDni(), "DNI getter/setter");
    assertEquals("John", entity.getName(), "Name getter/setter");
    assertEquals(
          "Doe", entity.getFirstSurname(), "First surname getter/setter"
    );
    assertEquals(
          "Smith", entity.getSecondSurname(), "Second surname getter/setter"
    );
    assertEquals(
          LocalDate.of(1990, 5, 15), entity.getBirthDate(),
          "Birth date getter/setter"
    );
    assertEquals("Male", entity.getGender(), "Gender getter/setter");
    assertEquals("password123", entity.getPassword(), "Password getter/setter");
    assertEquals("john@example.com", entity.getEmail(), "Email getter/setter");
    assertEquals(
          PersistentUserEntity.UserType.SOCIO_NUMERO, entity.getKindMember(),
          "Kind member getter/setter"
    );
    assertTrue(entity.isEnabled(), "Enabled getter/setter");
  }

  @Test
  void testEqualsWithNullAndDifferentClass() {
    var entity1 = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();

    var entity2 = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();

    assertEquals(entity1, entity1, "An object should be equal to itself");

    assertEquals(
          entity1, entity2, "Two objects with the same values should be equal"
    );
    assertEquals(
          entity1.hashCode(), entity2.hashCode(),
          "Hash codes of equal objects should be equal"
    );

    PersistentUserEntity entity3 = null;

    var otherObject = "some string";
    assertNotEquals(entity1, entity3, "An object should not be equal to null");
    assertNotEquals(
          entity1, otherObject,
          "An object should not be equal to an object of a different class"
    );
  }

  @Test
  void testEqualsWithDifferentAttributes() {
    var entity1 = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();

    var entity2 = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("jane@example.com") // Cambiar el email
          .build();

    var entity3 = PersistentUserEntity.builder().dni("87654321B") // Cambiar el dni
          .name("John").firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();

    assertNotEquals(
          entity1, entity2,
          "Two objects with the same dni and different email should not be equal"
    );

    assertNotEquals(
          entity1, entity3,
          "Two objects with the same email and different dni should not be equal"
    );
  }

  @Test
  void testAddRole() {
    var user = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();
    assertTrue(user.addRole(role), "Añadir un nuevo rol correctamente");
    assertTrue(
          user.getRoles().contains(role),
          "Verifica que el rol se ha añadido correctamente"
    );

    assertFalse(
          user.addRole(role),
          "Intentar añadir un rol que ya está presente en la lista de roles del usuario"
    );

    // Intentar añadir un rol nulo
    assertThrows(
          NullPointerException.class, () -> user.addRole(null),
          "Attempt to add a null role"
    );
  }

  @Test
  void testRemoveRole() {
    var user = PersistentUserEntity.builder().dni("12345678A").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(1990, 5, 15)).gender("Male")
          .password("password123").email("john@example.com").build();

    // Añadir un rol para la prueba de eliminarlo
    user.addRole(role);

    assertTrue(
          user.removeRole(role), "Eliminar un rol existente correctamente"
    );
    assertFalse(
          user.getRoles().contains(role),
          "Verifica que el rol se ha eliminado correctamente"
    ); //

    assertFalse(
          user.removeRole(role),
          "Intentar eliminar un rol que no está presente en la lista de roles del usuario"
    );

    assertThrows(
          NullPointerException.class, () -> user.removeRole(null),
          "Attempt to remove a null role"
    );
  }

}
