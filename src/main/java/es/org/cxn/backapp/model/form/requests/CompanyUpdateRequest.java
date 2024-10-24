
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by the controller as a request to update company
 * details.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, capturing the necessary information required to update
 * an existing company's details. The fields in this DTO include the company's
 * name and address.
 * </p>
 *
 * @param name    The new name of the company. This field is required and
 *                should not be blank. The length of this field must not exceed
 *                {@link ValidationConstants#MAX_NAME_SIZE} characters.
 * @param address The new address of the company. This field is required and
 *                should not be blank. The length of this field must not exceed
 *                {@link ValidationConstants#MAX_ADDRESS_SIZE} characters.
 *
 * <p>
 * Validation is enforced using Jakarta Bean Validation annotations.
 *  The validation ensures that the company name and address meet the
 *  constraints defined here with values provided by
 *  {@link ValidationConstants}.
 * </p>
 */
public record CompanyUpdateRequest(

      @NotBlank(message = ValidationConstants.NAME_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.MAX_NAME_SIZE,
            message = ValidationConstants.NAME_SIZE_MESSAGE
      )
      String name,

      @NotBlank(message = ValidationConstants.ADDRESS_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.MAX_ADDRESS_SIZE,
            message = ValidationConstants.ADDRESS_SIZE_MESSAGE
      )
      String address

) {
}
