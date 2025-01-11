
package es.org.cxn.backapp.exceptions;

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

import es.org.cxn.backapp.model.UserRoleName;

/**
 * Exception thrown by service when role with provided name not found.
 *
 * @author Santiago Paz.
 *
 */
public final class RoleNameNotFoundException extends Exception {
  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7474112384810484613L;

  /**
   * The role name field.
   */
  private final UserRoleName roleName;

  /**
   * Main constructor.
   *
   * @param value the role name that throw exception.
   */
  public RoleNameNotFoundException(final UserRoleName value) {
    super(value.toString());
    this.roleName = value;
  }

  /**
   * Getter for role name field.
   *
   * @return the role name.
   */
  public UserRoleName getRoleName() {
    return roleName;
  }

  @Override
  public String getMessage() {
    return "Role with name: " + roleName + " not found";
  }

}
