package com.firs.risk.mgt.auth.svc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = User.TABLE_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    public static final String TABLE_NAME= "firs_users";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID ID;
    private String phone;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Date lastLogin;
    private Boolean enabled;
    private Boolean active;
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "firs_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private Date expiry;

}

