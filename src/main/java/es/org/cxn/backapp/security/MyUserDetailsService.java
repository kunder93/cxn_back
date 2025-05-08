
package es.org.cxn.backapp.security;

import java.util.Objects;

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

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.repository.UserEntityRepository;

/**
 * Implementation of Spring UserDetailsService.
 *
 * @author Santiago Paz Perez.
 *
 */
@Service
public final class MyUserDetailsService implements UserDetailsService {

    /**
     * User repository field.
     */
    private final UserEntityRepository userRepository;

    /**
     * Main constructor.
     *
     * @param repository the user repository.
     */
    public MyUserDetailsService(final UserEntityRepository repository) {
        super();
        userRepository = Objects.requireNonNull(repository, "Received a null pointer as repository");
    }

    /**
     * Load User by user email as user identifier.
     */
    @Override
    public UserDetails loadUserByUsername(final String email) {
        String normalizedEmail = email.toLowerCase().trim(); // Normalize email

        final var usrEntityOpt = userRepository.findByEmail(normalizedEmail);
        if (usrEntityOpt.isEmpty()) {
            throw new UsernameNotFoundException("email: " + normalizedEmail);
        }
        final var userEntity = usrEntityOpt.get();
        return new MyPrincipalUser(userEntity);
    }

}
