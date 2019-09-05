package app.yapam.common.repository;

import app.yapam.secret.model.SecretTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "secret")
public class SecretDao extends Auditable {

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
    @Lob
    private String data;
    private SecretTypeEnum type;
    @OneToMany(
            mappedBy = "secret",
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private List<UserSecretDao> users;
    @ManyToMany(
            mappedBy = "secrets",
            cascade = CascadeType.MERGE
    )
    private List<FileDao> files;
    @ManyToMany(
            cascade = CascadeType.MERGE
    )
    @JoinTable(
            name = "secret_tag",
            joinColumns = @JoinColumn(name = "secret_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagDao> tags;
}
