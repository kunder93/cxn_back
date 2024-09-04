
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by the controller as a request to create a company.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by encapsulating the necessary information for creating a
 * company.
 * It includes validation annotations to ensure that the required data is
 * provided and conforms to the expected format.
 * </p>
 *
 * @param nif      The Tax Identification Number (NIF) of the company.
 *                 This field is required to be non-blank and should not
 *                 exceed the maximum
 *                 length defined by {@link ValidationConstants}.
 * @param name     The name of the company. This field may be blank.
 * @param address  The address of the company. This field may be blank.
 *
 * @author Santiago Paz
 */
public record CreateCompanyRequest(
      @NotBlank(message = ValidationConstants.COMPANY_NIF_NOT_BLANK_MESSAGE)
      @Size(
            max = ValidationConstants.COMPANY_NIF_MAX_LENGTH,
            message = ValidationConstants.COMPANY_NIF_SIZE_MESSAGE
      )
      String nif,

      @NotBlank(message = ValidationConstants.COMPANY_NAME_NOT_BLANK_MESSAGE)
      @Size(
            max = ValidationConstants.COMPANY_NAME_MAX_LENGTH,
            message = ValidationConstants.COMPANY_NAME_SIZE_MESSAGE
      )
      String name,

      @NotBlank(message = ValidationConstants.COMPANY_ADDRESS_NOT_BLANK_MESSAGE)
      @Size(
            max = ValidationConstants.COMPANY_ADDRESS_MAX_LENGTH,
            message = ValidationConstants.COMPANY_ADDRESS_SIZE_MESSAGE
      )
      String address
) {

}
