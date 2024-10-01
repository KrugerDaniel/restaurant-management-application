package com.reservation.restaurant.restaurant.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantDto;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantInformationDto;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.restaurant.repository.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public RestaurantService(final RestaurantRepository repository, final ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RestaurantDto> getAllRestaurants() {
        return repository.findAll().stream().map(RestaurantDto::new).toList();
    }

    public Restaurant getById(final UUID id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public RestaurantDto getRestaurantById(final UUID id) throws ValidationException {
        return new RestaurantDto(getById(id));
    }

    public Restaurant insertRestaurant(final RestaurantInformationDto restaurantToInsert) {
        final boolean existsRestaurant = repository.existsRestaurantByNameAndBranch(restaurantToInsert.getName(), restaurantToInsert.getBranch());

        if (existsRestaurant) {
            throw new ValidationException("Invalid restaurant");
        }
        final Restaurant restaurant = new Restaurant(restaurantToInsert);
        return repository.save(restaurant);
    }

    public void deleteRestaurant(final UUID id) {
        final Restaurant restaurant = getById(id);

        repository.delete(restaurant);
    }

    public RestaurantDto updateRestaurant(final UUID id, final RestaurantDto restaurantToUpdate) {
        final Restaurant restaurant = getById(id);
        mapper.map(restaurantToUpdate, restaurant);

        return new RestaurantDto(repository.save(restaurant));
    }
}
