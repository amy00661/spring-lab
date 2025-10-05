package com.example.springlab.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name; // 例如 ADMIN、USER（不要含 ROLE_ 前綴）

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authorities",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<AuthorityEntity> authorities = new HashSet<>();
}