package com.reservation.restaurant.reservationtable.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTableCapacityDto {

    @NotNull
    private UUID table;

    @NotNull
    @Positive
    private Integer capacity;
}
