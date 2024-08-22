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
  private String series;

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
   * Main arguments constructor.
   *
   * @param numberValue             the invoice number.
   * @param seriesValue             the invoice series.
   * @param expeditionDateValue     the invoice expedition date.
   * @param advancePaymentDateValue the advance payment date or null.
   * @param taxExemptValue          the tax exempt.
   * @param sellerComp                 the invoice's company seller.
   * @param buyerComp                   the invoice's company buyer.
   */
  public PersistentInvoiceEntity(
        final int numberValue, final String seriesValue,
        final LocalDate expeditionDateValue,
        final LocalDate advancePaymentDateValue, final Boolean taxExemptValue,
        final PersistentCompanyEntity sellerComp,
        final PersistentCompanyEntity buyerComp
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
          checkNotNull(sellerComp, "Received a null pointer as  seller");
    this.buyer = checkNotNull(buyerComp, "Received a null pointer as buyer");

  }

  /**
   * Compares this object with the specified object for equality.
   * <p>
   * The {@code equals} method implements a comparison of this object
   * with another object. To be considered equal,
   * both objects must be of the same class and have equal values for
   * all fields that are used in the comparison.
   * <p>
   * The general contract of {@code equals} is:
   * <ul>
   *   <li>It is reflexive: for any non-null reference value {@code x},
   *   {@code x.equals(x)} should return {@code true}.</li>
   *   <li>It is symmetric: for any non-null reference values {@code x}
   *   and {@code y}, {@code x.equals(y)} should return
   *       {@code true} if and only if {@code y.equals(x)}
   *       returns {@code true}.</li>
   *   <li>It is transitive: for any non-null reference values {@code x},
   *    {@code y}, and {@code z}, if {@code x.equals(y)}
   *       returns {@code true} and {@code y.equals(z)} returns {@code true},
   *        then {@code x.equals(z)} should return {@code true}.</li>
   *   <li>It is consistent: for any non-null reference values {@code x}
   *   and {@code y}, multiple invocations of {@code x.equals(y)}
   *       consistently return {@code true} or consistently return
   *       {@code false}, provided no information used in equality comparisons
   *       on the object is modified.</li>
   *   <li>For any non-null reference value {@code x}, {@code x.equals(null)}
   *   should return {@code false}.</li>
   * </ul>
   *
   * <p>
   * This implementation considers two {@code PersistentInvoiceEntity} objects
   *  equal if they have the same {@code number},
   * {@code series}, {@code buyer}, and {@code seller} values.
   *
   * <p>
   * Subclasses should override this method to ensure consistency with
   * {@code hashCode}. When overriding {@code equals},
   * make sure to include all fields used in the comparison and follow
   * the general contract of {@code equals}.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the {@code object};
   * {@code false} otherwise
   */
  @Override
  public boolean equals(final Object object) {
    final boolean result;

    if (this == object) {
      result = true;
    } else if (object == null || getClass() != object.getClass()) {
      result = false;
    } else {
      final var that = (PersistentInvoiceEntity) object;
      result = number == that.number && series.equals(that.series)
            && buyer.equals(that.buyer) && seller.equals(that.seller);
    }

    return result;
  }

  /**
   * Returns a hash code value for the object.
   * <p>
   * The {@code hashCode} method must be overridden to ensure that
   * it is consistent with {@link #equals(Object)}.
   * The hash code value should be computed based on the same fields
   *  that are used in the {@code equals} method.
   * <p>
   * The general contract of {@code hashCode} is:
   * <ul>
   *   <li>Whenever it is invoked on the same object more than once
   *   during an execution of an application,
   *       the {@code hashCode} method must consistently return the
   *       same integer, provided no information used
   *       in {@code equals} comparisons on the object is modified.</li>
   *   <li>If two objects are equal according to the {@code equals(Object)}
   *    method, then calling the {@code hashCode}
   *       method on each of the two objects must produce the same
   *       integer result.</li>
   *   <li>It is not required that if two objects are unequal according to
   *    the {@code equals(Object)} method,
   *       then calling the {@code hashCode} method on each of the two objects
   *        must produce distinct integer results.
   *       However, the programmer should be aware that producing distinct
   *        integer results for unequal objects
   *       can improve the performance of hash-based collections.</li>
   * </ul>
   *
   * <p>
   * This implementation computes the hash code based on the {@code number},
   *  {@code series}, {@code buyer}, and
   * {@code seller} fields. Subclasses should override this method to include
   *  all fields that are used in the
   * {@code equals} method to ensure consistency.
   *
   * @return a hash code value for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(number, series, buyer, seller);
  }
}
