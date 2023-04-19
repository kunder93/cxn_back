package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

/**
 * Represents the form used by controller as response for requesting one
 * company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class SubCountryListResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3133052899993337705L;

    private List<SubCountryResponse> subCountryList = new ArrayList<>();

    /**
     * @param country
     * @param name
     * @param kind_subdivision_name
     */
    public SubCountryListResponse(PersistentCountryEntity country) {
        super();
        if (country.getSubdivisions().isEmpty()) {

        } else {
            country.getSubdivisions()
                    .forEach((PersistentCountrySubdivisionEntity cs) -> {
                        subCountryList.add(new SubCountryResponse(cs));

                    });
        }
    }

    public List<SubCountryResponse> getSubCountryList() {
        return subCountryList;
    }

    public void setSubCountryList(List<SubCountryResponse> subCountryList) {
        this.subCountryList = subCountryList;
    }

}
