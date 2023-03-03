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

package es.org.cxn.backapp.service;

import java.util.List;

import es.org.cxn.backapp.exceptions.CompanyServiceException;
import es.org.cxn.backapp.model.CompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

/**
 * Service for the Company entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface CompanyService {

    /**
     * Creates a new company entity.
     *
     * @param nifCif            The company nif or cif.
     * @param name              The company name.
     * @param identityTaxNumber The company identity tax number.
     * @param address           The company address.
     * @return The Company entity persisted.
     * @throws CompanyServiceException When already exists company with same
     *                                 nif-cif or Identity tax number.
     */
    PersistentCompanyEntity add(
            String nifCif, String name, String identityTaxNumber, String address
    ) throws CompanyServiceException;

    /**
     * Returns an company with the given identifier (id).
     * <p>
     * If no instance exists with that id then an exception is thrown.
     *
     * @param nifCif The company identifier aka nifCif.
     * @return the company entity for the given identifier.
     * @throws CompanyServiceException when company with provided cifNif not
     *                                 exists.
     */
    CompanyEntity findById(String nifCif) throws CompanyServiceException;

    /**
     * Removes an company from persistence.
     *
     * @param nifCif company identifier. .
     * @throws CompanyServiceException When company with provided nifCif not
     *                                 found or When company cannot be delete
     *                                 cause is used in invoices.
     */
    void remove(String nifCif) throws CompanyServiceException;

    /**
     * Get all companies.
     *
     * @return all companies.
     */
    List<PersistentCompanyEntity> getCompanies();

    /**
     * Update a company with new data.
     * <p>
     * If no instance exists with that id then an exception is thrown.
     *
     * @param nifCif            The company identifier aka nifCif.
     * @param name              The company name.
     * @param address           The company address.
     * @param identityTaxNumber The company identity tax number.
     * @return the company entity for the given identifier.
     * @throws CompanyServiceException When company with provided cifNif not
     *                                 exists.
     */
    CompanyEntity updateCompany(
            String nifCif, String name, String address, String identityTaxNumber
    ) throws CompanyServiceException;

}
