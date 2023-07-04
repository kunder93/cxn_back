
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

/**
 * Represents the form used by controller as request of Food Housing data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class FoodHousingDataResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3110452922215007099L;

  /**
   * The food housing amount of days that last.
   */
  private Integer amountDays;
  /**
   * The food housing price per day.
   */
  private double dayPrice;

  /**
   * True if sleeps else false.
   */
  private boolean overnight;

  /**
   * Main empty constructor.
   */
  public FoodHousingDataResponse() {
    super();
  }

  /**
   * Constructor with parameters.
   *
   * @param amountDays The amount of days.
   * @param dayPrice   The price per day.
   * @param overnight  The overnight or if sleeps.
   */
  public FoodHousingDataResponse(
        final Integer amountDays, final double dayPrice, final boolean overnight
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
  public double getDayPrice() {
    return dayPrice;
  }

  /**
   * @return True if sleeps false else.
   */
  public boolean isOvernight() {
    return overnight;
  }

  /**
   * @param value The amount of days.
   */
  public void setAmountDays(final Integer value) {
    this.amountDays = value;
  }

  /**
   * @param value The price per day.
   */
  public void setDayPrice(final double value) {
    this.dayPrice = value;
  }

  /**
   * @param overnight The overnight.
   */
  public void setOvernight(final boolean overnight) {
    this.overnight = overnight;
  }

}
