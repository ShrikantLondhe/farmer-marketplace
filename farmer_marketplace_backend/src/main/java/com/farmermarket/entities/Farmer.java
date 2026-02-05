package com.farmermarket.entities;

import com.farmermarket.security.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "farmers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Farmer extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String address;

    // Managed by Admin
    @Column(nullable = false)
    private Boolean approved = false;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
