
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PersistentChessQuestionEntityTest {

  @Test
  void testGettersAndSetters() {
    var entity = new PersistentChessQuestionEntity();
    entity.setIdentifier(1);
    entity.setTopic("Chess");
    entity.setMessage("How to checkmate?");
    entity.setCategory("Strategy");
    entity.setEmail("example@example.com");

    assertEquals(1, entity.getIdentifier(), "Generation identity");
    assertEquals("Chess", entity.getTopic(), "Question topic");
    assertEquals("How to checkmate?", entity.getMessage(), "Question message");
    assertEquals("Strategy", entity.getCategory(), "Question category");
    assertEquals("example@example.com", entity.getEmail(), "Question email");
  }

  @Test
  void testEqualsAndHashCode() {
    var entity1 = new PersistentChessQuestionEntity();
    entity1.setIdentifier(1);
    entity1.setTopic("Chess");
    entity1.setMessage("How to checkmate?");
    entity1.setCategory("Strategy");
    entity1.setEmail("example@example.com");

    var entity2 = new PersistentChessQuestionEntity();
    entity2.setIdentifier(1);
    entity2.setTopic("Chess");
    entity2.setMessage("How to checkmate?");
    entity2.setCategory("Strategy");
    entity2.setEmail("example@example.com");

    assertEquals(
          entity1, entity2,
          "equals() method should return true for equal objects"
    );
    assertEquals(
          entity1.hashCode(), entity2.hashCode(),
          "hashCode() should return same value for equal objects"
    );
  }

  @Test
  void testToString() {
    var dateNow = LocalDateTime.now();
    var entity = new PersistentChessQuestionEntity();
    entity.setIdentifier(1);
    entity.setTopic("Chess");
    entity.setMessage("How to checkmate?");
    entity.setCategory("Strategy");
    entity.setDate(dateNow);
    entity.setEmail("example@example.com");

    var expectedToString =
          "PersistentChessQuestionEntity(identifier=1, topic=Chess,"
                + " message=How to checkmate?, category=Strategy,"
                + " email=example@example.com, date=" + dateNow + ", seen=false"
                + ")";
    assertEquals(
          expectedToString, entity.toString(),
          "toString() method should return expected string representation"
    );
  }

}
