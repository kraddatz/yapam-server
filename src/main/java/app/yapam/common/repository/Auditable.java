package app.yapam.common.repository;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class Auditable {

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserDao createdBy;

    @Column(name = "creation_date")
    @CreatedDate
    private LocalDateTime creationDate;
}
