
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A SelfVehicle entity interface representing a self-service vehicle. This
 * entity includes vehicle information such as places, distance traveled, and
 * price per kilometer.
 *
 * @author Santiago Paz Perez.
 */
public interface SelfVehicleEntity extends Serializable {

    /**
     * Retrieves the total distance traveled by the vehicle.
     *
     * @return The distance traveled by the vehicle.
     */
    BigDecimal getDistance();

    /**
     * Retrieves the identifier of the vehicle.
     *
     * @return The identifier of the vehicle.
     */
    Integer getIdentifier();

    /**
     * Retrieves the price charged per kilometer traveled by the vehicle.
     *
     * @return The price per kilometer.
     */
    BigDecimal getKmPrice();

    /**
     * Retrieves the number of available places in the vehicle.
     *
     * @return The number of available places.
     */
    String getPlaces();

    /**
     * Sets the total distance traveled by the vehicle.
     *
     * @param distance The distance traveled by the vehicle.
     */
    void setDistance(BigDecimal distance);

    /**
     * Sets the identifier of the vehicle.
     *
     * @param identifier The unique identifier of the vehicle.
     */
    void setIdentifier(Integer identifier);

    /**
     * Sets the price charged per kilometer traveled by the vehicle.
     *
     * @param kmPrice The price per kilometer.
     */
    void setKmPrice(BigDecimal kmPrice);

    /**
     * Sets the number of available places in the vehicle.
     *
     * @param places The number of available places.
     */
    void setPlaces(String places);
}
