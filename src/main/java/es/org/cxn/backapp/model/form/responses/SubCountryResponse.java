package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

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
public class SubCountryResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3133052826313337705L;

    private String name;
    private String kindSubdivisionName;
    private String code;

    public String getName() {
        return name;
    }

    public String getKindSubdivisionName() {
        return kindSubdivisionName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setKindSubdivisionName(String kindSubdivisionName) {
        this.kindSubdivisionName = kindSubdivisionName;
    }

    public SubCountryResponse(
            PersistentCountrySubdivisionEntity countrySubdivision
    ) {
        super();
        this.name = countrySubdivision.getName();
        this.kindSubdivisionName = countrySubdivision.getKindSubdivisionName();
        this.code = countrySubdivision.getCode();
    }

}
