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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.CompanyEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
   * Company address.
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
   * Company entity constructor.
   *
   * @param nif     The nif.
   * @param name    The name.
   * @param address The address.
   */
  public PersistentCompanyEntity(
        final String nif, final String name, final String address
  ) {
    super();
    this.nif = checkNotNull(nif, "Received a null pointer as cif or nif");
    this.name = checkNotNull(name, "Received a null pointer as company name");
    this.address = checkNotNull(address, "Received a null pointer as address");

  }

  /**
   * Get NIF.
   */
  @Override
  public String getNif() {
    return nif;
  }

  /**
   * Set NIF.
   */
  @Override
  public void setNif(final String value) {
    this.nif = value;
  }

  /**
   * Get name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set name.
   */
  @Override
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Get address.
   */
  @Override
  public String getAddress() {
    return address;
  }

  /**
   * Set address.
   */
  @Override
  public void setAddress(final String address) {
    this.address = address;
  }

  /**
   * Get invoices as Buyer.
   */
  @Override
  public List<Integer> getInvoicesAsBuyer() {
    var lista = new ArrayList<Integer>();
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
    var lista = new ArrayList<Integer>();
    invoicesAsSeller.forEach(
          (PersistentInvoiceEntity invoice) -> lista.add(invoice.getId())
    );
    return lista;
  }

  /**
   * Hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(address, nif, name);
  }

  /**
   * Equals.
   */
  @Override
  public boolean equals(final Object obj) {
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
          && Objects.equals(nif, other.nif) && Objects.equals(name, other.name);

  }

  /**
   * To string.
   */
  @Override
  public String toString() {
    return "PersistentCompanyEntity [nif=" + nif + ", name=" + name
          + ", address=" + address + "]";
  }

}
