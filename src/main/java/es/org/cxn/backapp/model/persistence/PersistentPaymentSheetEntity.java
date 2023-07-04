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
import java.util.Objects;
import java.util.Set;

/**
 * Operation Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "PaymentSheet")
@Table(name = "paymentsheet")
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
  private String reason = "";

  /**
   * Event place of the payment sheet.
   *
   */
  @Column(name = "place", nullable = false, unique = false)
  private String place = "";

  /**
   * Event starting date of the payment sheet.
   *
   */
  @Column(name = "start_date", nullable = false, unique = false)
  private LocalDate startDate;

  /**
   * Event end date of the payment sheet.
   */
  @Column(name = "end_date", nullable = false, unique = false)
  private LocalDate endDate;

  /**
   * The payment sheet user owner.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_dni", nullable = false)
  private PersistentUserEntity userOwner;

  /**
   * Roles associated with this user.
   */
  @OneToMany(mappedBy = "paymentSheet")
  private Set<PersistentRegularTransportEntity> regularTransports =
        new HashSet<>();

  /**
   * Self vehicle transport if have. Can be null.
   */
  @OneToOne(mappedBy = "paymentSheet")
  private PersistentSelfVehicleEntity selfVehicle;

  /**
   * Food and housing if have.Can be null.
   */
  @OneToOne(mappedBy = "paymentSheet")
  private PersistentFoodHousingEntity foodHousing;

  /**
   * Constructs a company entity.
   */
  public PersistentPaymentSheetEntity() {
    super();
  }

  /**
   * The payment sheet entity constructor.
   *
   * @param reason    The reason.
   * @param place     The place.
   * @param startDate The start date.
   * @param endDate   The end date.
   * @param userOwner The user owner.
   */
  public PersistentPaymentSheetEntity(
        final String reason, final String place, final LocalDate startDate,
        final LocalDate endDate, final PersistentUserEntity userOwner
  ) {
    super();
    this.reason = reason;
    this.place = place;
    this.startDate = startDate;
    this.endDate = endDate;
    this.userOwner = userOwner;
  }

  /**
   * Get identifier.
   */
  @Override
  public Integer getId() {
    return id;
  }

  /**
   * Get the reason.
   */
  @Override
  public String getReason() {
    return reason;
  }

  /**
   * Get the place.
   */
  @Override
  public String getPlace() {
    return place;
  }

  /**
   * Get the start date.
   */
  @Override
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Get the end date.
   */
  @Override
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Get the user owner entity.
   */
  @Override
  public PersistentUserEntity getUserOwner() {
    return userOwner;
  }

  /**
   * Set the identifier.
   */
  @Override
  public void setId(final Integer value) {
    this.id = value;
  }

  /**
   * Set the reason.
   */
  @Override
  public void setReason(final String reason) {
    this.reason = reason;
  }

  /**
   * Set the place.
   */
  @Override
  public void setPlace(final String place) {
    this.place = place;
  }

  /**
   * Set the start date.
   */
  @Override
  public void setStartDate(final LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * Set the end date.
   */
  @Override
  public void setEndDate(final LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   * Set the user owner entity.
   */
  @Override
  public void setUserOwner(final PersistentUserEntity value) {
    this.userOwner = value;
  }

  /**
   * Get the self vehicle entity.
   */
  @Override
  public PersistentSelfVehicleEntity getSelfVehicle() {
    return selfVehicle;
  }

  /**
   * Get the food housing entity.
   */
  @Override
  public PersistentFoodHousingEntity getFoodHousing() {
    return foodHousing;
  }

  /**
   * Set food housing entity.
   */
  @Override
  public void setFoodHousing(final PersistentFoodHousingEntity foodHousing) {
    this.foodHousing = foodHousing;
  }

  /**
   * Get regular transport entity set.
   */
  @Override
  public Set<PersistentRegularTransportEntity> getRegularTransports() {
    return new HashSet<>(regularTransports);
  }

  /**
   * Set regular transport entity set.
   */
  @Override
  public void setRegularTransports(
        final Set<PersistentRegularTransportEntity> regularTransports
  ) {
    this.regularTransports = new HashSet<>(regularTransports);
  }

  /**
   * Set self vehicle entity.
   */
  @Override
  public void setSelfVehicle(final PersistentSelfVehicleEntity selfVehicle) {
    this.selfVehicle = selfVehicle;
  }

  /**
   * Hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(endDate, id, place, reason, startDate);
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
    var other = (PersistentPaymentSheetEntity) obj;
    return Objects.equals(endDate, other.endDate)
          && Objects.equals(id, other.id) && Objects.equals(place, other.place)
          && Objects.equals(reason, other.reason)
          && Objects.equals(startDate, other.startDate);
  }

  /**
   * To string.
   */
  @Override
  public String toString() {
    return "PersistentPaymentSheetEntity [id=" + id + ", reason=" + reason
          + ", place=" + place + ", startDate=" + startDate + ", endDate="
          + endDate + "]";
  }

}
