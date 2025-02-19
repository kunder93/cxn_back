package es.org.cxn.backapp;

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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main executable app class.
 * <p>
 * Spring Boot application.
 * </p>
 *
 * @author Santi
 */
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class,
        org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration.class })
public class BackAppApplication {

    /**
     * Default constructor for {@link BackAppApplication}.
     * <p>
     * This constructor is provided by the Java compiler and does not require
     * additional functionality. It allows the creation of instances of the
     * application class.
     * </p>
     */
    public BackAppApplication() {
        // Default constructor
    }

    /**
     * The application main method.
     *
     * @param args the application initial arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(BackAppApplication.class, args);
    }
}
