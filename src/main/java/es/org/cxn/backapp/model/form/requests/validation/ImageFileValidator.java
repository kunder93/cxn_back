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

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidImageFile} annotation, which validates image
 * files based on specified file size and format constraints.
 *
 * <p>
 * This validator performs two main checks:
 * </p>
 * <ul>
 * <li>File size: Ensures that the file size does not exceed a defined
 * maximum.</li>
 * <li>File format: Ensures that the file is one of the allowed types (e.g.,
 * JPEG, PNG, WebP, AVIF).</li>
 * </ul>
 *
 * <p>
 * If the file does not meet the specified criteria, an error message is
 * generated.
 * </p>
 *
 * <p>
 * This class is initialized with parameters from the {@code @ValidImageFile}
 * annotation.
 * </p>
 *
 * @see ValidImageFile
 *
 * @author YourName
 */
public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {

    /**
     * The maximum allowed file size for the image file in bytes.
     * <p>
     * This value is initialized from the {@link ValidImageFile#filesize()}
     * annotation attribute, and is used to validate that the uploaded image does
     * not exceed the specified size.
     * </p>
     */
    private long maxFileSize;

    /**
     * An array of allowed image formats (MIME types).
     * <p>
     * This value is initialized from the {@link ValidImageFile#allowedFormats()}
     * annotation attribute, and is used to validate that the uploaded image file
     * matches one of the allowed MIME types.
     * </p>
     * <p>
     * For example, valid formats could include "image/jpeg", "image/png",
     * "image/webp", "image/avif".
     * </p>
     */
    private String[] allowedFormats;

    /**
     * Initializes the validator with values from the {@link ValidImageFile}
     * annotation, setting the maximum allowed file size and allowed file formats.
     *
     * @param constraintAnnotation the annotation instance containing configuration
     *                             data
     */
    @Override
    public void initialize(final ValidImageFile constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.filesize();
        this.allowedFormats = constraintAnnotation.allowedFormats();
    }

    /**
     * Validates the provided {@link MultipartFile} based on file size and format
     * constraints.
     *
     * <p>
     * If the file is null or empty, this method returns {@code true} to allow other
     * annotations like {@code @NotNull} to handle null checks if needed.
     * </p>
     *
     * @param file    the image file to validate
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the file meets the size and format constraints,
     *         otherwise {@code false}
     */
    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
        boolean isValid = true; // Default to valid

        if (file != null && !file.isEmpty()) {
            // Check file size
            if (file.getSize() > maxFileSize) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("File size should not exceed " + maxFileSize + " bytes")
                        .addConstraintViolation();
                isValid = false; // Update result to false
            }

            // Check file type
            final String contentType = file.getContentType();
            if (contentType == null || Arrays.stream(allowedFormats).noneMatch(contentType::endsWith)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "File type must be one of: " + String.join(", ", allowedFormats)).addConstraintViolation();
                isValid = false; // Update result to false
            }
        }

        return isValid; // Return the result at the end
    }

}
