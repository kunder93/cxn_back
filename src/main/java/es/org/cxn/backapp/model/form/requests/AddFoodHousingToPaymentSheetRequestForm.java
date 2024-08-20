
package es.org.cxn.backapp.model.form.requests;

/**
 * Represents the form used by the controller as a request to add food or
 * housing to an existing payment sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @param amountDays The number of days that the event lasts.
 * @param dayPrice   Price per day.
 * @param overnight  True if sleep is included, false if not.
 *
 * @author Santiago Paz.
 */
public record AddFoodHousingToPaymentSheetRequestForm(
      Integer amountDays, float dayPrice, Boolean overnight
) {

}
