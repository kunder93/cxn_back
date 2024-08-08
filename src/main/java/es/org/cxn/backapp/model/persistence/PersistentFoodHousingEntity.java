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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Self vehicle entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "FoodHousing")
@Table(name = "foodhousing")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
  @NonNull
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
  @NonNull
  private Boolean overnight;

  /**
   * The food housing payment sheet.
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_sheet_id", nullable = true)
  @NonNull
  private PersistentPaymentSheetEntity paymentSheet;

  /**
   * Roles associated with this user.
   */
  @OneToMany(mappedBy = "foodHousing")
  @Builder.Default
  private Set<PersistentInvoiceEntity> invoices = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var that = (PersistentFoodHousingEntity) o;
    return Double.compare(that.dayPrice, dayPrice) == 0
          && Objects.equals(id, that.id)
          && Objects.equals(amountDays, that.amountDays)
          && Objects.equals(overnight, that.overnight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amountDays, dayPrice, overnight);
  }
}
