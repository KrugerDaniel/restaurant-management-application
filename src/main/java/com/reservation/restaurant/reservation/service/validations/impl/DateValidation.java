package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.service.validations.ReservationValidation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateValidation implements ReservationValidation {

    @Override
    public void validate(final Reservation reservation) throws ValidationException {
        if (reservation.getDateReservation().isBefore(LocalDate.now())) {
            throw new ValidationException("Invalid date");
        }
    }
}
