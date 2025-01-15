package com.example.clinic.Domain.SecurityUser;

import com.example.clinic.Domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    private User model;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + model.getRole().name()));    }

    @Override
    public String getPassword() {
        return model.getPassword();
    }

    @Override
    public String getUsername() {
        return model.getEmail();
    }
}
