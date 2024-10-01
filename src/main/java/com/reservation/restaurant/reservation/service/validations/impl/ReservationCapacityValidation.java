package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.service.validations.ReservationValidation;
import org.springframework.stereotype.Component;

@Component
public class ReservationCapacityValidation implements ReservationValidation {

    @Override
    public void validate(final Reservation reservation) throws ValidationException {
        if (reservation.getCapacity() > 10) {
            throw new ValidationException("Reservation capacity higher than expected");
        }
        if (reservation.getCapacity().equals(0)) {
            throw new ValidationException("Reservation capacity must not be zero");
        }
    }
}
