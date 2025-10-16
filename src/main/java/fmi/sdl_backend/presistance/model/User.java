package fmi.sdl_backend.presistance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User extends SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    
    @CreationTimestamp
    private OffsetDateTime createdAt;

    public User(String email, String fullName) {
        this.email = email;
        this.firstName = fullName.split(" ")[0];
        this.lastName = fullName.split(" ")[1];
    }
}