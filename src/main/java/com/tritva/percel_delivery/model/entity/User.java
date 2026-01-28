package com.tritva.percel_delivery.model.entity;

import com.tritva.percel_delivery.model.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cellNo;

    @Column(nullable = false)
    private String password;

    // Optional: Link to rider profile if this user is a rider
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Rider riderProfile;
}
