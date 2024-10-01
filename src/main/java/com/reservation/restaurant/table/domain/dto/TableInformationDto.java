package com.reservation.restaurant.table.domain.dto;

import com.reservation.restaurant.table.domain.entity.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableInformationDto {

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    private UUID restaurant;

    public TableInformationDto(final Table table) {
        this(table.getCapacity(), table.getRestaurant().getIdRestaurant());
    }
}
