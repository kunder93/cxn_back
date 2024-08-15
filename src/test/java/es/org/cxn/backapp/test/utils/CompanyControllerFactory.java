
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;

import java.time.LocalDate;

public final class CompanyControllerFactory {

  /**
   * Company A NIF CIF.
   */
  public static final String COMPANY_A_NIFCIF = "45235234-G";
  /**
   * Company A name.
   */
  public static final String COMPANY_A_NAME = "MyCompanyName";
  /**
   * Company A address.
   */
  public static final String COMPANY_A_ADDRESS = "MyCompanyAddress";

  /**
   * Company B NIF CIF.
   */
  public static final String COMPANY_B_NIFCIF = "33344434-G";
  /**
   * Company B name.
   */
  public static final String COMPANY_B_NAME = "OtherCompanyName";
  /**
   * Company B address.
   */
  public static final String COMPANY_B_ADDRESS = "OtherCompanyAddress";

  /**
   * A static instance of {@link Gson} configured for
   * handling {@link LocalDate} objects.
   * <p>
   * This {@link Gson} instance is created using a {@link GsonBuilder} with
   * a custom {@link LocalDateAdapter}
   * registered as a type adapter. The {@link LocalDateAdapter} is responsible
   * for serializing {@code LocalDate}
   * instances to their JSON representation and deserializing JSON strings
   * back into {@code LocalDate} objects.
   * <p>
   * By configuring {@link Gson} with this adapter, we ensure that
   * {@code LocalDate} objects are correctly processed
   * when converting between Java objects and JSON. This is necessary
   * because the standard {@link Gson} library does
   * not natively support {@code LocalDate} serialization and deserialization.
   * <p>
   * Example usage:
   * <pre>
   * {@code
   * // Serialize a LocalDate object to JSON
   * String json = GSON.toJson(localDateInstance);
   *
   * // Deserialize a JSON string back to a LocalDate object
   * LocalDate date = GSON.fromJson(json, LocalDate.class);
   * }
   * </pre>
   */
  public static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  /**
   * Company A request.
   */
  private static final CreateCompanyRequestForm COMPANY_A_REQUEST =
        new CreateCompanyRequestForm(
              COMPANY_A_NIFCIF, COMPANY_A_NAME, COMPANY_A_ADDRESS
        );

  /**
   * Company B request.
   */
  private static final CreateCompanyRequestForm COMPANY_B_REQUEST =
        new CreateCompanyRequestForm(
              COMPANY_B_NIFCIF, COMPANY_B_NAME, COMPANY_B_ADDRESS
        );

  /**
   * @return Company A request json.
   */
  public static String getCompanyARequestJson() {
    return GSON.toJson(COMPANY_A_REQUEST);
  }

  /**
   * @return Company B request json.
   */
  public static String getCompanyBRequestJson() {
    return GSON.toJson(COMPANY_B_REQUEST);
  }

  /**
   * Private constructor to prevent instantiation.
   */
  private CompanyControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
