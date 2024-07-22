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
  @NonNull
  private PersistentPaymentSheetEntity paymentSheet;
}
