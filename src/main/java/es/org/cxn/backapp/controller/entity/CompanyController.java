/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.CompanyServiceException;
import es.org.cxn.backapp.model.form.requests.CompanyUpdateRequestForm;
import es.org.cxn.backapp.model.form.requests.CreateCompanyRequestForm;
import es.org.cxn.backapp.model.form.responses.CompanyListResponse;
import es.org.cxn.backapp.model.form.responses.CompanyResponse;
import es.org.cxn.backapp.model.form.responses.CompanyUpdateResponse;
import es.org.cxn.backapp.service.CompanyService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest controller for companies.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/company")
public class CompanyController {

  /**
   * The company service.
   */
  private final CompanyService companyService;

  /**
   * Constructs a controller with the specified dependencies.
   *
   * @param service company service.
   */
  public CompanyController(final CompanyService service) {
    super();

    companyService =
          checkNotNull(service, "Received a null pointer as service");
  }

  /**
   * Returns all stored companies.
   *
   * @return all stored companies.
   */
  //  @PreAuthorize("hasRole('PRESIDENTE')")
  //  @PreAuthorize("isAuthenticated()")
  @GetMapping()
  public ResponseEntity<CompanyListResponse> getAllCompanies() {
    final var companiesList = companyService.getCompanies();
    return new ResponseEntity<>(
          new CompanyListResponse(companiesList), HttpStatus.OK
    );
  }

  /**
   * Create a new company.
   *
   * @param createCompanyRequestForm form with data to create company.
   *                                 {@link CreateCompanyRequestForm}.
   * @return form with the created company data.
   */
  @PostMapping()
  public ResponseEntity<CompanyResponse> createCompany(@RequestBody @Valid
  final CreateCompanyRequestForm createCompanyRequestForm) {
    try {
      final var result = companyService.add(
            createCompanyRequestForm.getNif(),
            createCompanyRequestForm.getName(),
            createCompanyRequestForm.getAddress()
      );
      final var response = new CompanyResponse(result);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (CompanyServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Delete an existing company.
   *
   * @param nif contains company identifier
   *
   * @return Ok or error.
   */
  @DeleteMapping("/{nif}")
  public ResponseEntity<Boolean> deleteCompany(@PathVariable
  final String nif) {
    try {
      companyService.remove(nif);
    } catch (CompanyServiceException e) {

      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Update an existing company.
   *
   * @param nif         the company nif.
   * @param requestForm the new data for update company.
   * @return The company with data updated.
   */
  @PutMapping("/{nif}")
  public ResponseEntity<CompanyUpdateResponse> updateCompany(@PathVariable
  final String nif, @RequestBody
  final CompanyUpdateRequestForm requestForm) {
    try {
      final var companyUpdated = companyService.updateCompany(
            nif, requestForm.getName(), requestForm.getAddress()

      );
      final var response = new CompanyUpdateResponse(
            companyUpdated.getNif(), companyUpdated.getName(),
            companyUpdated.getAddress()

      );
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (CompanyServiceException e) {

      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }

  }
}
