package me.raddatz.yapam.user.repository;

import lombok.Getter;
import lombok.Setter;
import me.raddatz.yapam.secret.repository.SecretDBO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
public class UserDBO {

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
    @Column(name = "master_password_hint")
    private String masterPasswordHint;
    @Column(name = "public_key")
    @Lob
    private String publicKey;
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
    private Set<RoleDBO> role;
    @OneToMany(
            mappedBy = "user",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SecretDBO> secrets;
}
