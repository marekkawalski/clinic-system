package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    @Size(min = Constants.MIN_NAME_LENGTH, max = Constants.MAX_NAME_LENGTH, message = "Name must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH + " characters")
    private String name;

    @Size(min = Constants.MIN_SURNAME_LENGTH, max = Constants.MAX_SURNAME_LENGTH, message = "Surname must be between " + Constants.MIN_SURNAME_LENGTH + " and " + Constants.MAX_SURNAME_LENGTH + " characters")
    private String surname;

    @Email(message = "Invalid email format")
    @Indexed(unique = true)

    private String email;
    private String password;
    private UserRole userRole;
    private boolean isEnabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
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
        return isEnabled;
    }
}
