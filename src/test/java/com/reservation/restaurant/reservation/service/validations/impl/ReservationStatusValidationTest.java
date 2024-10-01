package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationStatusValidationTest {

    private final ReservationStatusValidation reservationStatusValidation = new ReservationStatusValidation();

    @Test
    @DisplayName("Validate if an exception is raised when status is canceled")
    void verifyValidationExceptionStatusCanceled() {
        final Reservation reservation = new Reservation();
        reservation.setStatus(Status.CANCELED);

        assertThrows(ValidationException.class, () -> reservationStatusValidation.validate(reservation));
    }

    @Test
    @DisplayName("Validate if no exception is raised")
    void verifyNoExceptionIsRaised() {
        final Reservation reservation = new Reservation();
        reservation.setStatus(Status.CONFIRMED);

        assertDoesNotThrow(() -> reservationStatusValidation.validate(reservation));
    }
}