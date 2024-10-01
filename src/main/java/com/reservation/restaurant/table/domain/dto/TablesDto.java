package com.reservation.restaurant.table.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TablesDto {

    @NotNull
    private List<Integer> tables;

    @NotNull
    private UUID restaurant;
}
