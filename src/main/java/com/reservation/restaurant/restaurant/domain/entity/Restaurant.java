package com.reservation.restaurant.restaurant.domain.entity;

import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.restaurant.domain.dto.RestaurantInformationDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "restaurant")
@Entity(name = "Restaurant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idRestaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRestaurant;
    private String name;
    private Integer branch;
    private String email;
    private String password;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Restaurant(RestaurantInformationDto restaurant) {
        setName(restaurant.getName());
        setBranch(restaurant.getBranch());
        setEmail(restaurant.getEmail());
        setPassword(restaurant.getPassword());
    }
}
