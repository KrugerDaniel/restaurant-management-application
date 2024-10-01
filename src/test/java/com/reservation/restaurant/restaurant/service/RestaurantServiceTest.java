package com.reservation.restaurant.restaurant.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantDto;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantInformationDto;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository repository;

    @Mock
    private ModelMapper mapper;

    @Spy
    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ensure all restaurants are got")
    void verifyGetAllRestaurants() {
        final UUID id = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant();

        restaurant.setIdRestaurant(id);

        when(repository.findAll()).thenReturn(List.of(restaurant));

        final List<RestaurantDto> response = restaurantService.getAllRestaurants();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(id, response.get(0).getId());
    }

    @Test
    @DisplayName("Ensure the correct restaurant is got")
    void verifyGetRestaurantById() {
        final UUID id = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant();

        restaurant.setIdRestaurant(id);

        when(repository.findById(id)).thenReturn(Optional.of(restaurant));

        final Restaurant response = restaurantService.getById(id);

        assertNotNull(response);
        assertEquals(id, response.getIdRestaurant());
    }

    @Test
    @DisplayName("Validate if a not found exception is raised when no client is found")
    void verifyNotFoundExceptionGetClientById() {
        final UUID id = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> restaurantService.getById(id));
    }

    @Test
    @DisplayName("Ensure get restaurant by id")
    void verifyGetRestaurant() {
        final Restaurant restaurant = new Restaurant();
        final UUID id = UUID.randomUUID();

        restaurant.setIdRestaurant(id);

        doReturn(restaurant).when(restaurantService).getById(id);

        final RestaurantDto response = restaurantService.getRestaurantById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Ensure correct restaurant insert")
    void verifyRestaurantInsertion() {
        final RestaurantInformationDto restaurantInformation = new RestaurantInformationDto("name", 1, "test", "test");
        final Restaurant restaurant = new Restaurant(restaurantInformation);

        when(repository.save(any())).thenReturn(restaurant);

        final Restaurant response = restaurantService.insertRestaurant(restaurantInformation);

        verify(repository).save(any());

        assertNotNull(response);
        assertEquals(restaurantInformation.getName(), response.getName());
        assertEquals(restaurantInformation.getBranch(), response.getBranch());
    }

    @Test
    @DisplayName("Validate exception for a restaurant already exists")
    void verifyRestaurantAlreadyExists() {
        final RestaurantInformationDto restaurantInformation = new RestaurantInformationDto("name", 1, "test", "test");

        when(repository.existsRestaurantByNameAndBranch(restaurantInformation.getName(), restaurantInformation.getBranch())).thenReturn(true);

        assertThrows(ValidationException.class, () -> restaurantService.insertRestaurant(restaurantInformation));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Ensure correct delete of restaurant")
    void verifyDeleteRestaurant() {
        final Restaurant restaurant = new Restaurant();
        final UUID id = UUID.randomUUID();

        restaurant.setIdRestaurant(id);

        doReturn(restaurant).when(restaurantService).getById(id);

        restaurantService.deleteRestaurant(id);

        verify(repository).delete(restaurant);
    }

    @Test
    @DisplayName("Ensure correct restaurant update")
    void verifyRestaurantUpdate() {
        final UUID id = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant();
        restaurant.setIdRestaurant(id);
        final RestaurantDto restaurantDto = new RestaurantDto(restaurant);

        doReturn(restaurant).when(restaurantService).getById(id);
        when(repository.save(restaurant)).thenReturn(restaurant);

        final RestaurantDto response = restaurantService.updateRestaurant(id, restaurantDto);

        verify(mapper).map(restaurantDto, restaurant);
        verify(repository).save(restaurant);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }
}