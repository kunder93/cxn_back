
package es.org.cxn.backapp.security;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.repository.UserEntityRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    userRepository =
          checkNotNull(repository, "Received a null pointer as repository");
  }

  /**
   * Load User by user name aka email.
   */
  @Override
  public UserDetails loadUserByUsername(final String email) {
    final var usrEntityOpt = userRepository.findByEmail(email);
    if (usrEntityOpt.isEmpty()) {
      throw new UsernameNotFoundException("email: " + email);
    }
    final var userEntity = usrEntityOpt.get();
    return new MyPrincipalUser(userEntity);
  }

}
