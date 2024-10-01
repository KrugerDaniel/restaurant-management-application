package com.reservation.restaurant.reservation.repository;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import com.reservation.restaurant.reservationtable.domain.entity.ReservationTable;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("""
            select (count(r) > 0)
            from   Reservation r
            where  r.client = :client
              and  r.restaurant = :restaurant
              and  r.dateReservation = :dateReservation
              and  r.status != 'CANCELED'
            """)
    boolean existsReservation(LocalDate dateReservation, Client client, Restaurant restaurant);

    @Query("""
            select rt
            from   ReservationTable rt
            where  rt.reservation.idReservation = :id
              and  rt.reservation.dateReservation = :dateReservation
            """)
    List<ReservationTable> findAllTablesReservedByReservation(@Param("id") UUID reservationIdentification, LocalDate dateReservation);

    @Query("""
            select r
            from   Reservation r
            where  r.status = :status
              and  (coalesce(:beginDate, r.dateReservation) <= r.dateReservation)
              and  (coalesce(:endDate, r.dateReservation) >= r.dateReservation)
            """)
    List<Reservation> getAllReservationsByStatus(Status status, LocalDate beginDate, LocalDate endDate);

    @Query("""
            select r
            from   Reservation r
            where  r.client.idClient = :id
            """)
    List<Reservation> findAllReservationByClient(UUID id);
}
