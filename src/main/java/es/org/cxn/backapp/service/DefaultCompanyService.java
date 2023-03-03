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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.exceptions.CompanyServiceException;
import es.org.cxn.backapp.model.CompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.repository.CompanyEntityRepository;

/**
 * Default implementation of the {@link UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultCompanyService implements CompanyService {

    /**
     * Message company already exists for exception.
     */
    public static final String COMPANY_EXISTS_MESSAGE = "Company already exists.";
    /**
     * Message company not found for exception.
     */
    public static final String COMPANY_NOT_FOUND_MESSAGE = "Company not found.";
    /**
     * Message company cannot be deleted for exception.
     */
    public static final String COMPANY_CANNOT_DELETE_MESSAGE = "Company can not be deleted.";

    /**
     * Repository for the invoice entities handled by the service.
     */
    private final CompanyEntityRepository companyRepository;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repoComp The company repository{@link CompanyEntityRepository}
     */
    public DefaultCompanyService(final CompanyEntityRepository repoComp) {
        super();

        companyRepository = checkNotNull(
                repoComp, "Received a null pointer as company repository"
        );
    }

    @Override
    public PersistentCompanyEntity add(
            String cifnif, String name, String identityTaxNumber, String address
    ) throws CompanyServiceException {
        if (Boolean.TRUE.equals(companyRepository.existsByCifnif(cifnif))) {
            throw new CompanyServiceException(COMPANY_EXISTS_MESSAGE);
        }
        if (Boolean.TRUE.equals(
                companyRepository.existsByIdentityTaxNumber(identityTaxNumber)
        )) {
            throw new CompanyServiceException(COMPANY_EXISTS_MESSAGE);
        }
        var company = new PersistentCompanyEntity(
                cifnif, name, identityTaxNumber, address
        );
        return companyRepository.save(company);
    }

    @Override
    public PersistentCompanyEntity findById(String nifCif)
            throws CompanyServiceException {
        var company = companyRepository.findByCifnif(nifCif);
        if (company.isEmpty()) {
            throw new CompanyServiceException(COMPANY_NOT_FOUND_MESSAGE);
        }
        return company.get();
    }

    @Override
    public void remove(String nifCif) throws CompanyServiceException {

        var companyOptional = companyRepository.findByCifnif(nifCif);
        if (companyOptional.isEmpty()) {
            throw new CompanyServiceException(COMPANY_NOT_FOUND_MESSAGE);
        }
        var numberInvoicesAsBuyer = companyOptional.get().getInvoicesAsBuyer()
                .size();
        var numberInvoicesAsSeller = companyOptional.get().getInvoicesAsSeller()
                .size();
        if (numberInvoicesAsBuyer > 0 || numberInvoicesAsSeller > 0) {
            throw new CompanyServiceException(COMPANY_CANNOT_DELETE_MESSAGE);
        }
        companyRepository.delete(companyOptional.get());
    }

    @Override
    public List<PersistentCompanyEntity> getCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public CompanyEntity updateCompany(
            String nifCif, String name, String addresss,
            String identityTaxNumber
    ) throws CompanyServiceException {

        var company = companyRepository.findByCifnif(nifCif);
        if (company.isEmpty()) {
            throw new CompanyServiceException("message");
        } else {
            var companyEntity = company.get();
            companyEntity.setName(name);
            companyEntity.setAddress(addresss);
            companyEntity.setIdentityTaxNumber(identityTaxNumber);

            return companyRepository.save(companyEntity);
        }
    }
}
