package com.example.project_shopping.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "photo", length = 45)
    private String photo;

    @Column(name = "createAt")
    private LocalDate createAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Role_id", nullable = false)
    @JsonManagedReference
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Voucher> vouchers = new LinkedHashSet<>();

    @PrePersist
    public void addCreateAt(){
        if(createAt == null){
            createAt = LocalDate.now();
        }

        if (this.photo == null || this.photo.isEmpty()) {
            this.photo = "default-photo.jpg";
        }
    }
}