
package com.mathan.nimbusvault.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mathan.nimbusvault.enums.AuthProvider;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "user_auth_providers",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"provider", "provider_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class UserAuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
