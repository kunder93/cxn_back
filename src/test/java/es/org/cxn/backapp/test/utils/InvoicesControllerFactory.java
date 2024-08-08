
package es.org.cxn.backapp.test.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;

import java.time.LocalDate;
import java.util.List;

public class InvoicesControllerFactory {

  private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

  // Definición de constantes para la factura A
  public static final int INVOICE_A_NUMBER = 1;
  public static final String INVOICE_A_SERIES = "IAS";
  public static final LocalDate INVOICE_A_EXPEDITION_DATE =
        LocalDate.of(2020, 1, 8);
  public static final Boolean INVOICE_A_TAX_EXEMPT = Boolean.TRUE;
  public static final String INVOICE_A_BUYER =
        CompanyControllerFactory.COMPANY_B_NIFCIF;
  public static final String INVOICE_A_SELLER =
        CompanyControllerFactory.COMPANY_A_NIFCIF;
  public static final LocalDate INVOICE_A_PAYMENT_DATE =
        LocalDate.of(2020, 1, 24);

  // Definición de constantes para la factura B
  public static final int INVOICE_B_NUMBER = 2;
  public static final String INVOICE_B_SERIES = "IBS";
  public static final LocalDate INVOICE_B_EXPEDITION_DATE =
        LocalDate.of(2020, 1, 16);
  public static final Boolean INVOICE_B_TAX_EXEMPT = Boolean.TRUE;
  public static final String INVOICE_B_BUYER =
        CompanyControllerFactory.COMPANY_B_NIFCIF;
  public static final String INVOICE_B_SELLER =
        CompanyControllerFactory.COMPANY_A_NIFCIF;
  public static final LocalDate INVOICE_B_PAYMENT_DATE =
        LocalDate.of(2020, 1, 5);

  // Método para obtener una instancia de CreateInvoiceRequestForm para la factura A
  public static CreateInvoiceRequestForm createInvoiceARequest() {
    return new CreateInvoiceRequestForm(
          INVOICE_A_NUMBER, INVOICE_A_SERIES, INVOICE_A_PAYMENT_DATE,
          INVOICE_A_EXPEDITION_DATE, INVOICE_A_TAX_EXEMPT, INVOICE_A_SELLER,
          INVOICE_A_BUYER
    );
  }

  // Método para obtener una instancia de CreateInvoiceRequestForm para la factura B
  public static CreateInvoiceRequestForm createInvoiceBRequest() {
    return new CreateInvoiceRequestForm(
          INVOICE_B_NUMBER, INVOICE_B_SERIES, INVOICE_B_PAYMENT_DATE,
          INVOICE_B_EXPEDITION_DATE, INVOICE_B_TAX_EXEMPT, INVOICE_B_SELLER,
          INVOICE_B_BUYER
    );
  }

  // Método para obtener el JSON de la solicitud de la factura A
  public static String getInvoiceARequestJson() {
    return gson.toJson(createInvoiceARequest());
  }

  // Método para obtener el JSON de la solicitud de la factura B
  public static String getInvoiceBRequestJson() {
    return gson.toJson(createInvoiceBRequest());
  }

  // Método para obtener el JSON de una lista de respuestas de facturas
  public static String
        getInvoiceListResponseJson(List<InvoiceResponse> invoices) {
    var invoiceListResponse = new InvoiceListResponse(invoices);
    return gson.toJson(invoiceListResponse);
  }

  // Constructor privado para evitar la instanciación de la clase
  private InvoicesControllerFactory() {
    throw new UnsupportedOperationException(
          "Utility class should not be instantiated."
    );
  }
}
