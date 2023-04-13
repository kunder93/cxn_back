package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of create company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class AddRegularTransportRequestForm implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3112385622215947705L;

    /**
     * The regular transport Category
     */
    private String category;
    /**
     * The regular transport description.
     */
    private String description;

    private Integer invoiceNumber;

    private String invoiceSeries;

    /**
     * @param category
     * @param description
     * @param invoiceNumber
     * @param invoiceSeries
     */
    public AddRegularTransportRequestForm(
            String category, String description, Integer invoiceNumber,
            String invoiceSeries
    ) {
        super();
        this.category = category;
        this.description = description;
        this.invoiceNumber = invoiceNumber;
        this.invoiceSeries = invoiceSeries;
    }

    /**
     * Main empty constructor.
     */
    public AddRegularTransportRequestForm() {
        super();
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(category, description, invoiceNumber, invoiceSeries);
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
        var other = (AddRegularTransportRequestForm) obj;
        return Objects.equals(category, other.category)
                && Objects.equals(description, other.description)
                && Objects.equals(invoiceNumber, other.invoiceNumber)
                && Objects.equals(invoiceSeries, other.invoiceSeries);
    }

    @Override
    public String toString() {
        return "AddRegularTransportRequestForm [category=" + category
                + ", description=" + description + ", invoiceNumber="
                + invoiceNumber + ", invoiceSeries=" + invoiceSeries + "]";
    }

}
