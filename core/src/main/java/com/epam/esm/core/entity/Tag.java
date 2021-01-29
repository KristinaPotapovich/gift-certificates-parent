package com.epam.esm.core.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Audited
@Entity
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
