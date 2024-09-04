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

import es.org.cxn.backapp.model.CompanyEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Company Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CompanyEntity")
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  @NonNull
  private String nif;

  /**
   * name of the company.
   *
   */
  @Column(name = "name", nullable = false, unique = false)
  @NonNull
  private String name;

  /**
   * Company address.
   *
   */
  @Column(name = "address", nullable = false, unique = false)
  @NonNull
  private String address;

  /**
   * List of companies that have this company as buyer.
   */
  @OneToMany(mappedBy = "buyer")
  @Builder.Default
  private List<PersistentInvoiceEntity> invoicesAsBuyer = new ArrayList<>();

  /**
   * List with invoices that have this company as seller.
   */
  @OneToMany(mappedBy = "seller")
  @Builder.Default
  private List<PersistentInvoiceEntity> invoicesAsSeller = new ArrayList<>();

  /**
   * Get invoices as Buyer.
   */
  @Override
  public List<Integer> getInvoicesAsBuyer() {
    final var lista = new ArrayList<Integer>();
    invoicesAsBuyer.forEach(
          (PersistentInvoiceEntity invoice) -> lista.add(invoice.getId())
    );
    return lista;
  }

  /**
   * Add invoice as buyer.
   */
  @Override
  public void addInvoicesAsBuyer(final PersistentInvoiceEntity invoiceAsBuyer) {
    this.invoicesAsBuyer.add(invoiceAsBuyer);
  }

  /**
   * Add invoice as seller.
   */
  @Override
  public void
        addInvoicesAsSeller(final PersistentInvoiceEntity invoiceAsSeller) {
    this.invoicesAsSeller.add(invoiceAsSeller);
  }

  /**
   * Remove invoice as buyer.
   */
  @Override
  public boolean
        removeInvoiceAsBuyer(final PersistentInvoiceEntity invoiceAsBuyer) {
    return this.invoicesAsBuyer.remove(invoiceAsBuyer);
  }

  /**
   * Remove invoice as seller.
   */
  @Override
  public boolean
        removeInvoiceAsSeller(final PersistentInvoiceEntity invoiceAsSeller) {
    return this.invoicesAsSeller.remove(invoiceAsSeller);
  }

  /**
   * Get invoice as seller.
   */
  @Override
  public List<Integer> getInvoicesAsSeller() {
    final var lista = new ArrayList<Integer>();
    invoicesAsSeller.forEach(
          (PersistentInvoiceEntity invoice) -> lista.add(invoice.getId())
    );
    return lista;
  }

}
