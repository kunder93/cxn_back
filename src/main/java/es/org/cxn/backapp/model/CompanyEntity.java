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

package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;

/**
 * A Company entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface CompanyEntity extends Serializable {

    /**
     * Returns the identifier as nif or cif assigned to this company entity.
     *
     * @return the company entity's identifier.
     */
    String getNifCif();

    /**
     * Returns the name of the company.
     *
     * @return the company entity's number.
     */
    String getName();

    /**
     * Get the company identity tax number.
     *
     * @return identity tax number.
     */
    String getIdentityTaxNumber();

    /**
     * Get the company address.
     *
     * @return the company address.
     */
    String getAddress();

    /**
     * Sets the identifier assigned to this company entity.
     *
     * @param value the identifier nif or cif for the company entity.
     */
    void setNifCif(String value);

    /**
     * Changes the name of the company entity.
     *
     * @param value the name to set on the company entity.
     */
    void setName(String value);

    /**
     * Changes the company identity tax number.
     *
     * @param value the new company identity tax number.
     */
    void setIdentityTaxNumber(String value);

    /**
     * Changes the company address.
     *
     * @param value the new company address.
     */
    void setAddress(String value);

    /**
     * Get list of invoices identifiers where this company is Buyer.
     *
     * @return Companies identifiers list.
     */
    List<Integer> getInvoicesAsBuyer();

    /**
     * Add invoices where company is buyer.
     *
     * @param value The invoice to add.
     */
    void addInvoicesAsBuyer(PersistentInvoiceEntity value);

    /**
     * Add invoices where company is seller.
     *
     * @param value The invoice to add.
     */
    void addInvoicesAsSeller(PersistentInvoiceEntity value);

    /**
     * Remove invoice from company's invoice buyer list.
     *
     * @param value The invoice for remove.
     * @return True if remove false if not.
     */
    boolean removeInvoiceAsBuyer(PersistentInvoiceEntity value);

    /**
     * Remove invoice from company's invoice seller list.
     *
     * @param value The invoice for remove.
     * @return True if remove false if not.
     */
    boolean removeInvoiceAsSeller(PersistentInvoiceEntity value);

    /**
     * Get invoices where company is seller.
     *
     * @return List with invoices identifiers.
     */
    List<Integer> getInvoicesAsSeller();

}
