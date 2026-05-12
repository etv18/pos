package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.dtos.stats.MonthlySum;
import com.tavarlabs.pos.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // If duplicated invoices show up at DISTINCT after the SELECT statement
    @Query("SELECT inv FROM Invoice inv LEFT JOIN FETCH inv.lines")
    List<Invoice> findAllWithLines();

    @Query("SELECT inv FROM Invoice inv JOIN FETCH inv.lines WHERE inv.code = :code")
    Optional<Invoice> findByCode(@Param("code") String code);

    @Query(
            "SELECT new com.tavarlabs.pos.dtos.stats.MonthlySum(MONTH(i.createdAt), SUM(i.total)) " +
                    "FROM Invoice i " +
                    "WHERE YEAR(i.createdAt) = :year " +
                    "GROUP BY MONTH(i.createdAt) " +
                    "ORDER BY MONTH(i.createdAt) ASC"
    )
    List<MonthlySum> getMonthlySalesTotals(@Param("year") int year);

    @Query("SELECT SUM(i.total) FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end")
    Optional<BigDecimal> totalSalesOfACertainTimeFrame(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    boolean existsByCode(String code);
}
