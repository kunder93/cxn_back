
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A SelfVehicle entity interface representing a self-service vehicle.
 * This entity includes vehicle information such as places, distance traveled,
 * and price per kilometer.
 *
 * @author Santiago Paz Perez.
 */
public interface SelfVehicleEntity extends Serializable {

  /**
   * Retrieves the identifier of the vehicle.
   *
   * @return The identifier of the vehicle.
   */
  Integer getIdentifier();

  /**
   * Retrieves the number of available places in the vehicle.
   *
   * @return The number of available places.
   */
  String getPlaces();

  /**
   * Retrieves the total distance traveled by the vehicle.
   *
   * @return The distance traveled by the vehicle.
   */
  BigDecimal getDistance();

  /**
   * Retrieves the price charged per kilometer traveled by the vehicle.
   *
   * @return The price per kilometer.
   */
  BigDecimal getKmPrice();

  /**
   * Sets the identifier of the vehicle.
   *
   * @param id The unique identifier of the vehicle.
   */
  void setIdentifier(Integer id);

  /**
   * Sets the number of available places in the vehicle.
   *
   * @param places The number of available places.
   */
  void setPlaces(String places);

  /**
   * Sets the total distance traveled by the vehicle.
   *
   * @param distance The distance traveled by the vehicle.
   */
  void setDistance(BigDecimal distance);

  /**
   * Sets the price charged per kilometer traveled by the vehicle.
   *
   * @param kmPrice The price per kilometer.
   */
  void setKmPrice(BigDecimal kmPrice);
}
