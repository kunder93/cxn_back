package es.org.cxn.backapp.model.persistence.user;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Encapsulates basic user profile details.
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3546611758551963766L;

    /**
     * Name of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "name", nullable = false, unique = false)
    private String name;

    /**
     * First surname of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "first_surname", nullable = false, unique = false)
    private String firstSurname;

    /**
     * Second surname of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "second_surname", nullable = false, unique = false)
    private String secondSurname;

    /**
     * Birth date of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "birth_date", nullable = false, unique = false)
    private LocalDate birthDate;

    /**
     * Gender of the user.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Column(name = "gender", nullable = false, unique = false)
    private String gender;

}
