package com.reservation.restaurant.client.controller;

import com.reservation.restaurant.client.domain.dto.ClientDto;
import com.reservation.restaurant.client.domain.dto.ClientInformationDto;
import com.reservation.restaurant.client.domain.dto.ClientReservationsDto;
import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.client.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(final ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(service.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientReservationsDto> getClientById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getClientById(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ClientDto> insertClient(@RequestBody @Valid ClientInformationDto client) {
        final Client newClient = service.insertClient(client);
        final URI uri = UriComponentsBuilder.fromPath("/client/{id}").buildAndExpand(newClient.getIdClient()).toUri();

        return ResponseEntity.created(uri).body(new ClientDto(newClient));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        service.deleteClient(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ClientDto> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientDto client) {
        return ResponseEntity.ok(service.updateClient(id, client));
    }
}
