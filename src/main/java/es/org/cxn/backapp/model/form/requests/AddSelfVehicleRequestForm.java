
package es.org.cxn.backapp.model.form.requests;

/**
 * Represents the form used by the controller as a request to add a self-owned
 * vehicle to a payment sheet.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, ensuring that the necessary information is provided for
 * adding a self-owned vehicle to the payment sheet.
 * </p>
 *
 * <p>
 * The {@code AddSelfVehicleRequestForm} includes fields for the number of
 * places in the vehicle, the distance traveled, and the price per kilometer.
 * These fields help calculate the total cost associated with using the self
 * owned vehicle.
 * </p>
 *
 * @param places   The number of available places in the vehicle. This is
 * expected to be a string representation of the number of seats or capacity.
 * @param distance The distance traveled by the vehicle in kilometers.
 * This is a floating-point number that indicates the total distance covered.
 * @param kmPrice  The cost per kilometer of using the vehicle. This is a
 * double precision value representing the price per kilometer.
 *
 * <p>
 * These fields are expected to be validated by the controller to ensure
 * accurate data is received for processing the payment sheet.
 * </p>
 *
 * @author Santiago Paz
 */
public record AddSelfVehicleRequestForm(
      String places, float distance, double kmPrice
) {

}
