package com.reservation.restaurant.reservation.domain.entity;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Table(name = "reservation")
@Entity(name = "Reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idReservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idReservation;
    private LocalDate dateReservation;
    private LocalTime timeReservation;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idRestaurant")
    private Restaurant restaurant;
}
