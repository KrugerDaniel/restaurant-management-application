package com.reservation.restaurant.client.domain.entity;

import com.reservation.restaurant.client.domain.dto.ClientInformationDto;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "client")
@Entity(name = "Client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idClient")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idClient;
    private String name;
    private String email;
    private String phone;
    private String cpf;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Client(final ClientInformationDto client) {
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.cpf = client.getCpf();
    }
}
