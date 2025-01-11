/**
 * This package contains integration tests for service layer classes within the
 * application, specifically focusing on the default implementations of various
 * core services.
 *
 * <p>The services tested in this package include:</p>
 *
 * <ul>
 *   <li>{@link es.org.cxn.backapp.service.impl.DefaultLibraryService}: This service
 *   handles operations related to the management of the application's
 *   library resources.</li>
 *
 *   <li>{@link es.org.cxn.backapp.service.impl.DefaultRoleService}: This service
 *   is responsible for managing roles within the application's
 *   security framework.</li>
 * </ul>
 *
 * <p>Each test class within this package ensures that the respective services
 * function correctly when integrated with other parts of the application,
 * such as the data access layer and security components.</p>
 *
 * <p>The tests are designed to validate both typical usage scenarios and
 * edge cases, ensuring that the services are robust and reliable in
 * a production environment.</p>
 *
 * <p>Author: Santiago Paz.</p>
 */

package es.org.cxn.backapp.test.integration.services;

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
