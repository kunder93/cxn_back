package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Represents the form used by controller as response for requesting one
 * country data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
public class CountryResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3132389826561387705L;

    /**
     * The country short name.
     */
    private String shortName;
    /**
     * The country full name.
     */
    private String fullName;
    /**
     * The country numeric code.
     */
    private Integer numericCode;
    /**
     * The country alpha 2 code.
     */
    private String alpha2Code;

    /**
     * The country alpha 3 code.
     */
    private String alpha3Code;

    /**
     * Constructor with country entity.
     *
     * @param country the country entity for get values.
     */
    public CountryResponse(PersistentCountryEntity country) {
        super();
        shortName = country.getShortName();
        fullName = country.getFullName();
        numericCode = country.getNumericCode();
        alpha2Code = country.getAlpha2Code();
        alpha3Code = country.getAlpha3Code();

    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

}
