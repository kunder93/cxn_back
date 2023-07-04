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

import es.org.cxn.backapp.model.FoodHousingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Self vehicle entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "FoodHousing")
@Table(name = "foodhousing")
public class PersistentFoodHousingEntity implements FoodHousingEntity {

  /**
   * serial UID.
   */
  @Transient
  private static final long serialVersionUID = -8275074649983253300L;

  /**
   * Entity's identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer id;

  /**
   * Places where travel with the vehicle.
   *
   */
  @Column(name = "amountdays", nullable = false, unique = false)
  private Integer amountDays;

  /**
   * The food housing day price.
   */
  @Column(name = "dayprice", nullable = false, unique = false)
  private double dayPrice;

  /**
   * The food housing overnight.
   */
  @Column(name = "overnight", nullable = false, unique = false)
  private Boolean overnight;

  /**
   * The food housing payment sheet.
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_sheet_id", nullable = true)
  private PersistentPaymentSheetEntity paymentSheet;

  /**
   * Roles associated with this user.
   */
  @OneToMany(mappedBy = "foodHousing")
  private Set<PersistentInvoiceEntity> invoices = new HashSet<>();

  /**
   * Constructs a company entity.
   */
  public PersistentFoodHousingEntity() {
    super();
  }

  /**
   * Constructs food housing entity.
   *
   * @param amountDays   The amount of days.
   * @param dayPrice     The day price.
   * @param overnight    The overnight
   * @param paymentSheet The payment sheet.
   */
  public PersistentFoodHousingEntity(
        final Integer amountDays, final double dayPrice,
        final Boolean overnight, final PersistentPaymentSheetEntity paymentSheet
  ) {
    super();
    this.amountDays = amountDays;
    this.dayPrice = dayPrice;
    this.overnight = overnight;
    this.paymentSheet = paymentSheet;
  }

  /**
   * Get identifier.
   */
  @Override
  public Integer getId() {
    return id;
  }

  /**
   * Get the amount of days.
   */
  @Override
  public Integer getAmountDays() {
    return amountDays;
  }

  /**
   * Get day price.
   */
  @Override
  public double getDayPrice() {
    return dayPrice;
  }

  /**
   * Get overnight value.
   */
  @Override
  public Boolean getOvernight() {
    return overnight;
  }

  /**
   * Set identifier.
   */
  @Override
  public void setId(final Integer id) {
    this.id = id;
  }

  /**
   * Set the amount of days.
   */
  @Override
  public void setAmountDays(final Integer amountDays) {
    this.amountDays = amountDays;
  }

  /**
   * Set the price per day.
   */
  @Override
  public void setDayPrice(final double dayPrice) {
    this.dayPrice = dayPrice;
  }

  /**
   * Set overnight value.
   */
  @Override
  public void setOvernight(final Boolean overnight) {
    this.overnight = overnight;
  }

  /**
   * Get the payment sheet entity.
   */
  @Override
  public PersistentPaymentSheetEntity getPaymentSheet() {
    return paymentSheet;
  }

  /**
   * Get the invoices list.
   */
  @Override
  public Set<PersistentInvoiceEntity> getInvoices() {
    return new HashSet<>(invoices);
  }

  /**
   * Set payment sheet entity.
   */
  @Override
  public void setPaymentSheet(final PersistentPaymentSheetEntity paymentSheet) {
    this.paymentSheet = paymentSheet;
  }

  /**
   * Set invoices list.
   */
  @Override
  public void setInvoices(final Set<PersistentInvoiceEntity> invoices) {
    this.invoices = new HashSet<>(invoices);
  }

  /**
   * Hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(amountDays, dayPrice, id, overnight);
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
    var other = (PersistentFoodHousingEntity) obj;
    return Objects.equals(amountDays, other.amountDays)
          && Double.doubleToLongBits(dayPrice) == Double
                .doubleToLongBits(other.dayPrice)
          && Objects.equals(id, other.id)
          && Objects.equals(overnight, other.overnight);
  }

  /**
   * To string.
   */
  @Override
  public String toString() {
    return "PersistentFoodHousingEntity [id=" + id + ", amountDays="
          + amountDays + ", dayPrice=" + dayPrice + ", overnight=" + overnight
          + "]";
  }

}
