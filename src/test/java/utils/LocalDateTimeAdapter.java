
package utils;

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

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,
      JsonDeserializer<LocalDateTime> {

  private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Override
  public JsonElement serialize(
        LocalDateTime src, Type typeOfSrc, JsonSerializationContext context
  ) {
    return new JsonPrimitive(formatter.format(src));
  }

  @Override
  public LocalDateTime deserialize(
        JsonElement json, Type typeOfT, JsonDeserializationContext context
  ) throws JsonParseException {
    var dateString = json.getAsString();
    return LocalDateTime.parse(dateString, formatter);
  }
}
