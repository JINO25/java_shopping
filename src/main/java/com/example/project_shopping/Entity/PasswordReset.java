package com.example.project_shopping.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "passwordReset")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expires", nullable = false)
    private LocalDateTime expires;

}
