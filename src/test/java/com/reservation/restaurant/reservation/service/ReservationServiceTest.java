package com.reservation.restaurant.reservation.service;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.client.service.ClientService;
import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.filter.DateFilter;
import com.reservation.restaurant.reservation.domain.dto.ReservationDetailDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationInformationDto;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationStatusDto;
import com.reservation.restaurant.reservationtable.service.ReservationTableService;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.restaurant.service.RestaurantService;
import com.reservation.restaurant.util.mail.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private List<ValidationException> validationExceptions;

    @Mock
    private ReservationTableService reservationTableService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ClientService clientService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private Mail mail;

    @InjectMocks
    @Spy
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ensure getting all reservations")
    void verifyGetAllReservations() {
        final Reservation reservation1 = new Reservation();
        final Reservation reservation2 = new Reservation();
        final Reservation reservation3 = new Reservation();
        final List<Reservation> reservations = List.of(reservation1, reservation2, reservation3);

        reservation1.setIdReservation(UUID.randomUUID());
        reservation2.setIdReservation(UUID.randomUUID());
        reservation3.setIdReservation(UUID.randomUUID());
        reservation1.setClient(new Client());
        reservation2.setClient(new Client());
        reservation3.setClient(new Client());
        reservation1.setRestaurant(new Restaurant());
        reservation2.setRestaurant(new Restaurant());
        reservation3.setRestaurant(new Restaurant());

        when(reservationRepository.findAll()).thenReturn(reservations);

        final List<ReservationDto> response = reservationService.getAllReservations();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals(reservation1.getIdReservation(), response.get(0).getId());
        assertEquals(reservation2.getIdReservation(), response.get(1).getId());
        assertEquals(reservation3.getIdReservation(), response.get(2).getId());
    }

    @Test
    @DisplayName("Ensure reservation details")
    void verifyReservationDetails() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        final ReservationDto response = reservationService.getReservationById(id);

        assertNotNull(response);
        assertEquals(id, reservation.getIdReservation());
    }

    @Test
    @DisplayName("Validate correct get by id")
    void verifyGetById() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        reservation.setIdReservation(id);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        final Reservation response = reservationService.getById(id);

        assertNotNull(response);
        assertEquals(id, reservation.getIdReservation());
    }

    @Test
    @DisplayName("Validates if a not found exception is raised when no reservation is found")
    void verifyNotFoundExceptionOfReservation() {
        final UUID id = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> reservationService.getById(id));
    }

    @Test
    @DisplayName("Ensure correct reservation insert")
    void verifyReservationInsert() {
        final ReservationInformationDto reservationInformation = new ReservationInformationDto();
        final Reservation reservation = new Reservation();
        final Client client = new Client();
        final Restaurant restaurant = new Restaurant();
        final UUID reservationId = UUID.randomUUID();
        final UUID clientId = UUID.randomUUID();
        final UUID restaurantId = UUID.randomUUID();

        reservationInformation.setClient(clientId);
        reservationInformation.setRestaurant(restaurantId);
        reservation.setIdReservation(reservationId);
        client.setIdClient(clientId);
        restaurant.setIdRestaurant(restaurantId);

        when(clientService.getById(clientId)).thenReturn(client);
        when(restaurantService.getById(restaurantId)).thenReturn(restaurant);
        when(reservationRepository.save(any())).thenReturn(reservation);

        final Reservation response = reservationService.insertReservation(reservationInformation);

        verify(reservationRepository).save(any());

        assertNotNull(response);
        assertEquals(reservationId, response.getIdReservation());
    }

    @Test
    @DisplayName("Ensure reservation cancel")
    void verifyReservationCancel() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        reservation.setDateReservation(LocalDate.now());

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationService).sendEmail(reservation);

        reservationService.cancelReservation(id);

        verify(reservationTableService).deleteTablesReservation(id, reservation.getDateReservation());
        verify(reservationService).sendEmail(reservation);
        verify(reservationRepository).save(reservation);

        assertEquals(Status.CANCELED, reservation.getStatus());
    }

    @Test
    @DisplayName("Ensure correct reservation update")
    void verifyReservationUpdate() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final ReservationDetailDto reservationDetail = new ReservationDetailDto();

        reservation.setIdReservation(id);
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());
        reservationDetail.setId(id);

        doReturn(reservation).when(reservationService).getById(id);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        final ReservationDto response = reservationService.updateReservation(reservationDetail);

        verify(mapper).map(any(), eq(reservation));
        verify(reservationRepository).save(reservation);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Validate confirmation email")
    void verifyConfirmationEmail() {
        final Reservation reservation = new Reservation();
        reservation.setStatus(Status.CONFIRMED);

        reservationService.sendEmail(reservation);

        verify(mail).sendEmailConfirmedReservation(reservation);
    }

    @Test
    @DisplayName("Validate pending email")
    void verifyPendingEmail() {
        final Reservation reservation = new Reservation();
        reservation.setStatus(Status.PENDING);

        reservationService.sendEmail(reservation);

        verify(mail).sendEmailPendingReservation(reservation);
    }

    @Test
    @DisplayName("Validate canceled email")
    void verifyCanceledEmail() {
        final Reservation reservation = new Reservation();
        reservation.setStatus(Status.CANCELED);

        reservationService.sendEmail(reservation);

        verify(mail).sendEmailCanceledReservation(reservation);
    }

    @Test
    @DisplayName("Ensure correct status update")
    void verifyStatusUpdate() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final ReservationStatusDto reservationStatus = new ReservationStatusDto(Status.CANCELED);

        reservation.setIdReservation(id);
        reservation.setStatus(Status.CONFIRMED);
        reservation.setDateReservation(LocalDate.now());
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());

        doReturn(reservation).when(reservationService).getById(id);
        doNothing().when(reservationService).sendEmail(reservation);

        final ReservationDto response = reservationService.updateStatus(id, reservationStatus);

        verify(reservationTableService).deleteTablesReservation(id, reservation.getDateReservation());
        verify(reservationRepository).save(reservation);
        verify(reservationService).sendEmail(reservation);

        assertNotNull(response);
        assertEquals(id, reservation.getIdReservation());
        assertEquals(Status.CANCELED, response.getStatus());
    }

    @Test
    @DisplayName("Validate if a validation exception is raised when trying to update a canceled reservation")
    void verifyValidationExceptionUpdatingCanceledReservation() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final ReservationStatusDto reservationStatus = new ReservationStatusDto(Status.CONFIRMED);

        reservation.setIdReservation(id);
        reservation.setStatus(Status.CANCELED);

        doReturn(reservation).when(reservationService).getById(id);

        assertThrows(ValidationException.class, () -> reservationService.updateStatus(id, reservationStatus));
    }

    @Test
    @DisplayName("Validate if a validation exception is raised when updating status")
    void verifyValidationExceptionUpdatingStatus() {
        final UUID id = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final ReservationStatusDto reservationStatus = new ReservationStatusDto(Status.CONFIRMED);

        reservation.setIdReservation(id);
        reservation.setStatus(Status.CONFIRMED);

        doReturn(reservation).when(reservationService).getById(id);

        assertThrows(ValidationException.class, () -> reservationService.updateStatus(id, reservationStatus));
    }

    @Test
    @DisplayName("Ensure right collect of reservations with status canceled")
    void verifyReservationWithStatusCanceled() {
        final Reservation reservation1 = new Reservation();
        final UUID id1 = UUID.randomUUID();
        final DateFilter dateFilter = new DateFilter(LocalDate.now(), LocalDate.now());
        final Status status = Status.CONFIRMED;
        final List<Reservation> reservations = List.of(reservation1);

        reservation1.setIdReservation(id1);
        reservation1.setClient(new Client());
        reservation1.setRestaurant(new Restaurant());

        when(reservationRepository.getAllReservationsByStatus(status, dateFilter.getBeginDate(), dateFilter.getEndDate())).thenReturn(reservations);

        final List<ReservationDto> response = reservationService.getAllReservationsByStatus(new ReservationStatusDto(status), dateFilter);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(id1, response.get(0).getId());
    }

    @Test
    @DisplayName("Validate if an exception is raised when no reservation is found with such status")
    void verifyNotFoundExceptionReservationStatus() {
        final ReservationStatusDto reservationStatus = new ReservationStatusDto();
        final DateFilter dateFilter = new DateFilter();

        assertThrows(NotFoundException.class, () -> reservationService.getAllReservationsByStatus(reservationStatus, dateFilter));
    }

    @Test
    @DisplayName("Ensure all reservations done by client are collected")
    void verifyReservationsDoneByClient() {
        final Reservation reservation = new Reservation();
        final UUID id = UUID.randomUUID();

        reservation.setIdReservation(id);

        when(reservationRepository.findAllReservationByClient(id)).thenReturn(List.of(reservation));

        final List<ReservationDetailDto> response = reservationService.getAllReservationsByClient(id);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(id, response.get(0).getId());
    }

    @Test
    @DisplayName("Validate if a not found exception is raised when no reservation with such client is found")
    void verifyNotFoundExceptionReservationByClient() {
        final UUID id = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> reservationService.getAllReservationsByClient(id));
    }
}