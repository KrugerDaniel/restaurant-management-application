package com.reservation.restaurant.reservation.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.filter.DateFilter;
import com.reservation.restaurant.reservation.domain.dto.ReservationDetailDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationInformationDto;
import com.reservation.restaurant.reservation.service.validations.ReservationValidation;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationStatusDto;
import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.domain.entity.enums.Status;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.client.service.ClientService;
import com.reservation.restaurant.reservationtable.service.ReservationTableService;
import com.reservation.restaurant.restaurant.service.RestaurantService;
import com.reservation.restaurant.util.mail.Mail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final List<ReservationValidation> reservationValidations;
    private final ReservationTableService reservationTableService;
    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final ClientService clientService;
    private final ModelMapper mapper;
    private final Mail mail;

    @Autowired
    public ReservationService(final List<ReservationValidation> reservationValidations,
                              final ReservationTableService reservationTableService,
                              final ReservationRepository repository, final RestaurantService restaurantService,
                              final ClientService clientService, final ModelMapper mapper, final Mail mail) {
        this.reservationValidations = reservationValidations;
        this.reservationTableService = reservationTableService;
        this.reservationRepository = repository;
        this.restaurantService = restaurantService;
        this.clientService = clientService;
        this.mapper = mapper;
        this.mail = mail;
    }

    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll().stream().map(ReservationDto::new).toList();
    }

    public ReservationDto getReservationById(final UUID id) {
        return new ReservationDto(getById(id));
    }

    public Reservation getById(final UUID id) {
        return reservationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Reservation insertReservation(final ReservationInformationDto reservationToInsert) {
        final Client client = clientService.getById(reservationToInsert.getClient());
        final Restaurant restaurant = restaurantService.getById(reservationToInsert.getRestaurant());
        final Reservation reservation = new Reservation(
                null,
                reservationToInsert.getDateReservation(),
                reservationToInsert.getTimeReservation(),
                reservationToInsert.getStatus(),
                reservationToInsert.getCapacity(),
                client,
                restaurant
        );
        reservationValidations.forEach(v -> v.validate(reservation));

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(final UUID id) {
        final Reservation reservation = getById(id);
        reservationTableService.deleteTablesReservation(id, reservation.getDateReservation());
        reservation.setStatus(Status.CANCELED);
        sendEmail(reservation);
        reservationRepository.save(reservation);
    }

    public ReservationDto updateReservation(final ReservationDetailDto reservationDetail) {
        final Reservation reservation = getById(reservationDetail.getId());

        final ReservationDto reservationDto = new ReservationDto(reservationDetail);
        mapper.map(reservationDto, reservation);

        return new ReservationDto(reservationRepository.save(reservation));
    }

    public void sendEmail(final Reservation reservation) {
        switch (reservation.getStatus()) {
            case CONFIRMED:
                mail.sendEmailConfirmedReservation(reservation);
                break;
            case PENDING:
                mail.sendEmailPendingReservation(reservation);
                break;
            case CANCELED:
                mail.sendEmailCanceledReservation(reservation);
                break;
            default:
                throw new ValidationException("No such status");
        }
    }

    public ReservationDto updateStatus(final UUID id, final ReservationStatusDto reservationStatus) {
        final Reservation reservation = getById(id);
        if (reservationStatus.getStatus().equals(reservation.getStatus())) {
            throw new ValidationException("Status invalid");
        }
        if (reservation.getStatus().equals(Status.CANCELED)) {
            throw new ValidationException("Status canceled can not be undone");
        }
        if (reservationStatus.getStatus().equals(Status.CANCELED)) {
            reservationTableService.deleteTablesReservation(reservation.getIdReservation(), reservation.getDateReservation());
        }

        reservation.setStatus(reservationStatus.getStatus());
        reservationRepository.save(reservation);
        sendEmail(reservation);
        return new ReservationDto(reservation);
    }

    public List<ReservationDto> getAllReservationsByStatus(final ReservationStatusDto status, final DateFilter dateFilter) {
        final List<ReservationDto> reservations = reservationRepository.getAllReservationsByStatus(status.getStatus(),
                dateFilter.getBeginDate(), dateFilter.getEndDate()).stream().map(ReservationDto::new).toList();
        isEmpty(reservations);
        return reservations;
    }

    public List<ReservationDetailDto> getAllReservationsByClient(final UUID clientIdentification) {
        final List<ReservationDetailDto> reservations = reservationRepository.findAllReservationByClient(clientIdentification)
                .stream().map(ReservationDetailDto::new).toList();
        isEmpty(reservations);
        return reservations;
    }

    private <T> void isEmpty(List<T> reservations) {
        if (reservations.isEmpty()) {
            throw new NotFoundException();
        }
    }
}
