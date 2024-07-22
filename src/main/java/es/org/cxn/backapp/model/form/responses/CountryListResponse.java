
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all
 * countries.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
@Data
public final class CountryListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6152907846653572886L;

  /**
   * List with all stored countries.
   */
  private List<CountryResponse> countryList = new ArrayList<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param value The countries list.
   */
  public CountryListResponse(final List<PersistentCountryEntity> value) {
    super();
    value.forEach(
          (PersistentCountryEntity e) -> this.countryList
                .add(new CountryResponse(e))
    );
  }
}
