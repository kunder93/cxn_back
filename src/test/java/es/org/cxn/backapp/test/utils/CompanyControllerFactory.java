
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;

import java.time.LocalDate;

public class CompanyControllerFactory {

  public static final String COMPANY_A_NIFCIF = "45235234-G";
  public static final String COMPANY_A_NAME = "MyCompanyName";
  public static final String COMPANY_A_ADDRESS = "MyCompanyAddress";

  public static final String COMPANY_B_NIFCIF = "33344434-G";
  public static final String COMPANY_B_NAME = "OtherCompanyName";
  public static final String COMPANY_B_ADDRESS = "OtherCompanyAddress";

  private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  private static final CreateCompanyRequestForm createCompanyARequest =
        new CreateCompanyRequestForm(
              COMPANY_A_NIFCIF, COMPANY_A_NAME, COMPANY_A_ADDRESS
        );

  private static final CreateCompanyRequestForm createCompanyBRequest =
        new CreateCompanyRequestForm(
              COMPANY_B_NIFCIF, COMPANY_B_NAME, COMPANY_B_ADDRESS
        );

  public static String getCompanyARequestJson() {
    return gson.toJson(createCompanyARequest);
  }

  public static String getCompanyBRequestJson() {
    return gson.toJson(createCompanyBRequest);
  }

  // Private constructor to prevent instantiation
  private CompanyControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
