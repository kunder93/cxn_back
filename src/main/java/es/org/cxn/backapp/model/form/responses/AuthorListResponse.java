
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.AuthorEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the form used by the controller as a response for requesting all
 * authors.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 * @param authorList The author list with authors responses.
 * @author Santiago Paz.
 */
public record AuthorListResponse(Set<AuthorResponse> authorList) {

  /**
   * Private constructor to convert a list of {@link AuthorEntity} into a set
   * of {@link AuthorResponse}.
   *
   * @param value The list of author entities.
   */
  private AuthorListResponse(final List<AuthorEntity> value) {
    this(
          value.stream().map(AuthorResponse::new)
                .collect(Collectors.toCollection(HashSet::new))
    );
  }

  /**
   * Factory method that takes a list of {@link AuthorEntity} and converts it
   * into a set of {@link AuthorResponse}.
   *
   * @param value The list of author entities.
   * @return An instance of {@link AuthorListResponse}.
   */
  public static AuthorListResponse from(final List<AuthorEntity> value) {
    return new AuthorListResponse(value);
  }
}
