
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;

import java.time.LocalDate;
import java.time.Month;

public class PaymentSheetControllerFactory {

  public final static String PAYMENT_SHEET_REASON = "MyPaymentSheet reason";
  public final static String PAYMENT_SHEET_PLACE = "Padron, Espain";
  public final static LocalDate PAYMENT_SHEET_START_DATE =
        LocalDate.of(2014, Month.JANUARY, 1);
  public final static LocalDate PAYMENT_SHEET_END_DATE =
        LocalDate.of(2014, Month.JANUARY, 3);

  public static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  public static CreatePaymentSheetRequestForm getPaymentSheetRequestForm() {
    return new CreatePaymentSheetRequestForm(
          UsersControllerFactory.USER_A_EMAIL, PAYMENT_SHEET_REASON,
          PAYMENT_SHEET_PLACE, PAYMENT_SHEET_START_DATE, PAYMENT_SHEET_END_DATE
    );
  };

  public static String getPaymentSheetRequestFormJson() {
    return gson.toJson(getPaymentSheetRequestForm());
  }

  // Private constructor to prevent instantiation
  private PaymentSheetControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
