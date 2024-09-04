
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequest;

import java.time.LocalDate;
import java.time.Month;

public final class PaymentSheetControllerFactory {

  /**
   * Payment sheet event description.
   */
  public static final String PAYMENT_SHEET_REASON = "MyPaymentSheet reason";
  /**
   * Payment sheet event place.
   */
  public static final String PAYMENT_SHEET_PLACE = "Padron, Espain";
  /**
   * Payment sheet event start date.
   */
  public static final LocalDate PAYMENT_SHEET_START_DATE =
        LocalDate.of(2014, Month.JANUARY, 1);
  /**
   * Payment sheet event end date.
   */
  public static final LocalDate PAYMENT_SHEET_END_DATE =
        LocalDate.of(2014, Month.JANUARY, 3);

  /**
   * An instance of Gson configured with a custom TypeAdapter for handling
   * {@link LocalDate} serialization and deserialization.
  This instance is created
   * using a {@link GsonBuilder} that registers a {@link LocalDateAdapter} to
   * ensure that {@code LocalDate} objects are properly converted to and from
   * JSON format.
   *
   * The {@link LocalDateAdapter} class should provide the implementation for
   * serializing {@code LocalDate} instances into their JSON
   * representation and deserializing JSON strings back into
   * {@code LocalDate} instances. This setup is necessary because
   * {@code LocalDate} is not natively supported by Gson and
   * requires a custom adapter for proper handling.
   *
   * This Gson instance can be used throughout the application for any JSON
   * processing involving {@code LocalDate} objects. For example, it
   * can be used to convert Java objects to JSON strings
   * and vice versa, ensuring that
   * {@code LocalDate} fields are correctly represented.
   *
   * Example usage:
   *
   * <pre>
   * {@code
   * String json = GSON.toJson(localDateInstance);
   * LocalDate date = GSON.fromJson(json, LocalDate.class);
   * }
   * </pre>
   *
   * @see LocalDateAdapter
   */
  public static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  /**
   * @return Payment sheet request form object.
   */
  public static CreatePaymentSheetRequest getPaymentSheetRequestForm() {
    return new CreatePaymentSheetRequest(
          UsersControllerFactory.USER_A_EMAIL, PAYMENT_SHEET_REASON,
          PAYMENT_SHEET_PLACE, PAYMENT_SHEET_START_DATE, PAYMENT_SHEET_END_DATE
    );
  }

  /**
   * @return Payment sheet request json.
   */
  public static String getPaymentSheetRequestFormJson() {
    return GSON.toJson(getPaymentSheetRequestForm());
  }

  /**
   * Private constructor to prevent instantiation.
   */
  private PaymentSheetControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
