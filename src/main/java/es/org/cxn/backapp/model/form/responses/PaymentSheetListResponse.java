package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

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
public class PaymentSheetListResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -2152905244656672886L;

    /**
     * List with all stored companies.
     */
    private List<PaymentSheetResponse> paymentSheetsList = new ArrayList<>();

    /**
     * Constructor with provided parameters values.
     *
     * @param value The companies list.
     */
    public PaymentSheetListResponse(
            List<PersistentPaymentSheetEntity> PaymentSheetEntityList
    ) {
        super();
        PaymentSheetEntityList
                .forEach((PersistentPaymentSheetEntity entity) -> {
                    this.paymentSheetsList
                            .add(new PaymentSheetResponse(entity));
                });
    }

    public List<PaymentSheetResponse> getPaymentSheetsList() {
        return new ArrayList<>(paymentSheetsList);
    }

    public void setPaymentSheetsList(
            List<PaymentSheetResponse> paymentSheetsList
    ) {
        this.paymentSheetsList = new ArrayList<>(paymentSheetsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentSheetsList);
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
        var other = (PaymentSheetListResponse) obj;
        return Objects.equals(paymentSheetsList, other.paymentSheetsList);
    }

    @Override
    public String toString() {
        return "PaymentSheetListResponse [paymentSheetsList="
                + paymentSheetsList + "]";
    }

}
