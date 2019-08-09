package app.yapam.user.repository;

import app.yapam.secret.repository.SecretDao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserDao {

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
    @Column(name = "public_key")
    @Lob
    private String publicKey;
    private String locale;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @OneToMany(
            mappedBy = "user",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SecretDao> secrets;
}
