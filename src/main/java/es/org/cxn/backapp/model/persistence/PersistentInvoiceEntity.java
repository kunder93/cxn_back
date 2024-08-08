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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Invoice Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "InvoiceEntity")
@Table(name = "invoices")
@Data
@NoArgsConstructor
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
        final LocalDate advancePaymentDateValue, final Boolean taxExemptValue,
        final PersistentCompanyEntity seller,
        final PersistentCompanyEntity buyer
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
    this.seller =
          checkNotNull(seller, "Received a null pointer as tax  seller");
    this.buyer = checkNotNull(buyer, "Received a null pointer as tax  seller");

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var that = (PersistentInvoiceEntity) o;
    return number == that.number && series.equals(that.series)
          && buyer.equals(that.buyer) && seller.equals(that.seller);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, series, buyer, seller);
  }
}
