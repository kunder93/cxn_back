
package es.org.cxn.backapp.model.form.responses;

/**
 * Represents the form used by controller as a response for self-vehicle data.
 * <p>
 * This record is a Data Transfer Object (DTO) that facilitates
 * communication between
 * the view and the controller, providing an immutable representation of the
 * self-vehicle data.
 * </p>
 *
 * @param places   The places associated with the self-vehicle.
 * @param distance The distance covered by the self-vehicle.
 * @param kmPrice  The price per kilometer for the self-vehicle.
 *
 * @author Santiago Paz
 */
public record SelfVehicleDataResponse(
      String places, float distance, double kmPrice
) {

}
