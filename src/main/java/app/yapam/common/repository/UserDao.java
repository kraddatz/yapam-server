package app.yapam.common.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserDao implements UserDetails {

    @Id
    private String id;
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
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private transient Set<UserSecretDao> secrets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
