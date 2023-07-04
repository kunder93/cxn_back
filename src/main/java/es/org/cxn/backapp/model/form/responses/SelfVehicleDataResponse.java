
package es.org.cxn.backapp.model.form.responses;

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
public final class SelfVehicleDataResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3110389922215947999L;

  /**
   * The regular transport Category.
   */
  private String places;
  /**
   * The regular transport description.
   */
  private float distance;

  /**
   * The regular transport price per kilometer.
   */
  private double kmPrice;

  /**
   * Main empty constructor.
   */
  public SelfVehicleDataResponse() {
    super();
  }

  /**
   * Constructor.
   *
   * @param places   The places.
   * @param distance The distance.
   * @param kmPrice  The price per kilometer.
   */
  public SelfVehicleDataResponse(
        final String places, final float distance, final double kmPrice
  ) {
    super();
    this.places = places;
    this.distance = distance;
    this.kmPrice = kmPrice;
  }

  /**
   * @return The places.
   */
  public String getPlaces() {
    return places;
  }

  /**
   * @return The distance.
   */
  public float getDistance() {
    return distance;
  }

  /**
   * @return The price per kilometer.
   */
  public double getKmPrice() {
    return kmPrice;
  }

  /**
   * @param places The places.
   */
  public void setPlaces(final String places) {
    this.places = places;
  }

  /**
   * @param distance The distance.
   */
  public void setDistance(final float distance) {
    this.distance = distance;
  }

  /**
   * @param kmPrice The price per kilometer.
   */
  public void setKmPrice(final double kmPrice) {
    this.kmPrice = kmPrice;
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
    var other = (SelfVehicleDataResponse) obj;
    return Double.doubleToLongBits(distance) == Double
          .doubleToLongBits(other.distance)
          && Double.doubleToLongBits(kmPrice) == Double
                .doubleToLongBits(other.kmPrice)
          && Objects.equals(places, other.places);
  }

  @Override
  public String toString() {
    return "SelfVehicleDataResponse [places=" + places + ", distance="
          + distance + ", kmPrice=" + kmPrice + "]";
  }

}
