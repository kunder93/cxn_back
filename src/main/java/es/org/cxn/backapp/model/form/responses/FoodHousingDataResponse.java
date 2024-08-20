
package es.org.cxn.backapp.model.form.responses;

/**
 * Represents the response DTO used by the controller for food housing data.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller for food housing data.
 * </p>
 *
 * @param amountDays The number of days the food housing lasts.
 * @param dayPrice   The price per day for food housing.
 * @param overnight  Indicates if overnight accommodation is included.
 * @author Santiago Paz
 */
public record FoodHousingDataResponse(
      Integer amountDays, double dayPrice, boolean overnight
) {

}
