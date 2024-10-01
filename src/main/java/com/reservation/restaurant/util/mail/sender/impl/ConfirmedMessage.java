package com.reservation.restaurant.util.mail.sender.impl;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.util.Utils;
import com.reservation.restaurant.util.mail.sender.MessageSender;

import java.time.LocalDateTime;

public class ConfirmedMessage implements MessageSender {

    @Override
    public String getMessage(final Reservation reservation, final Restaurant restaurant, final LocalDateTime start,
                             final LocalDateTime end, Client client) {
        return "<div style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: " +
                "center; align-items: center; height: 100vh;\">" +
                "<div style=\"background: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: " +
                "20px; max-width: 500px; width: 100%; text-align: center;\">" +
                "<div style=\"font-size: 1.2em; color: #28a745; margin-bottom: 20px;\">Reservation Confirmed!</div>" +
                "<p style=\"font-size: 1em; line-height: 1.5; color: #333;\">" +
                "Great news! Your reservation at <strong>" + restaurant.getName() + "</strong> has been successfully " +
                "confirmed. Here are the details of your booking:" +
                "</p>" +
                "<p style=\"font-size: 1em; line-height: 1.5; color: #333;\">" +
                "<strong>Status:</strong> <span style=\"color: #28a745;\">" + reservation.getStatus() + "</span><br>" +
                "<strong>Start Date:</strong> " + Utils.formatDateTime(start) + "<br>" +
                "<strong>End Date:</strong> " + Utils.formatDateTime(end) + "<br>" +
                "<strong>Number of People:</strong> " + reservation.getCapacity() + "<br>" +
                "<strong>Customer Name:</strong> " + client.getName() +
                "</p>" +
                "<div style=\"margin-top: 20px; font-size: 1em; color: #555;\">" +
                "<p>Thank you for booking with us! We’re excited to host you and make your experience memorable. If you " +
                "have any questions or need to make changes, don’t hesitate to reach out.</p>" +
                "</div>" +
                "</div>" +
                "</div>";
    }
}
