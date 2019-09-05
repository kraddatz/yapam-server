package app.yapam.common.repository;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "file")
public class FileDao extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String filename;
    @ManyToMany
    @JoinTable(
            name = "secret_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "secret_id")
    )
    private List<SecretDao> secrets;
    @Column(name = "filesize")
    private Long filesize;
    private String hash;
    private String mimetype;
}
