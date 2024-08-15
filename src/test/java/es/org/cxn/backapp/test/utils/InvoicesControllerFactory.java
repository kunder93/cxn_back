
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * A utility class that provides various factory methods and constants
 * related to invoice processing and JSON serialization/deserialization.
 * <p>
 * This class includes:
 * <ul>
 *   <li>Configuration of a Gson instance for handling {@link LocalDate}
 *   serialization and deserialization.</li>
 *   <li>Constants representing different invoice details.</li>
 *   <li>Methods to create request objects and their JSON representations
 *    for invoices.</li>
 *   <li>Methods to convert lists of invoice responses into JSON format.</li>
 * </ul>
 * The class is designed to be used for testing and creating
 * invoice-related data.
 * </p>
 */
public final class InvoicesControllerFactory {

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
  private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  /**
   * Invoice A number.
   */
  public static final int INVOICE_A_NUMBER = 1;
  /**
   * Invoice A series.
   */
  public static final String INVOICE_A_SERIES = "IAS";
  /**
   * Invoice A expedition date.
   */
  public static final LocalDate INVOICE_A_EXPEDITION_DATE =
        LocalDate.of(2020, 1, 8);
  /**
   * Invoice A tax exempt state.
   */
  public static final Boolean INVOICE_A_TAX_EXEMPT = Boolean.TRUE;
  /**
   * Invoice A buyer NIF CIF identifier.
   */
  public static final String INVOICE_A_BUYER =
        CompanyControllerFactory.COMPANY_B_NIFCIF;
  /**
   * Invoice A seller NIF CIF identifier.
   */
  public static final String INVOICE_A_SELLER =
        CompanyControllerFactory.COMPANY_A_NIFCIF;
  /**
   * Invoice A payment date.
   */
  public static final LocalDate INVOICE_A_PAYMENT_DATE =
        LocalDate.of(2020, 1, 24);

  /**
   * Invoice B number.
   */
  public static final int INVOICE_B_NUMBER = 2;
  /**
   * Invoice B series.
   */
  public static final String INVOICE_B_SERIES = "IBS";
  /**
   * Invoice B expedition date.
   */
  public static final LocalDate INVOICE_B_EXPEDITION_DATE =
        LocalDate.of(2020, 1, 16);
  /**
   * Invoice B tax exempt state.
   */
  public static final Boolean INVOICE_B_TAX_EXEMPT = Boolean.TRUE;
  /**
   * Invoice B buyer NIF CIF identifier.
   */
  public static final String INVOICE_B_BUYER =
        CompanyControllerFactory.COMPANY_B_NIFCIF;
  /**
   * Invoice B seller NIF CIF identifier.
   */
  public static final String INVOICE_B_SELLER =
        CompanyControllerFactory.COMPANY_A_NIFCIF;
  /**
   * Invoice B payment date.
   */
  public static final LocalDate INVOICE_B_PAYMENT_DATE =
        LocalDate.of(2020, 1, 5);

  /**
   * @return Invoice A request object.
   */
  public static CreateInvoiceRequestForm createInvoiceARequest() {
    return new CreateInvoiceRequestForm(
          INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
          INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT, INVOICE_A_SELLER,
          INVOICE_A_BUYER
    );
  }

  /**
   * @return Invoice B request object.-
   */
  public static CreateInvoiceRequestForm createInvoiceBRequest() {
    return new CreateInvoiceRequestForm(
          INVOICE_B_NUMBER, INVOICE_B_SERIES, INVOICE_B_PAYMENT_DATE,
          INVOICE_B_EXPEDITION_DATE, INVOICE_B_TAX_EXEMPT, INVOICE_B_SELLER,
          INVOICE_B_BUYER
    );
  }

  /**
   * @return Json from invoice A request.
   */
  public static String getInvoiceARequestJson() {
    return GSON.toJson(createInvoiceARequest());
  }

  /**
   * @return Json from invoice B request.
   */
  public static String getInvoiceBRequestJson() {
    return GSON.toJson(createInvoiceBRequest());
  }

  /**
   * @param invoices The invoices list.
   * @return json from list of invoices responses.
   */
  public static String
        getInvoiceListResponseJson(final List<InvoiceResponse> invoices) {
    var invoiceListResponse = new InvoiceListResponse(invoices);
    return GSON.toJson(invoiceListResponse);
  }

  /**
   * Constructor privado para evitar la instanciación de la clase.
   */
  private InvoicesControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
