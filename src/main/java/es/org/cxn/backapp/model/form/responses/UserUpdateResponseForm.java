
package es.org.cxn.backapp.model.form.responses;

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

import es.org.cxn.backapp.model.UserEntity;

/**
 * A record representing the response form used for updating a user's details in
 * the controller. This record encapsulates the essential fields that represent
 * a user in the update operation.
 * <p>
 * The record is immutable and provides an automatic implementation of equals,
 * hashCode, and toString methods. The fields include the user's name, surnames,
 * birth date, and gender.
 * </p>
 *
 * @param name          The user's first name.
 * @param firstSurname  The user's first surname.
 * @param secondSurname The user's second surname.
 * @param birthDate     The user's birth date.
 * @param gender        The user's gender.
 *
 * @author Santiago Paz Perez
 */
public record UserUpdateResponseForm(String name, String firstSurname, String secondSurname, LocalDate birthDate,
        String gender) {

    /**
     * Constructs a {@code UserUpdateResponseForm} from a {@link UserEntity}.
     *
     * @param user The {@code UserEntity} from which to create the response form.
     *             The entity's properties are used to initialize the record fields.
     */
    public UserUpdateResponseForm(final UserEntity user) {
        this(user.getProfile().getName(), user.getProfile().getFirstSurname(), user.getProfile().getSecondSurname(),
                user.getProfile().getBirthDate(), user.getProfile().getGender());
    }
}
