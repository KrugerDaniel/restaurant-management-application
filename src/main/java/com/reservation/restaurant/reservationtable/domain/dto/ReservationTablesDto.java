package com.reservation.restaurant.reservationtable.domain.dto;

import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTablesDto {

    @NotNull
    private LocalDate dateReservation;

    @NotNull
    private LocalTime timeReservation;

    @NotNull
    private Status status;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    private UUID client;

    @NotNull
    private UUID restaurant;

    @NotNull
    private List<UUID> tables;
}
