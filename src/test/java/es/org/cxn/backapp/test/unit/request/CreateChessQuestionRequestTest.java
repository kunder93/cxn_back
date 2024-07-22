
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequestForm;

import org.junit.jupiter.api.Test;

class CreateChessQuestionRequestTest {

  @Test
  void testGettersAndSetters() {
    // Given
    var email = "test@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";

    // When
    var form = new CreateChessQuestionRequestForm();
    form.setEmail(email);
    form.setCategory(category);
    form.setTopic(topic);
    form.setMessage(message);

    // Then
    assertEquals(email, form.getEmail(), "getter");
    assertEquals(category, form.getCategory(), "getter");
    assertEquals(topic, form.getTopic(), "getter");
    assertEquals(message, form.getMessage(), "getter");
  }

  @Test
  void testHashCode() {
    // Given
    var email = "test@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";

    var form1 =
          new CreateChessQuestionRequestForm(email, category, topic, message);
    var form2 =
          new CreateChessQuestionRequestForm(email, category, topic, message);

    // Then
    assertEquals(form1.hashCode(), form2.hashCode(), "hashCode is the same");
  }

  @Test
  void testEquals() {
    // Given
    var email = "test@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";

    var form1 =
          new CreateChessQuestionRequestForm(email, category, topic, message);
    var form2 =
          new CreateChessQuestionRequestForm(email, category, topic, message);

    // Then
    assertEquals(form1, form2, "forms are equals");
  }

  @Test
  void testHashCodeNotEqual() {
    // Given
    var email1 = "test1@example.com";
    var email2 = "test2@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";

    var form1 =
          new CreateChessQuestionRequestForm(email1, category, topic, message);
    var form2 =
          new CreateChessQuestionRequestForm(email2, category, topic, message);

    // Then
    assertNotEquals(
          form1.hashCode(), form2.hashCode(), "forms hashCode are not equals."
    );
  }

  @Test
  void testNotEquals() {
    // Given
    var email1 = "test1@example.com";
    var email2 = "test2@example.com";
    var category = "Test Category";
    var topic = "Test Topic";
    var message = "Test Message";

    var form1 =
          new CreateChessQuestionRequestForm(email1, category, topic, message);
    var form2 =
          new CreateChessQuestionRequestForm(email2, category, topic, message);

    // Then
    assertNotEquals(form1, form2, "forms are not equals.");
  }

}
