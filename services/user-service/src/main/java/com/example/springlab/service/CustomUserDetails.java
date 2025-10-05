package com.example.springlab.service;

import com.example.springlab.model.RoleEntity;
import com.example.springlab.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.authorities = mapAuthorities(user);
    }

    private Set<GrantedAuthority> mapAuthorities(UserEntity user) {
        Set<GrantedAuthority> result = new HashSet<>();
        // 角色：以 "ROLE_" 開頭
        result.addAll(user.getRoles().stream()
                .map(RoleEntity::getName)
                .map(SimpleGrantedAuthority::new) // 假設資料庫中的 name 已經是 "ROLE_XXX"
                .collect(Collectors.toSet()));
        // 權限：展開角色底下的 authorities（若模型有）
        user.getRoles().forEach(role -> {
            if (role.getAuthorities() != null) {
                role.getAuthorities().forEach(auth ->
                        result.add(new SimpleGrantedAuthority(auth.getName()))
                );
            }
        });
        return result;
    }

    public Long getId() {
        return id;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
