package com.reservation.restaurant.reservationtable.domain.entity;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@jakarta.persistence.Table(name = "reservation_table")
@Entity(name = "ReservationTable")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idReservationTable")
public class ReservationTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idReservationTable;

    @ManyToOne
    @JoinColumn(name = "idReservation")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "idTable")
    private com.reservation.restaurant.table.domain.entity.Table table;
}
