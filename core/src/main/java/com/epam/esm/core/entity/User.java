package com.epam.esm.core.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

/**
 * User Entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Component
@Table(name = "user_table")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long id;
    @NonNull
    @Column(name = "login", unique = true)
    private String login;
    @NonNull
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role userRole;
    @OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Order> orders;
}
