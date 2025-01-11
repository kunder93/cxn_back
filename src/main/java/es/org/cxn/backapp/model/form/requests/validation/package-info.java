/**
 * Contains custom validation annotations and their associated validators for
 * validating request data.
 * <p>
 * This package includes annotations and validators to enforce specific
 * constraints on incoming request data fields. These validations ensure that
 * fields meet requirements such as file type and size, among other possible
 * constraints defined in the application.
 * </p>
 *
 * <ul>
 * <li>{@link ValidImageFile} - Annotation to validate image file format and
 * size constraints.</li>
 * <li>{@link ImageFileValidator} - Implements the logic for the
 * {@code ValidImageFile} annotation, validating the file type and size.</li>
 * </ul>
 *
 *
 */
package es.org.cxn.backapp.model.form.requests.validation;

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
