package org.example.service;

import lombok.Getter;
import org.example.entities.UserInfo;
import org.example.entities.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
Being used for constructor to set username, password, list of roles
*/
public class CustomUserDetails extends UserInfo implements UserDetails {

    private String username;

    private String password;

    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo userInfo) {
        this.username=userInfo.getUsername();
        this.password=userInfo.getPassword();

        List<GrantedAuthority> l=new ArrayList<>();
        for(UserRoles role: userInfo.getRoles()){
            l.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }

        this.authorities=l;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
