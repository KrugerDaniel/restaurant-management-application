package com.reservation.restaurant.util.mail;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.util.mail.calendar.Calendar;
import com.reservation.restaurant.util.mail.sender.MessageSender;
import com.reservation.restaurant.util.mail.sender.impl.CanceledMessage;
import com.reservation.restaurant.util.mail.sender.impl.ConfirmedMessage;
import com.reservation.restaurant.util.mail.sender.impl.PendingMessage;
import org.simplejavamail.api.email.CalendarMethod;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Mail {

    private static final String SMTP_GMAIL_COM = "smtp.gmail.com";

    public void sendEmail(final Reservation reservation, final MessageSender messageSender) {
        final Restaurant restaurant = reservation.getRestaurant();
        final Client client = reservation.getClient();
        final LocalDateTime start = LocalDateTime.of(reservation.getDateReservation(), reservation.getTimeReservation());
        final LocalDateTime end = start.plusMinutes(15);

        final Mailer mailer = MailerBuilder.withSMTPServer(SMTP_GMAIL_COM, 587, restaurant.getEmail(), restaurant.getPassword())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        final EmailPopulatingBuilder email = EmailBuilder.startingBlank()
                .from("Restaurant", restaurant.getEmail())
                .to(client.getName(), client.getEmail())
                .withSubject("Restaurant reservation")
                .withHTMLText(messageSender.getMessage(reservation, restaurant, start, end, client));

        if (!(messageSender instanceof CanceledMessage)) {
            email.withCalendarText(CalendarMethod.REQUEST, Calendar.getCalendarText(reservation, restaurant, start, end));
        }

        mailer.sendMail(email.buildEmail());
    }

    public void sendEmailConfirmedReservation(final Reservation reservation) {
        sendEmail(reservation, new ConfirmedMessage());
    }

    public void sendEmailCanceledReservation(final Reservation reservation) {
        sendEmail(reservation, new CanceledMessage());
    }

    public void sendEmailPendingReservation(final Reservation reservation) {
        sendEmail(reservation, new PendingMessage());
    }
}
