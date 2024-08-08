package es.org.cxn.backapp.test.utils;

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

public class LocalDateAdapter
      implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
  private static final DateTimeFormatter formatter =
        DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public JsonElement serialize(
        LocalDate date, Type typeOfSrc, JsonSerializationContext context
  ) {
    var formattedDate = date.format(formatter);
    return new JsonPrimitive(formattedDate);
  }

  @Override
  public LocalDate deserialize(
        JsonElement json, Type typeOfT, JsonDeserializationContext context
  ) throws JsonParseException {
    var dateString = json.getAsString();
    return LocalDate.parse(dateString, formatter);
  }
}