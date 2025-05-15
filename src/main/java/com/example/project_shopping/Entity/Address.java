package com.example.project_shopping.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String country;
    String city;
    String street;
    String phoneNumber;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = true)
    User user;
}
