package com.tavarlabs.pos.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String clientName;

    /*
    * Bidirectional OneToMany relationship
    *
    * Invoice is the inverse (non owning) side of the relationship with InvoiceLine.
    * The owning side is InvoiceLine.invoice, which contains the foreign key.
    * "mappedBy" must match the field name in InvoiceLine class.
    * */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceLine> lines;

    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateTotal();
    }

    public void calculateTotal() {
        if(this.lines == null || this.lines.isEmpty()) {
            this.total = BigDecimal.ZERO;
        }

        this.total = lines.stream().map(line -> {
            double priceAsDouble = line.getProduct().getPrice();
            BigDecimal price = BigDecimal.valueOf(priceAsDouble);
            return price.multiply(BigDecimal.valueOf(line.getQuantity()));
        })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
