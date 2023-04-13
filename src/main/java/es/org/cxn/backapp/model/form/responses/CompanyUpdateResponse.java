package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as response of update company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * </p>
 *
 * @author Santiago Paz.
 */
public class CompanyUpdateResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3852186551148263014L;

    /**
     * The company cif or nif.
     */
    private String cifNif;
    /**
     * The company name.
     */
    private String name;
    /**
     * The company address.
     */
    private String address;

    /**
     * The company identity tax number.
     */
    private String identityTaxNumber;

    /**
     * Constructor with provided params.
     *
     * @param cifNif            the company cif or nif.
     * @param name              the company name.
     * @param address           the company address.
     * @param identityTaxNumber the company identityTaxNumber.
     */
    public CompanyUpdateResponse(
            String cifNif, String name, String address, String identityTaxNumber
    ) {
        super();
        this.name = name;
        this.address = address;
        this.cifNif = cifNif;
        this.identityTaxNumber = identityTaxNumber;
    }

    /**
     * Main empty constructor.
     */
    public CompanyUpdateResponse() {
        super();
    }

    /**
     * Get response company cif or nif.
     *
     * @return The company cif or nif.
     */
    public String getCifNif() {
        return cifNif;
    }

    /**
     * Set response company cif or nif.
     *
     * @param value The new company cif or nif.
     */
    public void setCifNif(String value) {
        this.cifNif = value;
    }

    /**
     * Get response company name.
     *
     * @return The company name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set response company name.
     *
     * @param value The company name.
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get response company address.
     *
     * @return The company address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set response company address.
     *
     * @param value The company address.
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Getter for identity tax number.
     *
     * @return The identity tax number.
     */
    public String getIdentityTaxNumber() {
        return identityTaxNumber;
    }

    /**
     * Setter for identity tax number.
     *
     * @param value The identity tax number.
     */
    public void setIdentityTaxNumber(String value) {
        this.identityTaxNumber = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, cifNif, identityTaxNumber, name);
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
        var other = (CompanyUpdateResponse) obj;
        return Objects.equals(address, other.address)
                && Objects.equals(cifNif, other.cifNif)
                && Objects.equals(identityTaxNumber, other.identityTaxNumber)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "CompanyUpdateResponse [cifNif=" + cifNif + ", name=" + name
                + ", address=" + address + ", identityTaxNumber="
                + identityTaxNumber + "]";
    }

}
