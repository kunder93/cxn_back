package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

/**
 * Represents the form used by controller as response for requesting one
 * invoice.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class RegularTransportResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3180089459011962505L;

    private String category;
    private String description;
    private InvoiceResponse invoiceResponse;

    /**
     *
     */
    public RegularTransportResponse() {
        super();
    }

    /**
     * @param category
     * @param description
     * @param invoiceResponse
     */
    public RegularTransportResponse(PersistentRegularTransportEntity entity) {
        super();
        this.category = entity.getCategory();
        this.description = entity.getDescription();
        this.invoiceResponse = new InvoiceResponse(
                entity.getTransportInvoice()
        );
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public InvoiceResponse getInvoiceResponse() {
        return invoiceResponse;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInvoiceResponse(InvoiceResponse invoiceResponse) {
        this.invoiceResponse = invoiceResponse;
    }

}
