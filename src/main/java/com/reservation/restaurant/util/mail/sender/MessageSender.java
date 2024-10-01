package com.reservation.restaurant.util.mail.sender;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;

import java.time.LocalDateTime;

public interface MessageSender {

    String getMessage(final Reservation reservation, final Restaurant restaurant, final LocalDateTime start,
                      final LocalDateTime end, Client client);
}
