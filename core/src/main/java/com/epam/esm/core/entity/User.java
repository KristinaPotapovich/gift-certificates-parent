package com.epam.esm.core.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Component
@Table(name = "user", schema = "mentoring_certificate")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long id;
    @NonNull
    @Column(name = "login",unique = true)
    private String login;
    @NonNull
    @Column(name = "password")
    private String password;
    @NonNull
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
