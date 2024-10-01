package com.reservation.restaurant.reservation.controller;

import com.reservation.restaurant.filter.DateFilter;
import com.reservation.restaurant.reservation.domain.dto.ReservationDetailDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationDto;
import com.reservation.restaurant.reservation.domain.dto.ReservationInformationDto;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationStatusDto;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTableCapacityDto;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTablesDto;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.service.ReservationService;
import com.reservation.restaurant.reservationtable.service.ReservationTableService;
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
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationTableService reservationTableService;

    @Autowired
    public ReservationController(final ReservationService service, final ReservationTableService reservationTableService) {
        this.reservationService = service;
        this.reservationTableService = reservationTableService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ReservationDto> insertReservationById(@RequestBody @Valid ReservationInformationDto reservationToInsert) {
        final Reservation reservation = reservationService.insertReservation(reservationToInsert);
        reservationTableService.addRandomTablesToReservation(reservation, reservation.getCapacity());
        reservationService.sendEmail(reservation);

        URI uri = UriComponentsBuilder.fromPath("/reservation/{id}").buildAndExpand(reservation.getIdReservation()).toUri();

        return ResponseEntity.created(uri).body(new ReservationDto(reservation));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.cancelReservation(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tables")
    @Transactional
    public ResponseEntity<List<ReservationTableCapacityDto>> addTablesReservation(@RequestBody @Valid ReservationTablesDto reservationTables) {
        final Reservation reservation = reservationService.insertReservation(new ReservationInformationDto(reservationTables));
        final List<ReservationTableCapacityDto> reservationTableCapacity = reservationTableService.addTablesToReservation(reservation, reservationTables);
        reservationService.sendEmail(reservation);

        return ResponseEntity.ok(reservationTableCapacity);
    }

    @PutMapping("/status/{id}")
    @Transactional
    public ResponseEntity<ReservationDto> updateStatus(@PathVariable UUID id,
                                                       @RequestBody @Valid ReservationStatusDto reservationStatus) {
        return ResponseEntity.ok(reservationService.updateStatus(id, reservationStatus));
    }

    @GetMapping("/status")
    public ResponseEntity<List<ReservationDto>> getAllReservationsByStatus(@RequestBody ReservationStatusDto status, @ModelAttribute DateFilter dateFilter) {
        return ResponseEntity.ok(reservationService.getAllReservationsByStatus(status, dateFilter));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<ReservationDetailDto>> getAllReservationsByClient(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getAllReservationsByClient(id));
    }
}
