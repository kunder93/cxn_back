package es.org.cxn.backapp.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.org.cxn.backapp.model.UserEntity;
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
     * User password.
     */
    private final String password;

    /**
     * User email.
     */
    private final String email;

    /**
     * User name.
     */
    private final String name;

    /**
     * User first surname.
     */
    private final String firstSurname;

    /**
     * User second surname.
     */
    private final String secondSurname;

    /**
     * User birth date.
     */
    private final LocalDate birthDate;

    /**
     * User gender.
     */
    private final String gender;

    /**
     * User role Names.
     */
    private Set<String> rolesNames = new HashSet<>();

    /**
     * Constructor with provided UserEntity.
     *
     * @param userEntity User data to build MyPrincipalUser.
     */
    public MyPrincipalUser(final UserEntity userEntity) {
        password = userEntity.getPassword();
        email = userEntity.getEmail();
        name = userEntity.getName();
        firstSurname = userEntity.getFirstSurname();
        secondSurname = userEntity.getSecondSurname();
        birthDate = userEntity.getBirthDate();
        gender = userEntity.getGender();
        userEntity.getRoles().forEach(
                (PersistentRoleEntity role) -> rolesNames.add(role.getName()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        rolesNames.forEach(
                role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Getter for user name.
     *
     * @return the user name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for user first surname.
     *
     * @return the first surname.
     */
    public String getFirstSurname() {
        return firstSurname;
    }

    /**
     * Getter for user second surname.
     *
     * @return the second surname.
     */
    public String getSecondSurname() {
        return secondSurname;
    }

    /**
     * Getter for user birth date.
     *
     * @return the user birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Getter for user gender.
     *
     * @return the user gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Getter for user roles set.
     *
     * @see PersistentRoleEntity
     *
     * @return the user roles hashSet.
     */
    public Set<String> getRoles() {
        return new HashSet<>(rolesNames);
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
        return true;
    }

}
