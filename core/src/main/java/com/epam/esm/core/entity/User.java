package com.epam.esm.core.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Audited
@Component
@Table(name = "user")
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
    @OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY,mappedBy = "user")
    private List<Order> orders;
}
