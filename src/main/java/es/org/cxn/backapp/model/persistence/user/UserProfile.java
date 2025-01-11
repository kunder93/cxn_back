package es.org.cxn.backapp.model.persistence.user;

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
