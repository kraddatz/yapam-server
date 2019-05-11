package me.raddatz.jwarden.user.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(nullable = false)
    private String name;
    private String email;
    @Column(name = "email_token")
    private String emailToken;
    @Column(name = "email_verified")
    private Boolean emailVerified;
    @Column(name = "master_password_hash")
    private String masterPasswordHash;
    @Column(name = "master_password_salt")
    private String masterPasswordSalt;
    @Column(name = "master_password_hint")
    private String masterPasswordHint;
    private String culture;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> role;

}
