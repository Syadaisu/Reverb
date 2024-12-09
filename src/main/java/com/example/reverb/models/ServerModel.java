import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Servers")
public class ServerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 32, nullable = false, unique = true)
    private String name;

    @Size(max = 128)
    private String description;

    private UUID pictureId;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private String ownerId;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Usr owner;

    @OneToMany(mappedBy = "server")
    private Set<Usr> users;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getPictureId() {
        return pictureId;
    }

    public void setPictureId(UUID pictureId) {
        this.pictureId = pictureId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Usr getOwner() {
        return owner;
    }

    public void setOwner(Usr owner) {
        this.owner = owner;
    }

    public Set<Usr> getUsers() {
        return users;
    }

    public void setUsers(Set<Usr> users) {
        this.users = users;
    }
}
