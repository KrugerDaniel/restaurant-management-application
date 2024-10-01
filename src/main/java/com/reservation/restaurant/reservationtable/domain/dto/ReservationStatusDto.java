package com.reservation.restaurant.reservationtable.domain.dto;

import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStatusDto {

    @NotNull
    private Status status;
}
