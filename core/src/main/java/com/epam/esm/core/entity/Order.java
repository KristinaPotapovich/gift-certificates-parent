package com.epam.esm.core.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Component
@Table(name = "order_table")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private long id;
    @NonNull
    @Column(name = "price")
    private BigDecimal price;
    @NonNull
    @Column(name = "time_of_purchase")
    private LocalDateTime timeOfPurchase;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE} )
    @JoinTable(name = "orders_certificates",
            joinColumns = {@JoinColumn(name = "id_order")},
            inverseJoinColumns = {@JoinColumn(name = "id_certificate")})
    private List<GiftCertificate> certificates;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
