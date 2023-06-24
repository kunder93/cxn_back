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

/**
 * Self vehicle entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "SelfVehicle")
@Table(name = "selfvehicle")
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
  private Integer id;

  /**
   * Places where travel with the vehicle.
   *
   */
  @Column(name = "places", nullable = false, unique = false)
  private String places = "";

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
   * Constructs a company entity.
   */
  public PersistentSelfVehicleEntity() {
    super();
  }

  /**
   * Self vehicle entity constructor.
   *
   * @param places       The places.
   * @param distance     The distance.
   * @param kmPrice      The price per kilometer.
   * @param paymentSheet The payment sheet.
   */
  public PersistentSelfVehicleEntity(
        final String places, final float distance, final double kmPrice,
        final PersistentPaymentSheetEntity paymentSheet
  ) {
    super();
    this.places = places;
    this.distance = distance;
    this.kmPrice = kmPrice;
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
   * Get places.
   */
  @Override
  public String getPlaces() {
    return places;
  }

  /**
   * Get distance.
   */
  @Override
  public float getDistance() {
    return distance;
  }

  /**
   * Get price per kilometer.
   */
  @Override
  public double getKmPrice() {
    return kmPrice;
  }

  /**
   * Set identifier.
   */
  @Override
  public void setId(final Integer id) {
    this.id = id;
  }

  /**
   * Set places.
   */
  @Override
  public void setPlaces(final String places) {
    this.places = places;
  }

  /**
   * Set distance.
   */
  @Override
  public void setDistance(final float distance) {
    this.distance = distance;
  }

  /**
   * Set price per kilometer.
   */
  @Override
  public void setKmPrice(final double kmPrice) {
    this.kmPrice = kmPrice;
  }

  /**
   * Get the payment sheet.
   *
   * @return The payment sheet.
   */
  public PersistentPaymentSheetEntity getPaymentSheet() {
    return paymentSheet;
  }

  /**
   * Set the payment sheet.
   *
   * @param paymentSheet The payment sheet.
   */
  public void setPaymentSheet(final PersistentPaymentSheetEntity paymentSheet) {
    this.paymentSheet = paymentSheet;
  }

  /**
   * Hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(distance, id, kmPrice, places);
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
    var other = (PersistentSelfVehicleEntity) obj;
    return Double.doubleToLongBits(distance) == Double
          .doubleToLongBits(other.distance)
          && Objects.equals(id, other.id)
          && Double.doubleToLongBits(kmPrice) == Double
                .doubleToLongBits(other.kmPrice)
          && Objects.equals(places, other.places);
  }

  /**
   * To string.
   */
  @Override
  public String toString() {
    return "PersistentSelfVehicleEntity [id=" + id + ", places=" + places
          + ", distance=" + distance + ", kmPrice=" + kmPrice + "]";
  }

}
