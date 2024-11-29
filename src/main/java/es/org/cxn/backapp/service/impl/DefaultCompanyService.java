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

package es.org.cxn.backapp.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.CompanyServiceException;
import es.org.cxn.backapp.model.CompanyEntity;
import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;
import es.org.cxn.backapp.repository.CompanyEntityRepository;
import es.org.cxn.backapp.service.CompanyService;
import es.org.cxn.backapp.service.UserService;

import java.util.List;

import org.springframework.stereotype.Service;

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
  public static final String COMPANY_EXISTS = "Company already exists.";
  /**
   * Message company not found for exception.
   */
  public static final String COMPANY_NOT_FOUND = "Company not found.";
  /**
   * Message company cannot be deleted for exception.
   */
  public static final String COMPANY_CANNOT_DELETE =
        "Company can not be deleted.";

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
  public PersistentCompanyEntity
        add(final String nif, final String name, final String address)
              throws CompanyServiceException {
    if (Boolean.TRUE.equals(companyRepository.existsByNif(nif))) {
      throw new CompanyServiceException(COMPANY_EXISTS);
    }
    final var company = PersistentCompanyEntity.builder().nif(nif).name(name)
          .address(address).build();
    return companyRepository.save(company);
  }

  @Override
  public PersistentCompanyEntity findById(final String nifCif)
        throws CompanyServiceException {
    final var company = companyRepository.findByNif(nifCif);
    if (company.isEmpty()) {
      throw new CompanyServiceException(COMPANY_NOT_FOUND);
    }
    return company.get();
  }

  @Override
  public void remove(final String nifCif) throws CompanyServiceException {

    final var companyOptional = companyRepository.findByNif(nifCif);
    if (companyOptional.isEmpty()) {
      throw new CompanyServiceException(COMPANY_NOT_FOUND);
    }
    final var numberInvoicesAsBuyer =
          companyOptional.get().getInvoicesAsBuyer().size();
    final var numberInvoicesAsSeller =
          companyOptional.get().getInvoicesAsSeller().size();
    if (numberInvoicesAsBuyer > 0 || numberInvoicesAsSeller > 0) {
      throw new CompanyServiceException(COMPANY_CANNOT_DELETE);
    }
    companyRepository.delete(companyOptional.get());
  }

  @Override
  public List<PersistentCompanyEntity> getCompanies() {
    return companyRepository.findAll();
  }

  @Override
  public CompanyEntity updateCompany(
        final String nif, final String name, final String addresss
  ) throws CompanyServiceException {

    final var company = companyRepository.findByNif(nif);
    if (company.isEmpty()) {
      throw new CompanyServiceException("message");
    } else {
      final var companyEntity = company.get();
      companyEntity.setName(name);
      companyEntity.setAddress(addresss);

      return companyRepository.save(companyEntity);
    }
  }
}
