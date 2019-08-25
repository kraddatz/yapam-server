package app.yapam.common.repository;

import app.yapam.secret.model.SecretTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "secret")
public class SecretDao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String title;
    @Column(name = "secret_id")
    private String secretId;
    private Integer version;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Lob
    private String data;
    private SecretTypeEnum type;
    @OneToMany(
            mappedBy = "secret",
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<UserSecretDao> users;
}
