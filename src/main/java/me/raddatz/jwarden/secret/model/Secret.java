package me.raddatz.jwarden.secret.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.raddatz.jwarden.user.model.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "secret")
@Getter
@Setter
public class Secret {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    private Integer version;
    @Lob
    private String data;
    private SecretType type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
