package com.reservation.restaurant.client.service;

import com.reservation.restaurant.client.domain.dto.ClientDto;
import com.reservation.restaurant.client.domain.dto.ClientInformationDto;
import com.reservation.restaurant.client.domain.dto.ClientReservationsDto;
import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.client.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ClientService(final ClientRepository repository, final ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ClientDto> getAllClients() {
        return repository.findAll().stream().map(ClientDto::new).toList();
    }

    public Client getById(final UUID id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public ClientReservationsDto getClientById(final UUID id) {
        return new ClientReservationsDto(getById(id));
    }

    public Client insertClient(final ClientInformationDto client) {
        final boolean clients = repository.existsByCpf(client.getCpf());

        if (clients) {
            throw new ValidationException("Client already registered");
        }
        final Client newClient = new Client(client);
        return repository.save(newClient);
    }

    public void deleteClient(final UUID id) {
        final Client client = getById(id);
        repository.delete(client);
    }

    public ClientDto updateClient(final UUID id, ClientDto dataToUpdate) {
        final Client client = getById(id);
        mapper.map(dataToUpdate, client);

        return new ClientDto(client);
    }
}
