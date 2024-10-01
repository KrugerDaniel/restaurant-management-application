package com.reservation.restaurant.client.domain.dto;

import com.reservation.restaurant.client.domain.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String cpf;

    public ClientDto(Client client) {
        this(client.getIdClient(), client.getName(), client.getEmail(), client.getPhone(), client.getCpf());
    }
}
