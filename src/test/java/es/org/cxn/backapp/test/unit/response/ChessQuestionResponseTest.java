
package es.org.cxn.backapp.test.unit.response;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
