package com.example.springlab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class AuthorityEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name; // 例如 REPORT:READ
}