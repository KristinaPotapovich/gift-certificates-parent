package com.epam.esm.core.entity;


import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Gift certificate Entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Audited
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
    @Column(name = "price")
    private BigDecimal price;
    @NonNull
    @Column(name = "duration")
    private int durationInDays;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "id_certificate", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "id_tag", nullable = false)})
    private List<Tag> tags;
    @NonNull
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
