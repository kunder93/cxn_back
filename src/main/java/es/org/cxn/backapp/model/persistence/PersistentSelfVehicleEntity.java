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

import es.org.cxn.backapp.model.SelfVehicleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;

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
@Entity(name = "SelfVehicle")
@Table(name = "selfvehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentSelfVehicleEntity implements SelfVehicleEntity {

  /**
   * serial UID.
   */
  @Transient
  private static final long serialVersionUID = -8299022119988393300L;

  /**
   * Entity's identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer identifier;

  /**
   * Places where travel with the vehicle.
   *
   */
  @Column(name = "places", nullable = false, unique = false)
  @NonNull
  private String places;

  /**
   * Distance of travels.
   *
   */
  @Column(name = "distance", nullable = false, unique = false)
  private float distance;

  /**
   * Price per kilometer in euros.
   *
   */
  @Column(name = "km_price", nullable = false, unique = false)
  private double kmPrice;

  /**
   * Payment sheet entity associated.
   */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_sheet_id", nullable = false)
  private PersistentPaymentSheetEntity paymentSheet;

  /**
   * Compares this object with the specified object for equality.
   * <p>
   * This method compares the current object with the specified object
   * to determine if they are equal. Two objects are considered equal if
   * they are of the same class and all relevant fields are equal.
   * <p>
   * If this class is subclassed, ensure that the subclass overrides
   * this method appropriately. The subclass should include all fields
   * in the equality check that are relevant for the equality of instances.
   * <p>
   * The general contract for {@code equals} is:
   * <ul>
   *   <li>Reflexive: For any non-null reference value {@code x},
   *       {@code x.equals(x)} should return {@code true}.</li>
   *   <li>Symmetric: For any non-null reference values {@code x} and
   *       {@code y}, {@code x.equals(y)} should return {@code true}
   *       if and only if
   *       {@code y.equals(x)} returns {@code true}.</li>
   *   <li>Transitive: For any non-null reference values {@code x}, {@code y},
   *       and {@code z}, if {@code x.equals(y)} returns {@code true} and
   *       {@code y.equals(z)} returns {@code true}, then {@code x.equals(z)}
   *       should return {@code true}.</li>
   *   <li>Consistent: For any non-null reference values {@code x}
   *   and {@code y},
   *       multiple invocations of {@code x.equals(y)} consistently
   *        return {@code true}
   *       or {@code false}, provided no information used
   *       in {@code equals} comparisons
   *       on the objects is modified.</li>
   *   <li>For any non-null reference value {@code x}, {@code x.equals(null)}
   *       should return {@code false}.</li>
   * </ul>
   *
   * <p>
   * The method should be overridden in subclasses to ensure that
   * all relevant fields
   * are compared and that the {@link #hashCode()} method is also
   * overridden consistently.
   *
   * @param obj the object to compare this instance with
   * @return {@code true} if this object is the same as the
   * {@code obj} argument;
   * {@code false} otherwise
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    final var that = (PersistentSelfVehicleEntity) obj;
    return Float.compare(that.distance, distance) == 0
          && Double.compare(that.kmPrice, kmPrice) == 0
          && Objects.equals(identifier, that.identifier)
          && Objects.equals(places, that.places);
  }

  /**
   * Returns a hash code value for the object.
   * <p>
   * The {@code hashCode} method must be overridden to ensure consistency
   *  with the {@link #equals(Object)}
   * method. The hash code value should be computed based on the same fields
   *  that are used in the {@code equals}
   * method to ensure that equal objects have the same hash code.
   * <p>
   * The general contract of {@code hashCode} is:
   * <ul>
   *   <li>Whenever it is invoked on the same object more than once during
   *   an execution of an application,
   *       the {@code hashCode} method must consistently return the same
   *       integer, provided no information used
   *       in {@code equals} comparisons on the object is modified.</li>
   *   <li>If two objects are equal according to the {@code equals(Object)}
   *    method, then calling the {@code hashCode}
   *       method on each of the two objects must produce the same
   *       integer result.</li>
   *   <li>It is not required that if two objects are unequal according
   *   to the {@code equals(Object)} method,
   *       then calling the {@code hashCode} method on each of the two
   *        objects must produce distinct integer results.
   *       However, the programmer should be aware that producing distinct
   *        integer results for unequal objects
   *       can improve the performance of hash-based collections.</li>
   * </ul>
   *
   * <p>
   * Subclasses should override this method to ensure that the hash code
   * is consistent with the {@code equals} method.
   * When overriding this method in a subclass, make sure to include all
   * fields that are used in the {@code equals}
   * method.
   *
   * @return a hash code value for this object
   */
  @Override
  public int hashCode() {
    return Objects.hash(identifier, places, distance, kmPrice);
  }

}
