
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all
 * authors.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
@Data
public final class AuthorListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6123427531674372216L;

  /**
   * List with all stored authors.
   */
  private Set<AuthorResponse> authorList = new HashSet<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param authorList The authors entities list.
   */
  public AuthorListResponse(final List<PersistentAuthorEntity> authorList) {
    super();
    authorList.forEach(
          (AuthorEntity e) -> this.authorList.add(new AuthorResponse(e))
    );
  }
}
