package com.reservation.restaurant.reservation.service.validations;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.exceptions.ValidationException;

public interface ReservationValidation {

    void validate(final Reservation reservation) throws ValidationException;
}
