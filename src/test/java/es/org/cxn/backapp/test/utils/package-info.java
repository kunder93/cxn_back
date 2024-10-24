/**
 * Provides utility classes for testing purposes within the
 * `es.org.cxn.backapp.test.utils` package.
 *
 * This package includes various factory classes and custom adapters
 * used to create test data and handle JSON serialization/deserialization
 * for different scenarios.
 *
 * The classes in this package include:
 *
 * - {@link CompanyControllerFactory} - Factory class for generating
 *   company-related test data and JSON representations.
 *
 * - {@link InvoicesControllerFactory} - Factory class for generating
 *   invoice-related test data and JSON representations.
 *
 * - {@link LocalDateAdapter} - Custom adapter for handling the serialization
 *   and deserialization of {@link java.time.LocalDate} objects with Gson.
 *
 * - {@link LocalDateTimeAdapter} - Custom adapter for handling
 *   the serialization and deserialization of {@link java.time.LocalDateTime}
 *    objects with Gson.
 *
 * - {@link PaymentSheetControllerFactory} - Factory class for generating
 *   payment sheet-related test data and JSON representations.
 *
 * - {@link UsersControllerFactory} - Factory class for generating user-related
 *   test data and JSON representations.
 *
 * Each factory class in this package is responsible for creating and
 * providing sample data and JSON representations needed for testing
 * different components and functionalities of the application. The custom
 * adapters ensure proper handling of date and time types during JSON
 * processing.
 */

package es.org.cxn.backapp.test.utils;
