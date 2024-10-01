package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.service.validations.ReservationValidation;
import org.springframework.stereotype.Component;

@Component
public class ReservationStatusValidation implements ReservationValidation {

    @Override
    public void validate(final Reservation reservation) throws ValidationException {
        if (reservation.getStatus().equals(Status.CANCELED)) {
            throw new ValidationException("Invalid Status");
        }
    }
}
