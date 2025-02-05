package com.ll.commars.tmp;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
/*
@Entity
@Table(name = "SPRING_SESSION")
@Data
public class SpringSession {

    @Id
    @Column(name = "PRIMARY_ID", nullable = false, length = 36)
    private String primaryId;

    @Column(name = "SESSION_ID", nullable = false, length = 36, unique = true)
    private String sessionId;

    @Column(name = "CREATION_TIME", nullable = false)
    private Long creationTime;

    @Column(name = "LAST_ACCESS_TIME", nullable = false)
    private Instant lastAccessTime;

    @Column(name = "MAX_INACTIVE_INTERVAL", nullable = false)
    private Integer maxInactiveInterval;

    @Column(name = "EXPIRY_TIME", nullable = false)
    private Long expiryTime;

    @Column(name = "PRINCIPAL_NAME", length = 100)
    private String principalName;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SpringSessionAttribute> attributes = new HashSet<>();
}

*/