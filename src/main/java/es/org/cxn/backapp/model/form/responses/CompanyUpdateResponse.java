
package es.org.cxn.backapp.model.form.responses;

/**
 * Represents the response DTO used by the controller when updating a
 * company's details.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when a company is updated.
 * It contains essential details of the company, such as its NIF, name,
 * and address.
 * </p>
 *
 * @param nif The National Identification Number (NIF) of the company.
 * @param name The name of the company.
 * @param address The address of the company.
 *
 * @author Santiago Paz
 */
public record CompanyUpdateResponse(String nif, String name, String address) {

}
