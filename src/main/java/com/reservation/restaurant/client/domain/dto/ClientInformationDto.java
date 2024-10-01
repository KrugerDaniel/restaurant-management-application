package com.reservation.restaurant.client.domain.dto;

import com.reservation.restaurant.client.domain.entity.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientInformationDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String cpf;

    public ClientInformationDto(final Client client) {
        this(client.getName(), client.getEmail(), client.getPhone(), client.getCpf());
    }
}
