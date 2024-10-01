package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationCapacityValidationTest {

    private final ReservationCapacityValidation reservationCapacityValidation = new ReservationCapacityValidation();

    private static Stream<Arguments> argumentsForCapacity() {
        return Stream.of(
                Arguments.of(11),
                Arguments.of(0)
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsForCapacity")
    @DisplayName("Validate if an exception is raised when the capacity is outside the limits")
    void verifyValidationExceptionCapacityBeyondTen(final Integer capacity) {
        final Reservation reservation = new Reservation();
        reservation.setCapacity(capacity);

        assertThrows(ValidationException.class, () -> reservationCapacityValidation.validate(reservation));
    }

    private static Stream<Arguments> argumentsCapacityValid() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(5),
                Arguments.of(10)
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsCapacityValid")
    @DisplayName("Validate if no exception is raised")
    void verifyNoExceptionIsRaised(final Integer capacity) {
        final Reservation reservation = new Reservation();
        reservation.setCapacity(capacity);

        assertDoesNotThrow(() -> reservationCapacityValidation.validate(reservation));
    }
}