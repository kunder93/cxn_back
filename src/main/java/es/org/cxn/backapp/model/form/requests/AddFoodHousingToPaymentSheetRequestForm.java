
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of add food or housing to
 * existing payment sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class AddFoodHousingToPaymentSheetRequestForm
      implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3100089900015947999L;

  /**
   * The amount of days that last the event.
   */
  private Integer amountDays;
  /**
   * Price for each day.
   */
  private float dayPrice;

  /**
   * True if sleep is included false if not.
   */
  private Boolean overnight;

  /**
   * Main empty constructor.
   */
  public AddFoodHousingToPaymentSheetRequestForm() {
    super();
  }

  /**
   * Constructor with provided values.
   *
   * @param amountDays The amount of days.
   * @param dayPrice   The price of each day.
   * @param overnight  Boolean if includes sleep or not.
   */
  public AddFoodHousingToPaymentSheetRequestForm(
        final Integer amountDays, final float dayPrice, final Boolean overnight
  ) {
    super();
    this.amountDays = amountDays;
    this.dayPrice = dayPrice;
    this.overnight = overnight;
  }

  /**
   * @return The amount of days.
   */
  public Integer getAmountDays() {
    return amountDays;
  }

  /**
   * @return The price per day.
   */
  public float getDayPrice() {
    return dayPrice;
  }

  /**
   * @return True if overnight false if not.
   */
  public Boolean getOvernight() {
    return overnight;
  }

  /**
   * @param value The amount of days.
   */
  public void setAmountDays(final Integer value) {
    this.amountDays = value;
  }

  /**
   * @param value the price per day.
   */
  public void setDayPrice(final float value) {
    this.dayPrice = value;
  }

  /**
   * @param value The overnight boolean value.
   */
  public void setOvernight(final Boolean value) {
    this.overnight = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amountDays, dayPrice, overnight);
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
    var other = (AddFoodHousingToPaymentSheetRequestForm) obj;
    return Objects.equals(amountDays, other.amountDays)
          && Float.floatToIntBits(dayPrice) == Float
                .floatToIntBits(other.dayPrice)
          && Objects.equals(overnight, other.overnight);
  }

  @Override
  public String toString() {
    return "AddFoodHousingToPaymentSheetRequestForm [amountDays=" + amountDays
          + ", dayPrice=" + dayPrice + ", overnight=" + overnight + "]";
  }

}
