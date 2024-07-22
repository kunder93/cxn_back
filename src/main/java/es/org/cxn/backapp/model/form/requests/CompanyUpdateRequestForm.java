
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of update company.
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
public final class CompanyUpdateRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7447202297370722033L;

  /**
   * The company name.
   */
  private String name;
  /**
   * The company address.
   */
  private String address;

}
