
package es.org.cxn.backapp.test.utils;

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
