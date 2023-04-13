package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

import es.org.cxn.backapp.model.form.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by controller as request of create company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class CreateCompanyRequestForm implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3133389822212147705L;

    /**
     * The company nif or cif.
     */
    private String nifCif;

    /**
     * The company name.
     */
    @NotBlank(message = Constants.NAME_NOT_BLANK_MESSAGE)
    @Size(
            max = Constants.NAME_MAX_LENGTH, message = Constants.NAME_MAX_LENGTH_MESSAGE
    )
    private String name;

    /**
     * The company identity tax number.
     */
    private String identityTaxNumber;

    /**
     * The company address.
     */
    private String address;

    /**
     * Main arguments constructor.
     *
     * @param nifCif            the company nif or cif.
     * @param name              the company name.
     * @param identityTaxNumber the company identity tax number.
     * @param address           the company address
     */
    public CreateCompanyRequestForm(
            String nifCif, String name, String identityTaxNumber, String address
    ) {
        super();
        this.nifCif = nifCif;
        this.name = name;
        this.identityTaxNumber = identityTaxNumber;
        this.address = address;
    }

    /**
     * Main empty constructor.
     */
    public CreateCompanyRequestForm() {
        super();
    }

    /**
     * Get company cif or nif.
     *
     * @return the company cif or nif.
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Set company cif or nif.
     *
     * @param value The company cif or nif.
     */
    public void setNifCif(String value) {
        this.nifCif = value;
    }

    /**
     * Get company name.
     *
     * @return The company name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set request company name.
     *
     * @param value The request company name.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get request company identity tax number.
     *
     * @return The company identity tax number.
     */
    public String getIdentityTaxNumber() {
        return identityTaxNumber;
    }

    /**
     * Set request company identity tax number.
     *
     * @param value The company identity tax number.
     */
    public void setIdentityTaxNumber(String value) {
        this.identityTaxNumber = value;
    }

    /**
     * Get request company address.
     *
     * @return The company address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set request company address.
     *
     * @param value The company address.
     */
    public void setAddress(String value) {
        this.address = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, identityTaxNumber, name, nifCif);
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
        var other = (CreateCompanyRequestForm) obj;
        return Objects.equals(address, other.address)
                && Objects.equals(identityTaxNumber, other.identityTaxNumber)
                && Objects.equals(name, other.name)
                && Objects.equals(nifCif, other.nifCif);
    }

    @Override
    public String toString() {
        return "CreateCompanyRequestForm [nifCif=" + nifCif + ", name=" + name
                + ", identityTaxNumber=" + identityTaxNumber + ", address="
                + address + "]";
    }

}
