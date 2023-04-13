package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the form used by controller as response for requesting all
 * invoices.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class InvoiceListResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6122905275533572886L;

    /**
     * List with all individual invoices responses.
     */
    private List<InvoiceResponse> invoicesList = new ArrayList<>();

    /**
     * Response constructor with parameters provided.
     *
     * @param value the invoices entity list.
     */
    public InvoiceListResponse(List<InvoiceResponse> value) {
        super();
        value.forEach((InvoiceResponse e) -> this.invoicesList.add(e));
    }

    /**
     * Get all invoices.
     *
     * @return The invoices list.
     */
    public List<InvoiceResponse> getInvoicesList() {
        return new ArrayList<>(invoicesList);
    }

    /**
     * Set invoices.
     *
     * @param value the invoices list.
     */
    public void setInvoicesList(List<InvoiceResponse> value) {
        this.invoicesList = new ArrayList<>(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoicesList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (InvoiceListResponse) obj;
        return Objects.equals(invoicesList, other.invoicesList);
    }

    @Override
    public String toString() {
        return "InvoiceListResponse [invoicesList=" + invoicesList + "]";
    }

}
