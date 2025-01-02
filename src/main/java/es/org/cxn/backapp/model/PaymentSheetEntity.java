
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a payment sheet that records details about a specific payment event.
 * This interface provides methods to access and modify information related to
 * the payment sheet, including its identifier, reason, place, dates,
 * and associated entities.
 *
 * @author Santiago Paz Perez
 */
public interface PaymentSheetEntity extends Serializable {

  /**
   * Retrieves the unique identifier of the payment sheet.
   *
   * @return The identifier of the payment sheet, or {@code null} if not set.
   */
  Integer getId();

  /**
   * Retrieves the reason for the payment sheet event.
   *
   * @return The reason for the payment sheet event, or {@code null} if not set.
   */
  String getReason();

  /**
   * Retrieves the location where the payment sheet event took place.
   *
   * @return The place of the payment sheet event, or {@code null} if not set.
   */
  String getPlace();

  /**
   * Retrieves the start date of the payment sheet event.
   *
   * @return The start date of the payment sheet event, or {@code null} if
   * not set.
   */
  LocalDate getStartDate();

  /**
   * Retrieves the end date of the payment sheet event.
   *
   * @return The end date of the payment sheet event, or {@code null} if
   * not set.
   */
  LocalDate getEndDate();

  /**
   * Sets the unique identifier of the payment sheet.
   *
   * @param value The new identifier of the payment sheet.
   */
  void setId(Integer value);

  /**
   * Sets the reason for the payment sheet event.
   *
   * @param value The new reason for the payment sheet event.
   */
  void setReason(String value);

  /**
   * Sets the location where the payment sheet event took place.
   *
   * @param value The new place of the payment sheet event.
   */
  void setPlace(String value);

  /**
   * Sets the end date of the payment sheet event.
   *
   * @param value The new end date of the payment sheet event.
   */
  void setEndDate(LocalDate value);

  /**
   * Sets the start date of the payment sheet event.
   *
   * @param value The new start date of the payment sheet event.
   */
  void setStartDate(LocalDate value);

  /**
   * Sets the user who owns this payment sheet.
   *
   * @param value The user entity that owns the payment sheet.
   */
  void setUserOwner(PersistentUserEntity value);

  /**
   * Retrieves the user who owns this payment sheet.
   *
   * @return The user entity that owns the payment sheet, or {@code null}
   * if not set.
   */
  PersistentUserEntity getUserOwner();

  /**
   * Retrieves the regular transport entities associated with this payment sheet.
   *
   * @return A set of regular transport entities associated with this payment
   * sheet, or an empty set if none are found.
   */
  Set<PersistentRegularTransportEntity> getRegularTransports();

  /**
   * Retrieves the self vehicle entity associated with this payment sheet.
   *
   * @return The self vehicle entity associated with this payment sheet, or
   * {@code null} if not set.
   */
  SelfVehicleEntity getSelfVehicle();

  /**
   * Retrieves the food housing entity associated with this payment sheet.
   *
   * @return The food housing entity associated with this payment sheet, or
   * {@code null} if not set.
   */
  FoodHousingEntity getFoodHousing();

  /**
   * Sets the regular transport entities associated with this payment sheet.
   *
   * @param value A set of regular transport entities to be associated with
   * this payment sheet.
   */
  void setRegularTransports(Set<PersistentRegularTransportEntity> value);

}
