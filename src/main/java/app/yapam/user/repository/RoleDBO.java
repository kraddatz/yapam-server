package app.yapam.user.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "role")
public class RoleDBO {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private String id;
	@Column(name = "name")
	private String name;

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "role"
	)
	@JsonIgnore
	private Set<UserDBO> user;
}
