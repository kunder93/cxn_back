
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class CreateCompanyRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133389822212147705L;

  /**
   * The company nif.
   */
  private String nif;

  /**
   * The company name.
   */
  @NotBlank(message = Constants.NAME_NOT_BLANK)
  @Size(
        max = Constants.NAME_MAX_LENGTH,
        message = Constants.NAME_MAX_LENGTH_MESSAGE
  )
  private String name;

  /**
   * The company address.
   */
  private String address;

}
