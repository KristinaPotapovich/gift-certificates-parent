package com.epam.esm.core.entity;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Component
@Table(name = "gift_certificate")
@Entity
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificate")
    private long id;
    @NonNull
    @Column(name = "name")
    private String name;
    @NonNull
    @Column(name = "description")
    private String description;
    @NonNull
    @Column(name = "price")
    private double price;
    @NonNull
    @Column(name = "duration")
    private int durationInDays;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "id_certificate", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "id_tag", nullable = false)})
    private List<Tag> tags;
}
