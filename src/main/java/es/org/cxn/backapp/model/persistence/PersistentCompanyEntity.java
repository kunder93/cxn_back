/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.CompanyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Company Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CompanyEntity")
@Table(name = "companies")
public class PersistentCompanyEntity implements CompanyEntity {

    /**
     * serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8242222114230392000L;

    /**
     * Entity's identifier.
     */
    @Id
    @Column(name = "nif", nullable = false, unique = true)
    private String nif;

    /**
     * name of the company.
     *
     */
    @Column(name = "name", nullable = false, unique = false)
    private String name = "";

    /**
     * Company address
     *
     */
    @Column(name = "address", nullable = false, unique = false)
    private String address;

    /**
     * List of companies that have this company as buyer.
     */
    @OneToMany(mappedBy = "buyer")
    private List<PersistentInvoiceEntity> invoicesAsBuyer = new ArrayList<>();

    /**
     * List with invoices that have this company as seller.
     */
    @OneToMany(mappedBy = "seller")
    private List<PersistentInvoiceEntity> invoicesAsSeller = new ArrayList<>();

    /**
     * Constructs a company entity.
     */
    public PersistentCompanyEntity() {
        super();
    }

    /**
     * Main arguments contructor.
     *
     * @param nifCif            the company cif or nif
     * @param name              the company name
     * @param identityTaxNumber the company identity tax number
     * @param address           the company address
     */
    public PersistentCompanyEntity(
            final String nifCif, final String name, final String address
    ) {
        super();
        this.nif = checkNotNull(
                nifCif, "Received a null pointer as cif or nif"
        );
        this.name = checkNotNull(
                name, "Received a null pointer as company name"
        );
        this.address = checkNotNull(
                address, "Received a null pointer as address"
        );

    }

    @Override
    public String getNif() {
        return nif;
    }

    @Override
    public void setNif(String value) {
        this.nif = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public List<Integer> getInvoicesAsBuyer() {
        var lista = new ArrayList<Integer>();
        invoicesAsBuyer.forEach(
                (PersistentInvoiceEntity invoice) -> lista.add(invoice.getId())
        );
        return lista;
    }

    @Override
    public void addInvoicesAsBuyer(PersistentInvoiceEntity invoiceAsBuyer) {
        this.invoicesAsBuyer.add(invoiceAsBuyer);
    }

    @Override
    public void addInvoicesAsSeller(PersistentInvoiceEntity invoiceAsSeller) {
        this.invoicesAsSeller.add(invoiceAsSeller);
    }

    @Override
    public boolean removeInvoiceAsBuyer(
            PersistentInvoiceEntity invoiceAsBuyer
    ) {
        return this.invoicesAsBuyer.remove(invoiceAsBuyer);
    }

    @Override
    public boolean removeInvoiceAsSeller(
            PersistentInvoiceEntity invoiceAsSeller
    ) {
        return this.invoicesAsSeller.remove(invoiceAsSeller);
    }

    @Override
    public List<Integer> getInvoicesAsSeller() {
        var lista = new ArrayList<Integer>();
        invoicesAsSeller.forEach(
                (PersistentInvoiceEntity invoice) -> lista.add(invoice.getId())
        );
        return lista;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, nif, name);
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
        var other = (PersistentCompanyEntity) obj;
        return Objects.equals(address, other.address)
                && Objects.equals(nif, other.nif)
                && Objects.equals(name, other.name);

    }

    @Override
    public String toString() {
        return "PersistentCompanyEntity [nif=" + nif + ", name=" + name
                + ", address=" + address + "]";
    }

}
