import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Usr implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;  // The IdentityUser class in C# uses a string for Id

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private UUID profilePic;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private boolean isRemoved;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(mappedBy = "users")
    private Set<Server> servers;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(UUID profilePic) {
        this.profilePic = profilePic;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Server> getServers() {
        return servers;
    }

    public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

    // Spring Security methods

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isRemoved;  // Assume removed means expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isRemoved;  // Assume removed means locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isRemoved;  // Assume removed means expired credentials
    }

    @Override
    public boolean isEnabled() {
        return !isRemoved;  // Assume removed means disabled
    }
}
