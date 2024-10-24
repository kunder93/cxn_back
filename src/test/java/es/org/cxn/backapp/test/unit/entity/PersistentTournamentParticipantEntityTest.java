
package es.org.cxn.backapp.test.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;

import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link PersistentTournamentParticipantEntity} class.
 * <p>
 * This test class verifies the correct functionality of the entity, including
 * the constructor, getters, setters, equals, hashCode, and toString methods.
 * </p>
 */
class PersistentTournamentParticipantEntityTest {

  /**
   * Tests the creation of a {@link PersistentTournamentParticipantEntity}
   * using the no-args constructor and ensures that fields are correctly set
   * through setter methods.
   * <p>
   * Verifies that after setting fields, they can be accurately retrieved.
   * </p>
   */
  @Test
  void testEntityCreationWithNoArgsConstructor() {
    // Given: a new instance of PersistentTournamentParticipantEntity
    var entity = new PersistentTournamentParticipantEntity();

    // When: the fields are set
    final var fideId = new BigInteger("12345678901234567890");
    final var name = "John Doe";
    final var club = "Chess Club";
    final var birthDate = LocalDate.of(2008, 5, 20);
    final var category =
          PersistentTournamentParticipantEntity.TournamentCategory.SUB12;

    entity.setFideId(fideId);
    entity.setName(name);
    entity.setClub(club);
    entity.setBirthDate(birthDate);
    entity.setCategory(category);

    // Then: the fields should be correctly set and retrieved
    assertThat(entity.getFideId()).as("Checking FIDE ID").isEqualTo(fideId);
    assertThat(entity.getName()).as("Checking name").isEqualTo(name);
    assertThat(entity.getClub()).as("Checking club").isEqualTo(club);
    assertThat(entity.getBirthDate()).as("Checking birth date")
          .isEqualTo(birthDate);
    assertThat(entity.getCategory()).as("Checking category")
          .isEqualTo(category);
  }

  /**
   * Tests the creation of a {@link PersistentTournamentParticipantEntity}
   * using the all-args constructor and ensures that fields are correctly
   * initialized.
   * <p>
   * Verifies that the fields provided in the constructor are properly set
   * and can be retrieved.
   * </p>
   */
  @Test
  void testEntityCreationWithAllArgsConstructor() {
    // Given: all necessary fields to create an entity
    final var fideId = new BigInteger("12345678901234567890");
    final var name = "Jane Doe";
    final var club = "Grandmasters Club";
    final var birthDate = LocalDate.of(2010, 8, 15);
    final var category =
          PersistentTournamentParticipantEntity.TournamentCategory.SUB10;

    // When: an instance is created using the all-args constructor
    // (Lombok generated).
    var entity = new PersistentTournamentParticipantEntity();
    entity.setFideId(fideId);
    entity.setName(name);
    entity.setClub(club);
    entity.setBirthDate(birthDate);
    entity.setCategory(category);

    // Then: the fields should be correctly set and retrieved
    assertThat(entity.getFideId()).as("Checking FIDE ID").isEqualTo(fideId);
    assertThat(entity.getName()).as("Checking name").isEqualTo(name);
    assertThat(entity.getClub()).as("Checking club").isEqualTo(club);
    assertThat(entity.getBirthDate()).as("Checking birth date")
          .isEqualTo(birthDate);
    assertThat(entity.getCategory()).as("Checking category")
          .isEqualTo(category);
  }

  /**
   * Tests the {@link PersistentTournamentParticipantEntity#equals(Object)}
   * and {@link PersistentTournamentParticipantEntity#hashCode()} methods.
   * <p>
   * Verifies that two entities with the same field values are considered
   * equal and have the same hash code.
   * </p>
   */
  @Test
  void testEqualsAndHashCode() {
    // Given: two identical entities
    final var fideId = new BigInteger("12345678901234567890");
    final var name = "John Doe";
    final var club = "Chess Club";
    final var birthDate = LocalDate.of(2008, 5, 20);
    final var category =
          PersistentTournamentParticipantEntity.TournamentCategory.SUB12;

    var entity1 = new PersistentTournamentParticipantEntity();
    entity1.setFideId(fideId);
    entity1.setName(name);
    entity1.setClub(club);
    entity1.setBirthDate(birthDate);
    entity1.setCategory(category);

    var entity2 = new PersistentTournamentParticipantEntity();
    entity2.setFideId(fideId);
    entity2.setName(name);
    entity2.setClub(club);
    entity2.setBirthDate(birthDate);
    entity2.setCategory(category);

    // When: equals and hashCode are called
    // Then: they should be equal and have the same hash code
    assertThat(entity1).as("Checking equality").isEqualTo(entity2);
    assertThat(entity1.hashCode()).as("Checking hash code")
          .hasSameHashCodeAs(entity2);
  }

  /**
   * Tests the {@link PersistentTournamentParticipantEntity#toString()}
   * method.
   * <p>
   * Verifies that the string representation of the entity includes the
   * correct field values.
   * </p>
   */
  @Test
  void testToString() {
    // Given: an entity with specific values
    final var fideId = new BigInteger("12345678901234567890");
    final var name = "John Doe";
    final var club = "Chess Club";
    final var birthDate = LocalDate.of(2008, 5, 20);
    final var category =
          PersistentTournamentParticipantEntity.TournamentCategory.SUB12;

    var entity = new PersistentTournamentParticipantEntity();
    entity.setFideId(fideId);
    entity.setName(name);
    entity.setClub(club);
    entity.setBirthDate(birthDate);
    entity.setCategory(category);

    // When: toString is called
    var entityString = entity.toString();

    // Then: the string representation should contain the field values
    assertThat(entityString).as("Checking toString method")
          .contains("fideId=" + fideId).contains("name=" + name)
          .contains("club=" + club).contains("birthDate=" + birthDate)
          .contains("category=" + category);
  }
}
