package com.reservation.restaurant.reservation.service.validations.impl;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.reservation.service.validations.ReservationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsReservationValidation implements ReservationValidation {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ExistsReservationValidation(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void validate(final Reservation reservation) throws ValidationException {
        final boolean reservations = reservationRepository.existsReservation(reservation.getDateReservation(),
                reservation.getClient(), reservation.getRestaurant());

        if (reservations) {
            throw new ValidationException("The client already has a reservation for this date");
        }
    }
}
