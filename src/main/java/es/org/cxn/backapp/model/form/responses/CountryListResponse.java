package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Represents the form used by controller as response for requesting all
 * countries.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
public class CountryListResponse implements Serializable {

    /**
     * Serial UID
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
    public CountryListResponse(List<PersistentCountryEntity> value) {
        super();
        value.forEach(
                (PersistentCountryEntity e) -> this.countryList
                        .add(new CountryResponse(e))
        );
    }

    /**
     * Get response countries list.
     *
     * @return The response countries list.
     */
    public List<CountryResponse> getCountryList() {
        return new ArrayList<>(countryList);
    }

    /**
     * Set response country list.
     *
     * @param value The countries list.
     */
    public void setCountryList(List<CountryResponse> value) {
        this.countryList = new ArrayList<>(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryList);
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
        var other = (CountryListResponse) obj;
        return Objects.equals(countryList, other.countryList);
    }

    @Override
    public String toString() {
        return "CountryListResponse [countries=" + countryList + "]";
    }

}
