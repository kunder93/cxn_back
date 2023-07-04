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

package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

/**
 * A payment sheet entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface PaymentSheetEntity extends Serializable {

  /**
   * Returns the payment sheet identifier.
   *
   * @return the payment sheet entity's identifier.
   */
  Integer getId();

  /**
   * Returns the reason of the payment sheet event.
   *
   * @return the payment sheet entity's reason.
   */
  String getReason();

  /**
   * Get the payment sheet event place.
   *
   * @return payment sheet event place.
   */
  String getPlace();

  /**
   * Get the payment sheet event start date.
   *
   * @return the payment sheet event start date.
   */
  LocalDate getStartDate();

  /**
   * Get the payment sheet event end date.
   *
   * @return the payment sheet event end date.
   */
  LocalDate getEndDate();

  /**
   * Sets the identifier assigned to this payment sheet entity.
   *
   * @param value the new id of payment sheet entity.
   */
  void setId(Integer value);

  /**
   * Changes the payment sheet event reason.
   *
   * @param value the new event reason of payment sheet.
   */
  void setReason(String value);

  /**
   * Changes the payment sheet event place.
   *
   * @param value the new event place of payment sheet.
   */
  void setPlace(String value);

  /**
   * Changes the payment sheet event end date.
   *
   * @param value the new event end date of the payment sheet.
   */
  void setEndDate(LocalDate value);

  /**
   * Changes the payment sheet event starting date.
   *
   * @param value the new event starting date of the payment sheet.
   */
  void setStartDate(LocalDate value);

  /**
   * Set the payment sheet user owner.
   *
   * @param value The user that owns payment sheet.
   */
  void setUserOwner(PersistentUserEntity value);

  /**
   * Get the payment sheet user owner.
   *
   * @return the payment sheet user owner.
   */
  PersistentUserEntity getUserOwner();

  /**
   * Get the regular transport entities associated with this payment sheet.
   *
   * @return Set with reagular transport entities associated.
   */
  Set<PersistentRegularTransportEntity> getRegularTransports();

  /**
   * @return Self vehicle entity associated with this payment sheet.
   */
  PersistentSelfVehicleEntity getSelfVehicle();

  /**
   * @return Food housing associated with this payment sheet.
   */
  PersistentFoodHousingEntity getFoodHousing();

  /**
   * @param value Set with regular transport entities associated with this
   *              payment sheet.
   */
  void setRegularTransports(Set<PersistentRegularTransportEntity> value);

  /**
   * @param value Self vehicle entity associated with this payment sheet.
   */
  void setSelfVehicle(PersistentSelfVehicleEntity value);

  /**
   * @param value Food housing entity associated with this payment sheet.
   */
  void setFoodHousing(PersistentFoodHousingEntity value);

}
