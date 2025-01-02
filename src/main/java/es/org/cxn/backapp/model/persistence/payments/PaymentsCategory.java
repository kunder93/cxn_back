package es.org.cxn.backapp.model.persistence.payments;

/**
 * Enum representing the possible categories of a payment. The category can be
 * one of the following:
 * <ul>
 * <li>{@link #MEMBERSHIP_PAYMENT}</li>
 * <li>{@link #FEDERATE_PAYMENT}</li>
 * <li>{@link #OTHER_PAYMENT}</li>
 * </ul>
 */
public enum PaymentsCategory {

    /**
     * Payment related to a membership fee. This category represents payments made
     * for membership fees, such as a subscription or dues.
     */
    MEMBERSHIP_PAYMENT("member_payment"),

    /**
     * Payment related to federating a user. This category represents payments made
     * for federating a user in a federation or similar structure.
     */
    FEDERATE_PAYMENT("federate_payment"),

    /**
     * Payment for any other type not covered by the defined categories. This
     * category represents payments that don't fall under the member or federate
     * categories.
     */
    OTHER_PAYMENT("other_payment");

    /**
     * The string value associated with the payment category.
     * <p>
     * This field holds a unique identifier for each payment category, which can be
     * used for storage or retrieval operations where a string representation of the
     * category is required.
     * </p>
     */
    private final String value;

    /**
     * Constructs a {@link PaymentsCategory} with the specified string value.
     * <p>
     * This constructor initializes the payment category with a unique string value
     * that identifies the category. It is used internally to set up the predefined
     * constants of the enum.
     * </p>
     *
     * @param val the string value associated with the payment category.
     */
    PaymentsCategory(final String val) {
        this.value = val;
    }

    /**
     * Returns the enum constant corresponding to the given string value.
     *
     * @param value the string value to map to an enum constant.
     * @return the corresponding {@link PaymentsCategory}.
     * @throws IllegalArgumentException if the value does not match any category.
     */
    public static PaymentsCategory fromValue(final String value) {
        for (final PaymentsCategory category : PaymentsCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }

    /**
     * Returns the string value associated with the category.
     *
     * @return the category value.
     */
    public String getValue() {
        return value;
    }
}
