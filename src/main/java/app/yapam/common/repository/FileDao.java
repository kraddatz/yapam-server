package app.yapam.common.repository;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
@Setter
@Table(name = "file")
public class FileDao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String filename;
    @ManyToOne
    @JoinColumn(name = "secret_id")
    private SecretDao secret;
    @Column(name = "filesize")
    private Long filesize;
    private String hash;
}
