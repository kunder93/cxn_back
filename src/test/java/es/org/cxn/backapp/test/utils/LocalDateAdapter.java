
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A custom adapter for serializing and deserializing {@link LocalDate}
 * <p>
 * This adapter converts {@link LocalDate} objects to a string format
 * ("yyyy-MM-dd") suitable for JSON representation and parses them
 * back into {@link LocalDate} objects. The format used is the ISO-8601
 * standard representation of a date without a time component.
 * </p>
 */
public final class LocalDateAdapter
      implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  /**
   * The DateTimeFormatter used to serialize and deserialize {@link LocalDate}
   * instances to and from JSON.
   * <p>
   * This formatter uses the ISO_LOCAL_DATE pattern, which represents the date
   * in the format "yyyy-MM-dd".
   * </p>
   */
  private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ISO_LOCAL_DATE;

  /**
   * Serializes a {@link LocalDate} instance to its JSON representation.
   *
   * @param date the {@link LocalDate} instance to serialize
   * @param typeOfSrc the type of the source object
   * @param context the context of serialization
   * @return a {@link JsonElement} representing the serialized {@link LocalDate}
   */
  @Override
  public JsonElement serialize(
        final LocalDate date, final Type typeOfSrc,
        final JsonSerializationContext context
  ) {
    var formattedDate = date.format(FORMATTER);
    return new JsonPrimitive(formattedDate);
  }

  /**
   * Deserializes a {@link JsonElement} into a {@link LocalDate} instance.
   *
   * @param json the {@link JsonElement} to deserialize
   * @param typeOfT the type of the target object
   * @param context the context of deserialization
   * @return a {@link LocalDate} instance representing the deserialized date
   * @throws JsonParseException if the {@link JsonElement}
   *  is not a valid date format
   */
  @Override
  public LocalDate deserialize(
        final JsonElement json, final Type typeOfT,
        final JsonDeserializationContext context
  ) throws JsonParseException {
    var dateString = json.getAsString();
    return LocalDate.parse(dateString, FORMATTER);
  }
}
