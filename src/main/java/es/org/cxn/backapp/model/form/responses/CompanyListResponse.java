
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Represents the form used by the controller as a response for requesting all
 * companies.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller.
 * It contains a set of company responses, ensuring that each company is
 * represented uniquely and sorted.
 * </p>
 *
 * @param companiesList A {@link Set} of {@link CompanyResponse} representing
 * the companies.
 *
 * @author Santiago Paz
 */
public record CompanyListResponse(Set<CompanyResponse> companiesList) {

  /**
   * Constructs a {@link CompanyListResponse} from a list of
   * {@link PersistentCompanyEntity}.
   * <p>
   * This constructor maps each {@link PersistentCompanyEntity} to a
   * {@link CompanyResponse} and collects them into a
   * {@link TreeSet}, which ensures that the list is sorted and does not
   * contain duplicate entries.
   * </p>
   *
   * @param companiesEntitiesList A {@link List} of
   * {@link PersistentCompanyEntity} to be converted.
   */
  public CompanyListResponse(
        final List<PersistentCompanyEntity> companiesEntitiesList
  ) {
    this(
          companiesEntitiesList.stream().map(CompanyResponse::new)
                .collect(Collectors.toCollection(TreeSet::new))
    );
  }
}
