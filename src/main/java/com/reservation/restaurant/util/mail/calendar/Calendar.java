package com.reservation.restaurant.util.mail.calendar;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.util.Utils;

import java.time.LocalDateTime;

public class Calendar {

    private Calendar() {}

    public static String getCalendarText(final Reservation reservation, final Restaurant restaurant, final LocalDateTime start,
                                   final LocalDateTime end) {
        return "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//hacksw/handcal//NONSGML v1.0//EN\n" +
                "BEGIN:VEVENT\n" +
                "SUMMARY:Reservation\n" +
                "DESCRIPTION:Reservation made for " + reservation.getCapacity() + "\n" +
                "Status: " + reservation.getStatus() + "\n" +
                "LOCATION:" + restaurant.getName() + "\n" +
                "DTSTART:" + Utils.formatTimeZone(start) + "\n" +
                "DTEND:" + Utils.formatTimeZone(end) + "\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";
    }
}
