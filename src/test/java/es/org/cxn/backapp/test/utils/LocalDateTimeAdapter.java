
package es.org.cxn.backapp.test.utils;

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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A custom adapter for serializing and deserializing
 * {@link LocalDateTime} instances to and from JSON using the Gson library.
 * <p>
 * This adapter converts {@link LocalDateTime} objects to a string format
 * ("yyyy-MM-dd'T'HH:mm:ss") suitable for JSON representation and parses them
 * back into {@link LocalDateTime} objects. This format follows the ISO 8601
 * standard but excludes milliseconds.
 * </p>
 */
public final class LocalDateTimeAdapter implements
      JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  /**
   * The DateTimeFormatter used to serialize and deserialize
   * {@link LocalDateTime} instances
   * to and from JSON.
   * <p>
   * The pattern used is "yyyy-MM-dd'T'HH:mm:ss", which represents the date
   * and time in ISO 8601 format without milliseconds.
   * </p>
   */
  private static final DateTimeFormatter FORMATTING_PATTERN =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Override
  public JsonElement serialize(
        final LocalDateTime src, final Type typeOfSrc,
        final JsonSerializationContext context
  ) {
    return new JsonPrimitive(FORMATTING_PATTERN.format(src));
  }

  @Override
  public LocalDateTime deserialize(
        final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context
  ) throws JsonParseException {
    var dateString = json.getAsString();
    return LocalDateTime.parse(dateString, FORMATTING_PATTERN);
  }
}
