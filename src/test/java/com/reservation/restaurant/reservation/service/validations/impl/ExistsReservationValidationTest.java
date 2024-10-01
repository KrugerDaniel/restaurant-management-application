package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ExistsReservationValidationTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ExistsReservationValidation existsReservationValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Validate is the exception is raised")
    void verifyValidationException() {
        final Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDate.now());
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());

        when(reservationRepository.existsReservation(reservation.getDateReservation(), reservation.getClient(),
                reservation.getRestaurant())).thenReturn(true);

        assertThrows(ValidationException.class, () -> existsReservationValidation.validate(reservation));
    }

    @Test
    @DisplayName("Validate if no exception is raised")
    void verifyNoExceptionIsRaised() {
        assertDoesNotThrow(() -> existsReservationValidation.validate(new Reservation()));
    }
}