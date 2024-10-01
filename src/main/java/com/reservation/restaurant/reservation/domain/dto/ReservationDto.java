package com.reservation.restaurant.reservation.domain.dto;

import com.reservation.restaurant.client.domain.dto.ClientDto;
import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantDto;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private UUID id;
    private LocalDate dateReservation;
    private LocalTime timeReservation;
    private Status status;
    private Integer capacity;
    private ClientDto client;
    private RestaurantDto restaurant;

    public ReservationDto(final Reservation reservation) {
        this(reservation.getIdReservation(),
                reservation.getDateReservation(),
                reservation.getTimeReservation(),
                reservation.getStatus(),
                reservation.getCapacity(),
                new ClientDto(reservation.getClient()),
                new RestaurantDto(reservation.getRestaurant()));
    }

    public ReservationDto(final ReservationDetailDto reservationDetailDto) {
        this(reservationDetailDto.getId(),
                reservationDetailDto.getDateReservation(),
                reservationDetailDto.getTimeReservation(),
                reservationDetailDto.getStatus(),
                reservationDetailDto.getCapacity(),
                null,
                null);
    }
}
