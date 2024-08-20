
package es.org.cxn.backapp.model.form.requests;

/**
 * Represents the form used by the controller as a request to add regular
 * transport to payment sheet.
 * <p>
 * This is a DTO, meant to facilitate communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will ensure it receives all the required data.
 *
 * @param category      The regular transport category.
 * @param description  The regular transport description.
 * @param invoiceNumber The regular transport invoice number, if it exists.
 * @param invoiceSeries The regular transport invoice series.
 *
 * @author Santiago Paz.
 */
public record AddRegularTransportRequestForm(
      String category, String description, Integer invoiceNumber,
      String invoiceSeries
) {

}
