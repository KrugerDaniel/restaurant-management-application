package com.reservation.restaurant.reservation.domain.dto;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTablesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailDto {

    private UUID id;
    private LocalDate dateReservation;
    private LocalTime timeReservation;
    private Status status;
    private Integer capacity;

    public ReservationDetailDto(final Reservation reservation) {
        this(reservation.getIdReservation(),
                reservation.getDateReservation(),
                reservation.getTimeReservation(),
                reservation.getStatus(),
                reservation.getCapacity());
    }
}
