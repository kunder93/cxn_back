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

import es.org.cxn.backapp.model.PaymentSheetEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Operation Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "PaymentSheet")
@Table(name = "paymentsheet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentPaymentSheetEntity implements PaymentSheetEntity {

  /**
   * serial UID.
   */
  @Transient
  private static final long serialVersionUID = -8238192117130392730L;

  /**
   * Payment sheet identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer id;

  /**
   * Event reason of the payment sheet.
   *
   */
  @Column(name = "reason", nullable = false, unique = false)
  @NonNull
  private String reason;

  /**
   * Event place of the payment sheet.
   *
   */
  @Column(name = "place", nullable = false, unique = false)
  @NonNull
  private String place;

  /**
   * Event starting date of the payment sheet.
   *
   */
  @Column(name = "start_date", nullable = false, unique = false)
  @NonNull
  private LocalDate startDate;

  /**
   * Event end date of the payment sheet.
   */
  @Column(name = "end_date", nullable = false, unique = false)
  @NonNull
  private LocalDate endDate;

  /**
   * The payment sheet user owner.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_dni", nullable = false)
  @NonNull
  private PersistentUserEntity userOwner;

  /**
   * Roles associated with this user.
   */
  @OneToMany(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  @Builder.Default
  private Set<PersistentRegularTransportEntity> regularTransports =
        new HashSet<>();

  /**
   * Self vehicle transport if have. Can be null.
   */
  @OneToOne(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  private PersistentSelfVehicleEntity selfVehicle;

  /**
   * Food and housing if have.Can be null.
   */
  @OneToOne(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  private PersistentFoodHousingEntity foodHousing;

}
