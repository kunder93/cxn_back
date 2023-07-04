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

import es.org.cxn.backapp.model.InvoiceEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Invoice Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "InvoiceEntity")

@Table(name = "invoices")
public class PersistentInvoiceEntity implements InvoiceEntity {

  /**
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 8787424785678537331L;

  /**
   * Invoice identifier id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer id = -1;

  /**
   * Number of the invoice.
   *
   */
  @Column(name = "number", nullable = false, unique = false)
  private int number;

  /**
   * Series of the invoice.
   *
   */
  @Column(name = "series", nullable = false, unique = false)
  private String series = "";

  /**
   * Expedition date of the invoice.
   *
   */
  @Temporal(TemporalType.DATE)
  @Column(name = "expedition_date", nullable = false, unique = false)
  private LocalDate expeditionDate;

  /**
   * Advance payment date of the invoice.
   *
   */
  @Temporal(TemporalType.DATE)
  @Column(name = "advance_payment_date", nullable = true, unique = false)
  private LocalDate advancePaymentDate;

  /**
   * The invoice Tax exempt.
   *
   */
  @Column(name = "tax_exempt", nullable = false, unique = false)
  private Boolean taxExempt;

  /**
   * Roles associated with this user.
   */
  @OneToMany(mappedBy = "invoice")
  private Set<PersistentOperationEntity> operations = new HashSet<>();

  /**
   * The seller company.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_nif", nullable = false)
  private PersistentCompanyEntity seller;

  /**
   * The buyer company.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_nif", nullable = false)
  private PersistentCompanyEntity buyer;

  /**
   * Invoice's associated regular transport.
   */
  @OneToOne(mappedBy = "transportInvoice")
  private PersistentRegularTransportEntity regularTransport;

  /**
   * The payment sheet user owner.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "food_housing_id", nullable = true)
  private PersistentFoodHousingEntity foodHousing;

  /**
   * Constructs an example entity.
   */
  public PersistentInvoiceEntity() {
    super();
  }

  /**
   * Main arguments contructor.
   *
   * @param numberValue             the invoice number
   * @param seriesValue             the invoice series
   * @param expeditionDateValue     the invoice expedition date
   * @param advancePaymentDateValue the advance payment date or null
   * @param taxExemptValue          the tax exempt
   */
  public PersistentInvoiceEntity(
        final int numberValue, final String seriesValue,
        final LocalDate expeditionDateValue,
        final LocalDate advancePaymentDateValue, final Boolean taxExemptValue

  ) {
    super();
    this.number = checkNotNull(
          numberValue, "Received a null pointer as invoice number"
    );
    this.series =
          checkNotNull(seriesValue, "Received a null pointer as series value");
    this.expeditionDate = checkNotNull(
          expeditionDateValue, "Received a null pointer as expedition Date"
    );

    this.advancePaymentDate = advancePaymentDateValue;
    this.taxExempt = checkNotNull(
          taxExemptValue, "Received a null pointer as tax  Exempt"
    );

  }

  /**
   * Get the invoices seller.
   *
   * @return the invoice seller company.
   */
  public PersistentCompanyEntity getSeller() {
    return seller;
  }

  /**
   * Set seller invoice.
   *
   * @param seller The invoice seller.
   */
  public void setSeller(final PersistentCompanyEntity seller) {
    this.seller = seller;
  }

  /**
   * Get the invoice identifier.
   *
   * @return The invoice identifier.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Set the invoice identifier.
   *
   * @param id The invoice identifier.
   */
  public void setId(final Integer id) {
    this.id = id;
  }

  /**
   * Get the invoice company buyer.
   *
   * @return the invoice buyer.
   */
  public PersistentCompanyEntity getBuyer() {
    return buyer;
  }

  /**
   * Set the invoices buyer.
   *
   * @param buyer The buyer company.
   */
  public void setBuyer(final PersistentCompanyEntity buyer) {
    this.buyer = buyer;
  }

  /**
   * Get number.
   */
  @Override
  public int getNumber() {
    return number;
  }

  /**
   * Get series.
   */
  @Override
  public String getSeries() {
    return series;
  }

  /**
   * Get expedition date.
   */
  @Override
  public LocalDate getExpeditionDate() {
    return expeditionDate;
  }

  /**
   * Get advance payment date.
   */
  @Override
  public LocalDate getAdvancePaymentDate() {
    return advancePaymentDate;
  }

  /**
   * Get tax exempt value.
   */
  @Override
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * Get operation entity set.
   */
  @Override
  public Set<PersistentOperationEntity> getOperations() {
    return new HashSet<>(operations);
  }

  /**
   * Set number.
   */
  @Override
  public void setNumber(final int value) {
    number = checkNotNull(value, "Received a null pointer as number");

  }

  /**
   * Set series.
   */
  @Override
  public void setSeries(final String value) {
    series = checkNotNull(value, "Received a null pointer as series");

  }

  /**
   * Set expedition date.
   */
  @Override
  public void setExpeditionDate(final LocalDate value) {
    expeditionDate =
          checkNotNull(value, "Received a null pointer as expedition date");

  }

  /**
   * Set advance payment date.
   */
  @Override
  public void setAdvancePaymentDate(final LocalDate value) {
    advancePaymentDate = value;

  }

  /**
   * Set tax exempt.
   */
  @Override
  public void setTaxExempt(final Boolean value) {
    taxExempt = checkNotNull(value, "Received a null pointer as tax exempt");

  }

  /**
   * Set operations entity set.
   */
  @Override
  public void setOperations(final Set<PersistentOperationEntity> value) {
    operations = new HashSet<>(value);

  }

  /**
   * Gets regular transport entity associated.
   *
   * @return The regular transport entity.
   */
  public PersistentRegularTransportEntity getRegularTransport() {
    return regularTransport;
  }

  /**
   * Set regular transport association.
   *
   * @param regularTransport The regular transport entity.
   */
  public void setRegularTransport(
        final PersistentRegularTransportEntity regularTransport
  ) {
    this.regularTransport = regularTransport;
  }

  /**
   * Gets food housing entity associated.
   *
   * @return The food housing entity.
   */
  public PersistentFoodHousingEntity getFoodHousing() {
    return foodHousing;
  }

  /**
   * Set food housing associated entity.
   *
   * @param foodHousing The food housing entity.
   */
  public void setFoodHousing(final PersistentFoodHousingEntity foodHousing) {
    this.foodHousing = foodHousing;
  }

  /**
   * Hash code.
   */
  @Override
  public int hashCode() {
    return Objects
          .hash(advancePaymentDate, expeditionDate, number, series, taxExempt);
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
    var other = (PersistentInvoiceEntity) obj;
    return Objects.equals(advancePaymentDate, other.advancePaymentDate)
          && Objects.equals(expeditionDate, other.expeditionDate)
          && number == other.number && Objects.equals(series, other.series)
          && Objects.equals(taxExempt, other.taxExempt);
  }

}
