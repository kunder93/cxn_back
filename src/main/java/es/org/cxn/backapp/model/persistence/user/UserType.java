package es.org.cxn.backapp.model.persistence.user;

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
 * kind of member that users can be.
 *
 * @author Santi
 *
 */
public enum UserType {
    /**
     * Socio numerario, cuota de 30, mayor de 18 independiente economicamente.
     */
    SOCIO_NUMERO,
    /**
     * Socio aspirante, menor de 18, sin voto en junta.
     */
    SOCIO_ASPIRANTE,
    /**
     * Socio honorario, cuota de 0, sin voto en junta.
     */
    SOCIO_HONORARIO,
    /**
     * Depende economicamente de socio de numero, cuota 0, sin voto en junta.
     */
    SOCIO_FAMILIAR
}
