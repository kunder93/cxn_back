package es.org.cxn.backapp.model;

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
 * Represents a team preference entity that defines the association between a
 * user and their preferred team. Implementations of this interface manage the
 * team name and user email, typically used as a composite key for storing user
 * preferences.
 *
 * <p>
 * This interface extends {@code Serializable} to allow implementations to be
 * serialized.
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface TeamPreferenceEntity extends java.io.Serializable {

    /**
     * Retrieves the name of the preferred team.
     *
     * @return the team name.
     */
    public String getTeamName();

    /**
     * Retrieves the email of the user who set the team preference.
     *
     * @return the user email.
     */
    public String getUserEmail();

    /**
     * Sets the name of the preferred team.
     *
     * @param teamName the team name to set.
     */
    public void setTeamName(String teamName);

    /**
     * Sets the email of the user who set the team preference.
     *
     * @param userEmail the user email to set.
     */
    public void setUserEmail(String userEmail);
}
