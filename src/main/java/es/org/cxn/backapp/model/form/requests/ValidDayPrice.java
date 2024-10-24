
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating the day price based on the overnight status.
 */
@Constraint(validatedBy = DayPriceValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDayPrice {

  /**
   * The default error message to be used when the day price is invalid based
   * on the overnight status.
   *
   * @return The error message.
   */
  String message() default "Invalid day price based on overnight status";

  /**
   * Groups can be used to group multiple constraints together and apply them
   * conditionally.
   *
   * @return The groups that this constraint belongs to.
   */
  Class<?>[] groups() default {};

  /**
   * Payload can be used to carry additional data with the constraint
   * annotation.
   * This data can be used by the validation framework or constraint validators
   * for additional processing.
   *
   * @return The payload data.
   */
  Class<? extends Payload>[] payload() default {};
}
