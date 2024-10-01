package com.reservation.restaurant.reservationtable.service;

import com.reservation.restaurant.client.domain.entity.Client;
import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTableCapacityDto;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTablesDto;
import com.reservation.restaurant.reservationtable.domain.entity.ReservationTable;
import com.reservation.restaurant.reservationtable.repository.ReservationTableRepository;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.table.domain.entity.Table;
import com.reservation.restaurant.table.repository.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationTableServiceTest {

    @Mock
    private ReservationTableRepository reservationTableRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTableService reservationTableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ensure that the correct tables are added to reservation")
    void verifyAddTableToReservation() {
        final Reservation reservation = new Reservation();
        final UUID tableId = UUID.randomUUID();
        final Table table = new Table(tableId, null, null);
        final ReservationTablesDto reservationTables = new ReservationTablesDto();

        reservation.setIdReservation(UUID.randomUUID());
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());
        reservationTables.setRestaurant(UUID.randomUUID());
        reservationTables.setTables(List.of(tableId));

        when(tableRepository.findTableByIdAndRestaurantId(tableId, reservationTables.getRestaurant())).thenReturn(Optional.of(table));

        final List<ReservationTableCapacityDto> response = reservationTableService.addTablesToReservation(reservation, reservationTables);

        verify(reservationTableRepository).save(any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(table.getIdTable(), response.get(0).getTable());
    }

    @Test
    @DisplayName("Validate if an exception is raised when the table is already reserved")
    void verifyValidationExceptionForTableReserved() {
        final UUID tableId = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final Table table = new Table(tableId, null, null);
        final LocalDate date = LocalDate.now();
        final ReservationTablesDto reservationTables = new ReservationTablesDto();

        reservation.setIdReservation(UUID.randomUUID());
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());
        reservation.setDateReservation(date);
        reservationTables.setRestaurant(UUID.randomUUID());
        reservationTables.setTables(List.of(tableId));

        when(tableRepository.findTableByIdAndRestaurantId(tableId, reservationTables.getRestaurant())).thenReturn(Optional.of(table));
        when(reservationTableRepository.existsReservationTable(date, tableId)).thenReturn(true);

        assertThrows(ValidationException.class, () -> reservationTableService.addTablesToReservation(reservation, reservationTables));
    }

    @Test
    @DisplayName("Validate if an exception is raised when the table does not exists")
    void verifyNotFoundExceptionTableWhenReserving() {
        final UUID tableId = UUID.randomUUID();
        final Reservation reservation = new Reservation();
        final LocalDate date = LocalDate.now();
        final ReservationTablesDto reservationTables = new ReservationTablesDto();

        reservation.setIdReservation(UUID.randomUUID());
        reservation.setClient(new Client());
        reservation.setRestaurant(new Restaurant());
        reservation.setDateReservation(date);
        reservationTables.setRestaurant(UUID.randomUUID());
        reservationTables.setTables(List.of(tableId));

        when(reservationRepository.findById(reservation.getIdReservation())).thenReturn(Optional.of(reservation));

        assertThrows(NotFoundException.class, () -> reservationTableService.addTablesToReservation(reservation, reservationTables));
    }

    @Test
    @DisplayName("Ensure correct reservation of tables")
    void verifyReservationOfTables() {
        final Table table1 = new Table(UUID.randomUUID(), 4, null);
        final Table table2 = new Table(UUID.randomUUID(), 4, null);
        final List<Table> tables = List.of(table1, table2);
        final LocalDate date = LocalDate.now();
        final Reservation reservation = new Reservation();
        final Restaurant restaurant = new Restaurant();
        reservation.setDateReservation(date);
        reservation.setRestaurant(restaurant);
        restaurant.setIdRestaurant(UUID.randomUUID());

        when(tableRepository.findAllAvailableTableSortByCapacityDesc(date, restaurant.getIdRestaurant())).thenReturn(tables);

        reservationTableService.addRandomTablesToReservation(reservation, 7);

        verify(reservationTableRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("Validate if an exception is raised due to not having enough tables")
    void verifyValidationExceptionNotEnoughTables() {
        final Table table1 = new Table(UUID.randomUUID(), 4, null);
        final Table table2 = new Table(UUID.randomUUID(), 4, null);
        final List<Table> tables = List.of(table1, table2);
        final LocalDate date = LocalDate.now();
        final Reservation reservation = new Reservation();
        final Restaurant restaurant = new Restaurant();
        reservation.setDateReservation(date);
        reservation.setRestaurant(restaurant);
        restaurant.setIdRestaurant(UUID.randomUUID());

        when(tableRepository.findAllAvailableTableSortByCapacityDesc(date, restaurant.getIdRestaurant())).thenReturn(tables);

        assertThrows(ValidationException.class, () -> reservationTableService.addRandomTablesToReservation(reservation, 9));
    }

    @Test
    @DisplayName("Validate if a exception is raised when no reservation is found during delete")
    void verifyValidationExceptionWhenNoReservationIsFound() {
        final UUID id = UUID.randomUUID();
        final LocalDate date = LocalDate.now();

        assertThrows(NotFoundException.class, () -> reservationTableService.deleteTablesReservation(id, date));

        verify(reservationTableRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Ensure correct delete os table reservation by reservation identification")
    void verifyDeleteTableReservationByReservation() {
        final UUID id = UUID.randomUUID();
        final LocalDate date = LocalDate.now();
        final ReservationTable reservationTable = new ReservationTable();

        when(reservationRepository.findAllTablesReservedByReservation(id, date)).thenReturn(List.of(reservationTable));

        reservationTableService.deleteTablesReservation(id, date);

        verify(reservationTableRepository).delete(any());
    }
}