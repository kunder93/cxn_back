package es.org.cxn.backapp.model.form.requests;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidDayPrice} annotation.
 * <p>
 * This validator checks if the {@link AddFoodHousingToPaymentSheetRequest}
 * object has a valid day price based on whether the request includes an
 * overnight stay or not.
 * </p>
 */
public final class DayPriceValidator
        implements ConstraintValidator<ValidDayPrice, AddFoodHousingToPaymentSheetRequest> {

    /**
     * Default constructor for {@link DayPriceValidator}.
     * <p>
     * This constructor is provided by the Java compiler and does not require
     * additional functionality.
     * </p>
     */
    public DayPriceValidator() {
        // Default constructor
    }

    /**
     * Validates the day price based on whether the overnight stay is true or false.
     *
     * @param dayPrice  The day price to validate.
     * @param overnight Indicates whether the stay is overnight.
     * @param context   The context in which the constraint is evaluated.
     * @return true if the day price is valid, false otherwise.
     */
    private static boolean validateDayPrice(final BigDecimal dayPrice, final Boolean overnight,
            final ConstraintValidatorContext context) {
        final boolean isValid;

        if (Boolean.TRUE.equals(overnight)) {
            isValid = validateDayPriceForOvernight(dayPrice, context);
        } else {
            isValid = validateDayPriceForNoOvernight(dayPrice, context);
        }

        return isValid;
    }

    /**
     * Validates the day price for a non-overnight stay.
     *
     * @param dayPrice The day price to validate.
     * @param context  The context in which the constraint is evaluated.
     * @return true if the day price is valid for a non-overnight stay, false
     *         otherwise.
     */
    private static boolean validateDayPriceForNoOvernight(final BigDecimal dayPrice,
            final ConstraintValidatorContext context) {
        boolean isValid = true; // Default to valid

        if (dayPrice.compareTo(ValidationConstants.MAX_DAY_PRICE_NO_OVERNIGHT) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationConstants.DAY_PRICE_MESSAGE_NO_OVERNIGHT)
                    .addConstraintViolation();
            isValid = false; // Set as invalid if price exceeds the limit
        }

        return isValid; // Return the result at the end
    }

    /**
     * Validates the day price for an overnight stay.
     *
     * @param dayPrice The day price to validate.
     * @param context  The context in which the constraint is evaluated.
     * @return true if the day price is valid for an overnight stay, false
     *         otherwise.
     */
    private static boolean validateDayPriceForOvernight(final BigDecimal dayPrice,
            final ConstraintValidatorContext context) {
        boolean isValid = true; // Default to valid

        if (dayPrice.compareTo(ValidationConstants.MAX_DAY_PRICE_OVERNIGHT) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationConstants.DAY_PRICE_MESSAGE_OVERNIGHT)
                    .addConstraintViolation();
            isValid = false; // Set as invalid if price exceeds the limit
        }

        return isValid; // Return the result at the end
    }

    @Override
    public boolean isValid(final AddFoodHousingToPaymentSheetRequest form, final ConstraintValidatorContext context) {
        boolean isValid = true; // Default to valid

        if (form != null) {
            final var overnight = form.overnight();
            if (overnight != null) {
                final var dayPrice = form.dayPrice();
                isValid = validateDayPrice(dayPrice, overnight, context); // Perform validation and set isValid
                                                                          // accordingly
            }
        }

        return isValid; // Return the result at the end
    }

}
