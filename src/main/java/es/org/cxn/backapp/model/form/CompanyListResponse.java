package es.org.cxn.backapp.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

/**
 * Represents the form used by controller as response for requesting all
 * invoices.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class CompanyListResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6152905276653572886L;

    /**
     * List with all stored companies.
     */
    private List<CompanyResponse> companiesList = new ArrayList<>();

    /**
     * Constructor with provided parameters values.
     *
     * @param value The companies list.
     */
    public CompanyListResponse(List<PersistentCompanyEntity> value) {
        super();
        value.forEach(
                (PersistentCompanyEntity e) -> this.companiesList
                        .add(new CompanyResponse(e))
        );
    }

    /**
     * Get response companies list.
     *
     * @return The response companies list.
     */
    public List<CompanyResponse> getCompaniesList() {
        return new ArrayList<>(companiesList);
    }

    /**
     * Set response companies list.
     *
     * @param value The companies list.
     */
    public void setCompaniesList(List<CompanyResponse> value) {
        this.companiesList = new ArrayList<>(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companiesList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (CompanyListResponse) obj;
        return Objects.equals(companiesList, other.companiesList);
    }

    @Override
    public String toString() {
        return "CompaniesListResponse [companies=" + companiesList + "]";
    }

}
