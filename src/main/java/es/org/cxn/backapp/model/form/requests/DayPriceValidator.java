
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Validator for the {@link ValidDayPrice} annotation.
 * <p>
 * This validator checks if the {@link AddFoodHousingToPaymentSheetRequest} object
 * has a valid day price based on whether the request includes an overnight
 * stay or not.
 * </p>
 */
public final class DayPriceValidator implements
      ConstraintValidator<ValidDayPrice, AddFoodHousingToPaymentSheetRequest> {

  @Override
  public boolean isValid(
        final AddFoodHousingToPaymentSheetRequest form,
        final ConstraintValidatorContext context
  ) {
    if (form == null) {
      // Assume null form is valid
      return true;
    }

    final var overnight = form.overnight();

    if (overnight == null) {
      // If overnight is null, no validation applied
      return true;
    }
    final var dayPrice = form.dayPrice();
    return validateDayPrice(dayPrice, overnight, context);
  }

  private static boolean validateDayPrice(
        final BigDecimal dayPrice, final Boolean overnight,
        final ConstraintValidatorContext context
  ) {
    if (Boolean.TRUE.equals(overnight)) {
      return validateDayPriceForOvernight(dayPrice, context);
    } else {
      return validateDayPriceForNoOvernight(dayPrice, context);
    }
  }

  private static boolean validateDayPriceForOvernight(
        final BigDecimal dayPrice, final ConstraintValidatorContext context
  ) {
    if (dayPrice.compareTo(ValidationConstants.MAX_DAY_PRICE_OVERNIGHT) > 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
            ValidationConstants.DAY_PRICE_MESSAGE_OVERNIGHT
      ).addConstraintViolation();
      return false;
    }
    return true;
  }

  private static boolean validateDayPriceForNoOvernight(
        final BigDecimal dayPrice, final ConstraintValidatorContext context
  ) {
    if (dayPrice
          .compareTo(ValidationConstants.MAX_DAY_PRICE_NO_OVERNIGHT) > 0) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
            ValidationConstants.DAY_PRICE_MESSAGE_NO_OVERNIGHT
      ).addConstraintViolation();
      return false;
    }
    return true;
  }
}
