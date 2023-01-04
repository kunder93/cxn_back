package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

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
        userRepository = checkNotNull(repository,
                "Received a null pointer as repository");
    }

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        var userEntityOptional = userRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException("email: " + email);
        }

        return new MyPrincipalUser(userEntityOptional.get());
    }

}
