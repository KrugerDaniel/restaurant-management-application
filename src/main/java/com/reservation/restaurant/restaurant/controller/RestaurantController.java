package com.reservation.restaurant.restaurant.controller;

import com.reservation.restaurant.restaurant.domain.dto.RestaurantDto;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantInformationDto;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService service;

    @Autowired
    public RestaurantController(final RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return ResponseEntity.ok(service.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getRestaurantById(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RestaurantDto> insertRestaurant(@RequestBody @Valid RestaurantInformationDto restaurantToInsert) {
        final Restaurant restaurant = service.insertRestaurant(restaurantToInsert);
        final URI uri = UriComponentsBuilder.fromPath("/restaurant/{id}").buildAndExpand(restaurant.getIdRestaurant()).toUri();

        return ResponseEntity.created(uri).body(new RestaurantDto(restaurant));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        service.deleteRestaurant(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable UUID id,
                                                          @RequestBody @Valid RestaurantDto restaurantToUpdate) {
        return ResponseEntity.ok(service.updateRestaurant(id, restaurantToUpdate));
    }
}
