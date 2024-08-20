
package es.org.cxn.backapp.model.form.responses;

import java.util.List;

/**
 * Represents the response DTO used by the controller for requesting all
 * invoices.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting a list of
 * all invoices. It contains a list of invoice responses.
 * </p>
 *
 * @param invoicesList The list of individual invoice responses.
 * @author Santiago Paz
 */
public record InvoiceListResponse(List<InvoiceResponse> invoicesList) {

}
