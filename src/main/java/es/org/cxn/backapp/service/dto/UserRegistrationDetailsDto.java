
package es.org.cxn.backapp.service.dto;

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

import es.org.cxn.backapp.model.persistence.user.UserType;

/**
 * Data Transfer Object (DTO) representing the details required for user
 * registration.
 * <p>
 * This record encapsulates the user details necessary for registration,
 * including personal information, contact details, and address information.
 * </p>
 *
 * @param dni            the unique identifier (DNI) of the user
 * @param name           the name of the user
 * @param firstSurname   the first surname of the user
 * @param secondSurname  the second surname of the user
 * @param birthDate      the birth date of the user
 * @param gender         the gender of the user
 * @param password       the password for the user's account
 * @param email          the email address of the user
 * @param addressDetails an instance of {@link AddressRegistrationDetailsDto}
 *                       containing the user's address details
 * @param kindMember     the type of membership the user holds, represented by
 *                       {@link UserType}
 *
 * @author Santi
 */
public record UserRegistrationDetailsDto(String dni, String name, String firstSurname, String secondSurname,
        LocalDate birthDate, String gender, String password, String email, AddressRegistrationDetailsDto addressDetails,
        UserType kindMember) {
}
