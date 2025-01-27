package es.org.cxn.backapp.model.form.requests.member_resources;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

/**
 * Validation values like messages and constants.
 */
public class ValidationValues {

    /**
     * The minimum allowed length for the title of a book.
     */
    public static final int MIN_TITLE_LENGTH = 1;

    /**
     * The maximum allowed length for the title of a book.
     */
    public static final int MAX_TITLE_LENGTH = 100;

    /**
     * The minimum allowed length for the gender of a book.
     */
    public static final int MIN_GENDER_LENGTH = 1;

    /**
     * The maximum allowed length for the gender of a book.
     */
    public static final int MAX_GENDER_LENGTH = 30;

    /**
     * The minimum allowed length for the language of a book.
     */
    public static final int MIN_LANGUAGE_LENGTH = 1;

    /**
     * The maximum allowed length for the language of a book.
     */
    public static final int MAX_LANGUAGE_LENGTH = 20;

    /**
     * The minimum number of authors required for the authors list of a book.
     */
    public static final int MIN_AUTHORS_LIST_SIZE = 1;

    /**
     * The maximum length allowed for the transport description.
     */
    public static final int MAX_DESCRIPTION_LENGTH = 100;

    /**
     * Error message for when the ISBN field is null.
     */
    public static final String ISBN_NOT_NULL = "ISBN must not be null";

    /**
     * Error message for when the title field is null.
     */
    public static final String TITLE_NOT_NULL = "Title must not be null";

    /**
     * Error message for when the title length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_TITLE_LENGTH} and
     * {@code ValidationConstants.MAX_TITLE_LENGTH} characters.
     */
    public static final String TITLE_SIZE = "Title must be between " + MIN_TITLE_LENGTH + " and " + MAX_TITLE_LENGTH
            + " characters";

    /**
     * Error message for when the gender length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_GENDER_LENGTH} and
     * {@code ValidationConstants.MAX_GENDER_LENGTH} characters.
     */
    public static final String GENDER_SIZE = "Gender must be between " + MIN_GENDER_LENGTH + " and " + MAX_GENDER_LENGTH
            + " characters";

    /**
     * Error message for when the language length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_LANGUAGE_LENGTH} and
     * {@code ValidationConstants.MAX_LANGUAGE_LENGTH} characters.
     */
    public static final String LANGUAGE_SIZE = "Language must be between " + MIN_LANGUAGE_LENGTH + " and "
            + MAX_LANGUAGE_LENGTH + " characters";

    /**
     * Error message for when the authors list is null.
     */
    public static final String AUTHORS_LIST_NOT_NULL = "Authors list must not be null";

    /**
     * Error message for when the authors list size is smaller than the minimum
     * allowed. The list must contain at least
     * {@code ValidationConstants.MIN_AUTHORS_LIST_SIZE} author.
     */
    public static final String AUTHORS_LIST_SIZE = "Authors list must contain at least " + MIN_AUTHORS_LIST_SIZE
            + " author";

    /**
     * The private constructor.
     */
    private ValidationValues() {
        // Private constructor.
    }

}
