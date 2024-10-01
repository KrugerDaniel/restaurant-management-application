package com.reservation.restaurant.restaurant.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInformationDto {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer branch;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
