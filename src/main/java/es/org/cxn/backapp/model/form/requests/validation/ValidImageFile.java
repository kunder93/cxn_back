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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom validation annotation for validating image files. It checks both the
 * file size and format to ensure that the file meets specified requirements.
 *
 * <p>
 * This annotation can be used on fields or parameters of type
 * {@link org.springframework.web.multipart.MultipartFile}. The supported file
 * formats and maximum allowed file size can be customized.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * {@ValidImageFile(message = "Image must be a JPEG, PNG, WebP, or AVIF file and no larger than 8 MB.",
 *         filesize = 8 * 1024 * 1024)}
 * private MultipartFile imageFile;
 * }
 * </pre>
 *
 * <p>
 * The annotation uses {@link ImageFileValidator} for the actual validation
 * logic.
 * </p>
 *
 * @see ImageFileValidator
 *
 * @author YourName
 */
@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageFile {

    /**
     * Specifies the allowed formats for the image file. The format strings should
     * match the file MIME types.
     *
     * @return an array of allowed format strings, e.g., {"jpeg", "png", "webp",
     *         "avif"}
     */
    String[] allowedFormats() default { "jpeg", "png", "webp", "avif" };

    /**
     * Specifies the maximum file size allowed for the image file in bytes. Default
     * is set to 4 MB.
     *
     * @return the maximum allowed file size in bytes
     */
    long filesize() default 4 * 1024 * 1024; // 4 MB

    /**
     * Allows the specification of validation groups, to which this constraint
     * belongs.
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Specifies the default message if validation fails.
     *
     * @return the error message
     */
    String message() default "Invalid image file";

    /**
     * Specifies custom payload data to carry during validation.
     *
     * @return the payload with which clients can associate metadata
     */
    Class<? extends Payload>[] payload() default {};
}
