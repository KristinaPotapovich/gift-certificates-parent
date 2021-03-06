package com.epam.esm.core.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Tag Entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Audited
@Entity
@Component
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    private long id;
    @NonNull
    @NotBlank
    @Column(name = "name", unique = true)
    private String name;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<GiftCertificate> certificates;
}
