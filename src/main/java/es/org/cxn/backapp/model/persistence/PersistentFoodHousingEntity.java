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

  /**
   * Compares this object with the specified object for equality.
   * <p>
   * This implementation of {@code equals} checks if the object being
   * compared is the same instance as this object.
   * If not, it checks if the object is of the same class and then
   * compares relevant fields for equality.
   * <p>
   * The general contract of {@code equals} is:
   * <ul>
   *   <li>It is reflexive: for any non-null reference value {@code x},
   *   {@code x.equals(x)} should return {@code true}.</li>
   *   <li>It is symmetric: for any non-null reference values {@code x}
   *    and {@code y}, {@code x.equals(y)} should return
   *       {@code true} if and only if {@code y.equals(x)}
   *        returns {@code true}.</li>
   *   <li>It is transitive: for any non-null reference values {@code x},
   *    {@code y}, and {@code z}, if {@code x.equals(y)}
   *       returns {@code true} and {@code y.equals(z)} returns {@code true},
   *       then {@code x.equals(z)} should return {@code true}.</li>
   *   <li>It is consistent: for any non-null reference values {@code x} and
   *    {@code y}, multiple invocations of {@code x.equals(y)}
   *       consistently return {@code true} or consistently return
   *        {@code false}, provided no information used in equality comparisons
   *       on the object is modified.</li>
   *   <li>For any non-null reference value {@code x}, {@code x.equals(null)}
   *    should return {@code false}.</li>
   * </ul>
   *
   * <p>
   * In this implementation, two {@code PersistentFoodHousingEntity}
   * objects are considered equal if they have the same {@code id},
   * {@code amountDays}, {@code dayPrice}, and {@code overnight} values.
   * <p>
   * If this class is subclassed, ensure that the subclass overrides
   *  {@code equals} to maintain consistency with {@code hashCode}
   * and to include any additional fields used for comparison.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the
   *  {@code object}; {@code false} otherwise
   */
  @Override
  public boolean equals(final Object object) {
    boolean isEqual;

    // Check if the object is compared to itself
    if (this == object) {
      isEqual = true;
    } else if (object != null && getClass() == object.getClass()) {
      // Cast the object to the correct type
      final var that = (PersistentFoodHousingEntity) object;

      // Compare the relevant fields
      isEqual = Double.compare(that.dayPrice, dayPrice) == 0
            && Objects.equals(id, that.id)
            && Objects.equals(amountDays, that.amountDays)
            && Objects.equals(overnight, that.overnight);
    } else {
      // Cover the case where object is either null or of a different class
      isEqual = false;
    }

    return isEqual;
  }

  /**
   * Returns a hash code value for the object.
   * <p>
   * The {@code hashCode} method returns an integer hash code value computed
   *  using the {@code id}, {@code amountDays},
   * {@code dayPrice}, and {@code overnight} fields. This method must be
   *  consistent with {@link #equals(Object)}:
   * if two objects are considered equal by the {@code equals} method,
   *  they must return the same hash code value.
   * <p>
   * The general contract of {@code hashCode} is:
   * <ul>
   *   <li>Whenever it is invoked on the same object more than once
   *    during an execution of an application,
   *       the {@code hashCode} method must consistently return the
   *        same integer, provided no information used
   *       in {@code equals} comparisons on the object is modified.</li>
   *   <li>If two objects are equal according to the {@code equals(Object)}
   *    method, then calling the {@code hashCode}
   *       method on each of the two objects must produce the
   *        same integer result.</li>
   *   <li>It is not required that if two objects are unequal according to
   *    the {@code equals(Object)} method,
   *       then calling the {@code hashCode} method on each of the two
   *        objects must produce distinct integer results.
   *       However, producing distinct integer results for unequal objects
   *       can improve the performance of hash-based collections.</li>
   * </ul>
   *
   * <p>
   * This implementation calculates the hash code based on the {@code id},
   *  {@code amountDays}, {@code dayPrice}, and
   * {@code overnight} fields. Ensure that any subclass also overrides
   *  {@code hashCode} to include all fields that are used
   * in the {@code equals} method to maintain consistency.
   *
   * @return a hash code value for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, amountDays, dayPrice, overnight);
  }
}
