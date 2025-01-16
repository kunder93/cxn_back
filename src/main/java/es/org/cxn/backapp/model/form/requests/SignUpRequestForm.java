
package es.org.cxn.backapp.model.form.requests;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.time.LocalDate;

import es.org.cxn.backapp.model.form.Constants;
import es.org.cxn.backapp.model.persistence.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents the form used by the controller as input for creating users. This
 * contains user data and user address.
 * <p>
 * This is a DTO (Data Transfer Object), meant to facilitate communication
 * between the view and the controller. The record is immutable and provides
 * built-in implementations for methods such as {@code equals()},
 * {@code hashCode()}, and {@code toString()}.
 * <p>
 * Includes Java validation annotations to ensure that all required fields are
 * provided and valid.
 *
 * @param dni                    User DNI, must be 8 digits followed by a
 *                               letter.
 * @param name                   User name, must not be blank and within the max
 *                               length.
 * @param firstSurname           User first surname, must not be blank and
 *                               within the max length.
 * @param secondSurname          User second surname, must not be blank and
 *                               within the max length.
 * @param birthDate              User birth date, must be in the past.
 * @param gender                 User gender, must not be blank and within the
 *                               max length.
 * @param password               User password, must not be blank and within the
 *                               min and max length.
 * @param email                  User email, must not be blank, well formatted,
 *                               and within the max length.
 * @param postalCode             User postal code, must not be blank and within
 *                               the max length.
 * @param apartmentNumber        User apartment number, must not be blank and
 *                               within the max length.
 * @param building               User building, must not be blank and within the
 *                               max length.
 * @param street                 User street, must not be blank and within the
 *                               max length.
 * @param city                   User city, must not be blank and within the max
 *                               length.
 * @param kindMember             User type, can be null.
 * @param countryNumericCode     Country numeric code, can be null.
 * @param countrySubdivisionName Country subdivision name, can be null.
 *
 * @author Santiago Paz
 */
public record SignUpRequestForm(
        @Pattern(regexp = "^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$", message = Constants.DNI_BAD_FORMAT)
        @NotNull(message = Constants.DNI_BAD_FORMAT) String dni,

        @NotBlank(message = Constants.NAME_NOT_BLANK)
        @Size(max = Constants.NAME_MAX_LENGTH, message = Constants.NAME_MAX_LENGTH_MESSAGE) String name,

        @NotBlank(message = Constants.FIRST_SURNAME_NOT_BLANK)
        @Size(max = Constants.FIRST_SURNAME_MAX_LENGTH,
                message = Constants.FIRST_SURNAME_MAX_LENGTH_MESSAGE) String firstSurname,

        @NotBlank(message = Constants.SECOND_SURNAME_NOT_BLANK)
        @Size(max = Constants.SECOND_SURNAME_MAX_LENGTH,
                message = Constants.SECOND_SURNAME_MAX_LENGTH_MESSAGE) String secondSurname,

        @Past(message = Constants.BIRTH_DATE_PAST) LocalDate birthDate,

        @NotBlank(message = Constants.GENDER_NOT_BLANK)
        @Size(max = Constants.GENDER_MAX_LENGTH, message = Constants.GENDER_MAX_LENGTH_MESSAGE) String gender,

        @NotBlank(message = Constants.PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = Constants.MIN_PASSWORD_LENGTH, max = Constants.MAX_PASSWORD_LENGTH,
                message = Constants.PASSWORD_SIZE_MESSAGE) String password,

        @NotBlank(message = Constants.EMAIL_NOT_VALID)
        @Size(max = Constants.EMAIL_MAX_SIZE, message = Constants.MAX_SIZE_EMAIL_MESSAGE)
        @Email(message = Constants.EMAIL_NOT_VALID) String email,

        @NotBlank(message = Constants.POSTAL_CODE_NOT_BLANK_MESSAGE)
        @Size(max = Constants.POSTAL_CODE_MAX_LENGHT,
                message = Constants.POSTAL_CODE_MAX_LENGHT_MESSAGE) String postalCode,

        @NotBlank(message = Constants.APARTMENT_NUMBER_NOT_BLANK_MESSAGE)
        @Size(max = Constants.APARTMENT_NUMBER_MAX_LENGHT,
                message = Constants.APARTMENT_NUMBER_MAX_LENGHT_MESSAGE) String apartmentNumber,

        @NotBlank(message = Constants.BUILDING_NOT_BLANK)
        @Size(max = Constants.BUILDING_MAX_LENGHT, message = Constants.BUILDING_MAX_LENGHT_MESSAGE) String building,

        @NotBlank(message = Constants.STREET_NOT_BLANK)
        @Size(max = Constants.STREET_MAX_LENGHT, message = Constants.STREET_MAX_LENGHT_MESSAGE) String street,

        @NotBlank(message = Constants.CITY_NOT_BLANK)
        @Size(max = Constants.CITY_MAX_LENGHT, message = Constants.CITY_MAX_LENGHT_MESSAGE) String city,

        UserType kindMember,

        Integer countryNumericCode,

        String countrySubdivisionName) {

}
