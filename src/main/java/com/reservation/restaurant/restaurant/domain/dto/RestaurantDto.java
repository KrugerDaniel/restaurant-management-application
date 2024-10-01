package com.reservation.restaurant.restaurant.domain.dto;

import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    private UUID id;
    private String name;
    private Integer branch;
    private String email;

    public RestaurantDto(final Restaurant restaurant) {
        this(restaurant.getIdRestaurant(), restaurant.getName(), restaurant.getBranch(), restaurant.getEmail());
    }
}
