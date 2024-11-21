
package es.org.cxn.backapp.service;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;

/**
 * Implementation of {@link UserDetails} Change methods and works with
 * UserEntity.
 *
 * @author Santiago Paz Perez.
 *
 */
public final class MyPrincipalUser implements UserDetails {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 2055653610418609601L;

    /**
     * User DNI.
     */
    private final String dni;

    /**
     * User password.
     */
    private final String password;

    /**
     * User email.
     */
    private final String email;

    /**
     * User role Names.
     */
    private final Set<UserRoleName> rolesNames;

    /**
     * User state enabled or disabled.
     */
    private final boolean accountEnabled;

    /**
     * Constructor with provided UserEntity.
     *
     * @param userEntity User data to build MyPrincipalUser.
     */
    public MyPrincipalUser(final UserEntity userEntity) {
        dni = userEntity.getDni();
        password = userEntity.getPassword();
        email = userEntity.getEmail();
        accountEnabled = userEntity.isEnabled();
        rolesNames = EnumSet.noneOf(UserRoleName.class);
        userEntity.getRoles().forEach((PersistentRoleEntity role) -> rolesNames.add(role.getName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        rolesNames.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    /**
     * Getter for user DNI.
     *
     * @return The user dni.
     */
    public String getDni() {
        return dni;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Getter for user roles set.
     *
     * @see PersistentRoleEntity
     *
     * @return the user roles hashSet.
     */
    public Set<UserRoleName> getRoles() {
        return EnumSet.copyOf(rolesNames);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountEnabled;
    }

}
