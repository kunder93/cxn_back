
package es.org.cxn.backapp.model.form.requests;

/**
 * Represents the form used by the controller as a request to update company
 * details.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, capturing the necessary information required to update
 * an existing company's details.
 * </p>
 *
 * <p>
 * The {@code CompanyUpdateRequestForm} includes fields for the company's name
 * and address.
 * These fields can be used to update the company's information in the system.
 * </p>
 *
 * @param name    The new name of the company. This field is optional and can
 * be null if no update is required for the company's name.
 * @param address The new address of the company. This field is optional and
 * can be null if no update is required for the company's address.
 *
 * <p>
 * This record does not include validation annotations, which means the fields
 * can be null or empty.
 * Validation logic should be handled separately in the application, if
 * required.
 * </p>
 *
 * @author Santiago Paz.
 */
public record CompanyUpdateRequestForm(String name, String address) {

}
