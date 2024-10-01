package com.reservation.restaurant.client.domain.dto;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.dto.ReservationDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientReservationsDto {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private List<ReservationDetailDto> reservations;

    public ClientReservationsDto(final Client client) {
        this(client.getIdClient(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getCpf(),
                client.getReservations().stream().map(ReservationDetailDto::new).toList());
    }
}
