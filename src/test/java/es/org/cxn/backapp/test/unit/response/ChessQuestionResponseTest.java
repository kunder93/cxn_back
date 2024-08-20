
package es.org.cxn.backapp.test.unit.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ChessQuestionResponseTest {

  @Test
  void testGettersAndSetters() {
    // Given
    var id = 1;
    var email = "test@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";
    var date = LocalDateTime.now();
    var seen = false;

    // When
    var response = new ChessQuestionResponse(
          id, email, category, topic, message, date, seen
    );

    // Then
    assertEquals(email, response.email(), "getter");
    assertEquals(category, response.category(), "getter");
    assertEquals(topic, response.topic(), "getter");
    assertEquals(message, response.message(), "getter");
    assertEquals(date, response.date(), "getter");
  }

  @Test
  void testEquals() {
    // Given
    var now = LocalDateTime.now();
    var response1 = new ChessQuestionResponse(
          1, "test@example.com", "Category", "Topic", "Message", now, false
    );
    var response2 = new ChessQuestionResponse(
          1, "test@example.com", "Category", "Topic", "Message", now, false
    );
    var response3 = new ChessQuestionResponse(
          2, "another@example.com", "Category", "Topic", "Message", now, false
    );

    ChessQuestionResponse nullResponse = null;
    // Then
    assertEquals(response1, response2, "Reflexive and Symmetric");
    assertEquals(response2, response1, "Symmetric");
    assertNotEquals(response1, response3, "Consistency");
    assertNotEquals(nullResponse, response1, " Non-nullity");
  }

  @Test
  void testHashCode() {
    // Given
    var now = LocalDateTime.now();
    var response1 = new ChessQuestionResponse(
          1, "test@example.com", "Category", "Topic", "Message", now, false
    );
    var response2 = new ChessQuestionResponse(
          1, "test@example.com", "Category", "Topic", "Message", now, false
    );

    // Then
    assertEquals(
          response1.hashCode(), response2.hashCode(), "consistency with equals"
    );
  }
}
