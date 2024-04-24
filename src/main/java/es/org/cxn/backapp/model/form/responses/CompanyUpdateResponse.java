
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response of update company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * </p>
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CompanyUpdateResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3852186551148263014L;

  /**
   * The company nif.
   */
  private String nif;
  /**
   * The company name.
   */
  private String name;
  /**
   * The company address.
   */
  private String address;
}
