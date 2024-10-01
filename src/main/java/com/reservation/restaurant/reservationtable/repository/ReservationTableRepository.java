package com.reservation.restaurant.reservationtable.repository;

import com.reservation.restaurant.reservationtable.domain.entity.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationTableRepository extends JpaRepository<ReservationTable, UUID> {

    @Query("""
            select rt
            from   ReservationTable rt
            where  rt.reservation.idReservation = :reservation
            """)
    List<ReservationTable> findReservationTableByReservationId(final UUID reservation);

    @Query("""
            select (count(rt) > 0)
            from ReservationTable rt
            where rt.reservation.dateReservation = :date
              and rt.table.idTable = :table
            """)
    boolean existsReservationTable(final LocalDate date, final UUID table);

    @Query("""
            select rt
            from ReservationTable rt
            where rt.reservation.dateReservation = :date
            """)
    List<ReservationTable> findAllReservationTableAva(final LocalDate date);
}
