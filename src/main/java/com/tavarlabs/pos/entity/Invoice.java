package com.tavarlabs.pos.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private double total;

    /*
    * Bidirectional OneToMany relationship
    *
    * Invoice is the inverse (non owning) side of the relationship with InvoiceLine.
    * The owning side is InvoiceLine.invoice, which contains the foreign key.
    * "mappedBy" must match the field name in InvoiceLine class.
    * */
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceLine> lines;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
