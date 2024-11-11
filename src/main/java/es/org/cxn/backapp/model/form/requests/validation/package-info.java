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
