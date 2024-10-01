package com.reservation.restaurant.client.service;

import com.reservation.restaurant.client.domain.dto.ClientDto;
import com.reservation.restaurant.client.domain.dto.ClientInformationDto;
import com.reservation.restaurant.client.domain.dto.ClientReservationsDto;
import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.client.repository.ClientRepository;
import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository repository;

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        this.repository = mock(ClientRepository.class);
        this.mapper = mock(ModelMapper.class);
        this.clientService = new ClientService(repository, mapper);
    }

    @Test
    @DisplayName("Ensure find all clients")
    void verifyFindAllClients(){
        final Client client1 = new Client();
        final Client client2 = new Client();
        final Client client3 = new Client();

        client1.setIdClient(UUID.randomUUID());
        client2.setIdClient(UUID.randomUUID());
        client3.setIdClient(UUID.randomUUID());

        final List<Client> clients = List.of(client1, client2, client3);

        when(repository.findAll()).thenReturn(clients);

        final List<ClientDto> response = clientService.getAllClients();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals(client1.getIdClient(), response.get(0).getId());
        assertEquals(client2.getIdClient(), response.get(1).getId());
        assertEquals(client3.getIdClient(), response.get(2).getId());
    }

    @Test
    @DisplayName("Ensure getting client by id")
    void verifyGettingClientById() {
        final UUID id = UUID.randomUUID();
        final Client client = new Client();
        client.setIdClient(id);

        when(repository.findById(id)).thenReturn(Optional.of(client));

        final Client response = clientService.getById(id);

        assertNotNull(response);
        assertEquals(client.getIdClient(), response.getIdClient());
    }

    @Test
    @DisplayName("Validates if an exception is raised when a client is not found")
    void verifyNotFoundExceptionClient() {
        final UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClientById(id));
    }

    @Test
    @DisplayName("Ensure correct get of client")
    void verifyCorrectGetClient() {
        final UUID id = UUID.randomUUID();
        final Client client = new Client();
        client.setIdClient(id);
        client.setReservations(new ArrayList<>());

        when(repository.findById(id)).thenReturn(Optional.of(client));

        final ClientReservationsDto response = clientService.getClientById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Validate if an exception is raised when the client already exists")
    void verifyValidationExceptionRaisedClientFound() {
        final ClientInformationDto client = new ClientInformationDto(null, null, null, "12345");

        when(repository.existsByCpf(client.getCpf())).thenReturn(true);

        assertThrows(ValidationException.class, () -> clientService.insertClient(client));
    }

    @Test
    @DisplayName("Ensure that the new client is inserted")
    void verifyNewClientInserted() {
        final ClientInformationDto clientDto = new ClientInformationDto("Name", null, null, null);
        final Client client = new Client();
        client.setIdClient(UUID.randomUUID());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);

        when(repository.save(any())).thenReturn(client);

        final Client response = clientService.insertClient(clientDto);

        verify(repository).save(captor.capture());

        assertNotNull(response);
        assertEquals(client.getIdClient(), response.getIdClient());
        assertEquals(clientDto.getName(), captor.getValue().getName());
    }

    @Test
    @DisplayName("Ensure correct delete of client")
    void verifyDeleteClient() {
        final UUID id = UUID.randomUUID();
        final Client client = new Client();
        client.setIdClient(id);
        client.setReservations(new ArrayList<>());

        when(repository.findById(id)).thenReturn(Optional.of(client));

        clientService.deleteClient(id);

        verify(repository).delete(client);
    }

    @Test
    @DisplayName("Ensure correct update of client")
    void verifyCorrectUpdateOfClient() {
        final UUID id = UUID.randomUUID();
        final ClientDto clientDto = new ClientDto();
        final Client client = new Client();
        client.setIdClient(id);
        client.setReservations(new ArrayList<>());

        when(repository.findById(id)).thenReturn(Optional.of(client));

        final ClientDto response = clientService.updateClient(id, clientDto);

        assertNotNull(response);
        assertEquals(client.getIdClient(), response.getId());
    }
}