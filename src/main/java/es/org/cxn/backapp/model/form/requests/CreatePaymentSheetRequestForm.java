
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
public final class CreatePaymentSheetRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3313389555214347185L;

  /**
   * The email of user who owns payment sheet.
   */
  private String userEmail;

  /**
   * The payment sheet event reason.
   */
  private String reason;

  /**
   * The payment sheet event place.
   */
  private String place;

  /**
   * The payment sheet event start date.
   */
  private LocalDate startDate;

  /**
   * The payment sheet event end date.
   */
  private LocalDate endDate;

  /**
   * Main empty constructor.
   */
  public CreatePaymentSheetRequestForm() {
    super();
  }

  /**
   * Constructor with parameters.
   *
   * @param userEmail The payment sheet owner user email.
   * @param reason    The payment sheet event reason.
   * @param place     The payment sheet event place.
   * @param startDate The payment sheet event start date.
   * @param endDate   The payment sheet event end date.
   */
  public CreatePaymentSheetRequestForm(
        final String userEmail, final String reason, final String place,
        final LocalDate startDate, final LocalDate endDate
  ) {
    super();
    this.userEmail = userEmail;
    this.reason = reason;
    this.place = place;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * @return They payment sheet user owner email.
   */
  public String getUserEmail() {
    return userEmail;
  }

  /**
   * @return The payment sheet event reason.
   */
  public String getReason() {
    return reason;
  }

  /**
   * @return The payment sheet event place.
   */
  public String getPlace() {
    return place;
  }

  /**
   * @return The payment sheet event start date.
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * @return The payment sheet event end date.
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * @param value the Owner user email.
   */
  public void setUserEmail(final String value) {
    this.userEmail = value;
  }

  /**
   * @param value The event reason.
   */
  public void setReason(final String value) {
    this.reason = value;
  }

  /**
   * @param value The event place.
   */
  public void setPlace(final String value) {
    this.place = value;
  }

  /**
   * @param startDate The payment sheet event start date.
   */
  public void setStartDate(final LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * @param value The payment sheet event end date.
   */
  public void setEndDate(final LocalDate value) {
    this.endDate = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(endDate, place, reason, startDate, userEmail);
  }

  @Override
  public boolean equals(final Object obj) {
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
    return "CreatePaymentSheetRequestForm [userEmail=" + userEmail + ", reason="
          + reason + ", place=" + place + ", startDate=" + startDate
          + ", endDate=" + endDate + "]";
  }

}
