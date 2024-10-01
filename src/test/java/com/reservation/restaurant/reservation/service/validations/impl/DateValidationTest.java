package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateValidationTest {

    private final DateValidation dateValidation = new DateValidation();

    @Test
    @DisplayName("Validate if an exception is raised when date is before actual")
    void verifyExceptionDateIsBeforeActual() {
        final Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDate.now().minusDays(1));

        assertThrows(ValidationException.class, () -> dateValidation.validate(reservation));
    }

    private static Stream<Arguments> argumentsOfValidDate() {
        final LocalDate date = LocalDate.now();
        return Stream.of(
                Arguments.of(date),
                Arguments.of(date.plusDays(1))
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsOfValidDate")
    @DisplayName("Validate if an exception is not raised when the date is the same or above")
    void verifyExceptionSameAndAbove(final LocalDate date) {
        final Reservation reservation = new Reservation();
        reservation.setDateReservation(date);

        assertDoesNotThrow(() -> dateValidation.validate(reservation));
    }
}