
package es.org.cxn.backapp.model;

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

/**
 * Enumeration representing the names of the user roles within the system.
 * Each role corresponds to a specific level of access or set of privileges.
 *
 * <p>
 * Roles include:
 * <ul>
 *   <li>ROLE_ADMIN - Grants all privileges within the system.</li>
 *   <li>ROLE_PRESIDENTE - Role of the president.</li>
 *   <li>ROLE_SECRETARIO - Role of the secretary.</li>
 *   <li>ROLE_TESORERO - Role of the treasurer.</li>
 *   <li>ROLE_SOCIO - Role of a regular member.</li>
 *   <li>ROLE_CANDIDATO_SOCIO - Role of a candidate for membership.</li>
 * </ul>
 *
 * <p>
 * These roles are used to define the permissions and access levels of different
 * users within the application.
 *
 * @author Santi
 */
public enum UserRoleName {
  /**
   * Admin role grants all privileges.
   */
  ROLE_ADMIN,

  /**
   * Role of the president.
   */
  ROLE_PRESIDENTE,

  /**
   * Role of the secretary.
   */
  ROLE_SECRETARIO,

  /**
   * Role of the treasurer.
   */
  ROLE_TESORERO,

  /**
   * Role of a regular member.
   */
  ROLE_SOCIO,

  /**
   * Role of a candidate for membership.
   */
  ROLE_CANDIDATO_SOCIO
}
