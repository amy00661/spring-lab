package com.example.springlab.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    private UserEntity userInfo;
    private List<RoleEntity> roleInfoList;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private List<String> roles;

    public Long getUserId() {
        return this.userInfo.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (grantedAuthorities != null) return this.grantedAuthorities;
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<String> authorities = new ArrayList<>();
        roleInfoList.forEach(role -> {
            authorities.add(role.getName());
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        this.grantedAuthorities = grantedAuthorities;
        this.roles = authorities;
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userInfo.getUsername();
    }

    /**
     * 賬戶是否沒過期
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 賬戶是否沒被鎖定
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 賬戶憑據是否沒過期
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 賬戶是否啟用
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}