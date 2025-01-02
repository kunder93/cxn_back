/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model.persistence;

import java.util.ArrayList;
import java.util.List;

import es.org.cxn.backapp.model.CompanyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Company Entity.
 * <p>
 * This class represents a company entity in the system, utilizing JPA
 * annotations for persistence. It includes fields for company identifier (NIF),
 * name, address, and lists of invoices where the company is either a buyer or
 * seller.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CompanyEntity")
@Table(name = "companies")
@Data
@AllArgsConstructor
@Builder
public class PersistentCompanyEntity implements CompanyEntity {

    /**
     * Serial UID for serialization.
     */
    @Transient
    private static final long serialVersionUID = -8242222114230392000L;

    /**
     * Entity's identifier, typically a tax identification number (NIF).
     */
    @Id
    @Column(name = "nif", nullable = false, unique = true)
    @NonNull
    private String nif;

    /**
     * The name of the company.
     */
    @Column(name = "name", nullable = false, unique = false)
    @NonNull
    private String name;

    /**
     * The address of the company.
     */
    @Column(name = "address", nullable = false, unique = false)
    @NonNull
    private String address;

    /**
     * List of invoices where this company is the buyer.
     */
    @OneToMany(mappedBy = "buyer")
    @Builder.Default
    private List<PersistentInvoiceEntity> invoicesAsBuyer = new ArrayList<>();

    /**
     * List of invoices where this company is the seller.
     */
    @OneToMany(mappedBy = "seller")
    @Builder.Default
    private List<PersistentInvoiceEntity> invoicesAsSeller = new ArrayList<>();

    /**
     * Default constructor for the entity.
     * <p>
     * Required by JPA. Initializes the invoices lists.
     * </p>
     */
    public PersistentCompanyEntity() {
        this.invoicesAsBuyer = new ArrayList<>();
        this.invoicesAsSeller = new ArrayList<>();
    }

    /**
     * Adds an invoice to the list of invoices where this company is the buyer.
     *
     * @param invoiceAsBuyer The invoice to add.
     */
    @Override
    public void addInvoicesAsBuyer(final PersistentInvoiceEntity invoiceAsBuyer) {
        this.invoicesAsBuyer.add(invoiceAsBuyer);
    }

    /**
     * Adds an invoice to the list of invoices where this company is the seller.
     *
     * @param invoiceAsSeller The invoice to add.
     */
    @Override
    public void addInvoicesAsSeller(final PersistentInvoiceEntity invoiceAsSeller) {
        this.invoicesAsSeller.add(invoiceAsSeller);
    }

    /**
     * Gets a list of invoice IDs where this company is the buyer.
     *
     * @return A list of invoice IDs.
     */
    @Override
    public List<Integer> getInvoicesAsBuyer() {
        final var lista = new ArrayList<Integer>();
        invoicesAsBuyer.forEach((PersistentInvoiceEntity invoice) -> lista.add(invoice.getIdentifier()));
        return lista;
    }

    /**
     * Gets a list of invoice IDs where this company is the seller.
     *
     * @return A list of invoice IDs.
     */
    @Override
    public List<Integer> getInvoicesAsSeller() {
        final var lista = new ArrayList<Integer>();
        invoicesAsSeller.forEach((PersistentInvoiceEntity invoice) -> lista.add(invoice.getIdentifier()));
        return lista;
    }

    /**
     * Removes an invoice from the list of invoices where this company is the buyer.
     *
     * @param invoiceAsBuyer The invoice to remove.
     * @return true if the invoice was removed, false otherwise.
     */
    @Override
    public boolean removeInvoiceAsBuyer(final PersistentInvoiceEntity invoiceAsBuyer) {
        return this.invoicesAsBuyer.remove(invoiceAsBuyer);
    }

    /**
     * Removes an invoice from the list of invoices where this company is the
     * seller.
     *
     * @param invoiceAsSeller The invoice to remove.
     * @return true if the invoice was removed, false otherwise.
     */
    @Override
    public boolean removeInvoiceAsSeller(final PersistentInvoiceEntity invoiceAsSeller) {
        return this.invoicesAsSeller.remove(invoiceAsSeller);
    }

}
