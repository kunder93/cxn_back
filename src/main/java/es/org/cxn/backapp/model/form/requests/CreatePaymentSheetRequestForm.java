package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the form used by controller as request of create payment sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class CreatePaymentSheetRequestForm implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3313389555214347185L;

    /**
     * The email of user who owns payment sheet.
     */
    private String userEmail;

    /**
     * The company name.
     */
    private String reason;

    /**
     * The company identity tax number.
     */
    private String place;

    /**
     * The payment sheet start date.2
     */
    private LocalDate startDate;

    /**
     * The company address.
     */
    private LocalDate endDate;

    /**
    *
    */
    public CreatePaymentSheetRequestForm() {
        super();
    }

    /**
     * @param userEmail
     * @param reason
     * @param place
     * @param startDate
     * @param endDate
     */
    public CreatePaymentSheetRequestForm(
            String userEmail, String reason, String place, LocalDate startDate,
            LocalDate endDate
    ) {
        super();
        this.userEmail = userEmail;
        this.reason = reason;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getReason() {
        return reason;
    }

    public String getPlace() {
        return place;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endDate, place, reason, startDate, userEmail);
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
        var other = (CreatePaymentSheetRequestForm) obj;
        return Objects.equals(endDate, other.endDate)
                && Objects.equals(place, other.place)
                && Objects.equals(reason, other.reason)
                && Objects.equals(startDate, other.startDate)
                && Objects.equals(userEmail, other.userEmail);
    }

    @Override
    public String toString() {
        return "CreatePaymentSheetRequestForm [userEmail=" + userEmail
                + ", reason=" + reason + ", place=" + place + ", startDate="
                + startDate + ", endDate=" + endDate + "]";
    }

}
