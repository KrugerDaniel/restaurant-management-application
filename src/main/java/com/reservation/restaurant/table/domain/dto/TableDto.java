package com.reservation.restaurant.table.domain.dto;

import com.reservation.restaurant.restaurant.domain.dto.RestaurantDto;
import com.reservation.restaurant.table.domain.entity.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableDto {

    private UUID id;
    private Integer capacity;
    private RestaurantDto restaurant;

    public TableDto(final Table table) {
        this(table.getIdTable(), table.getCapacity(), new RestaurantDto(table.getRestaurant()));
    }
}
