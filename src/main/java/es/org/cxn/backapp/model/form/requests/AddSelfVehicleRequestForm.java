
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of add self vehicle to
 * payment sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class AddSelfVehicleRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112385111515947705L;

  /**
   * The self vehicle places visited.
   */
  private String places;
  /**
   * The total distance traveled.
   */
  private float distance;

  /**
   * Price per kilometer traveled.
   */
  private double kmPrice;

  /**
   * @param places   The places visited.
   * @param distance The total distance traveled.
   * @param kmPrice  Price per kilometer traveled.
   */
  public AddSelfVehicleRequestForm(
        final String places, final float distance, final double kmPrice
  ) {
    super();
    this.places = places;
    this.distance = distance;
    this.kmPrice = kmPrice;
  }

  /**
   * Main empty constructor.
   */
  public AddSelfVehicleRequestForm() {
    super();
  }

  /**
   * @return Get places traveled.
   */
  public String getPlaces() {
    return places;
  }

  /**
   * @return The total distance.
   */
  public float getDistance() {
    return distance;
  }

  /**
   * @return Price per kilometer.
   */
  public double getKmPrice() {
    return kmPrice;
  }

  /**
   * @param value The places value.
   */
  public void setPlaces(final String value) {
    this.places = value;
  }

  /**
   * @param value The distance value.
   */
  public void setDistance(final float value) {
    this.distance = value;
  }

  /**
   * @param value The km price value.
   */
  public void setKmPrice(final double value) {
    this.kmPrice = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(distance, kmPrice, places);
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
    var other = (AddSelfVehicleRequestForm) obj;
    return Float.floatToIntBits(distance) == Float
          .floatToIntBits(other.distance)
          && Double.doubleToLongBits(kmPrice) == Double
                .doubleToLongBits(other.kmPrice)
          && Objects.equals(places, other.places);
  }

  @Override
  public String toString() {
    return "AddSelfVehicleRequestForm [places=" + places + ", distance="
          + distance + ", kmPrice=" + kmPrice + "]";
  }
}
