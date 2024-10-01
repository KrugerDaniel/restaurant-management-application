package com.reservation.restaurant.reservationtable.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTableCapacityDto;
import com.reservation.restaurant.reservation.domain.entity.Reservation;
import com.reservation.restaurant.reservationtable.domain.dto.ReservationTablesDto;
import com.reservation.restaurant.reservationtable.domain.entity.ReservationTable;
import com.reservation.restaurant.table.domain.entity.Table;
import com.reservation.restaurant.reservation.repository.ReservationRepository;
import com.reservation.restaurant.reservationtable.repository.ReservationTableRepository;
import com.reservation.restaurant.table.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationTableService {

    private final ReservationTableRepository reservationTableRepository;
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTableService(final ReservationTableRepository reservationTableRepository, final TableRepository tableRepository,
                                   final ReservationRepository reservationRepository) {
        this.reservationTableRepository = reservationTableRepository;
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTableCapacityDto> addTablesToReservation(final Reservation reservation, final ReservationTablesDto tablesToAdd) {
        final List<ReservationTableCapacityDto> tablesReservations = new ArrayList<>();
        tablesToAdd.getTables().forEach(tableToAdd -> {
            final Table table = tableRepository.findTableByIdAndRestaurantId(tableToAdd, tablesToAdd.getRestaurant()).orElseThrow(NotFoundException::new);
            if (reservationTableRepository.existsReservationTable(reservation.getDateReservation(), tableToAdd)) {
                throw new ValidationException("Table reserved");
            }
            final ReservationTable reservationTable = new ReservationTable(null, reservation, table);
            reservationTableRepository.save(reservationTable);

            final ReservationTableCapacityDto reservationTableCapacityDto = new ReservationTableCapacityDto(tableToAdd, table.getCapacity());
            tablesReservations.add(reservationTableCapacityDto);
        });
        return tablesReservations;
    }

    public void addRandomTablesToReservation(final Reservation reservation, final Integer capacity) {
        final List<Table> tablesAvailable = new ArrayList<>(tableRepository.findAllAvailableTableSortByCapacityDesc(reservation.getDateReservation(), reservation.getRestaurant().getIdRestaurant()));
        final List<Table> tablesReserved = getTablesReserved(tablesAvailable, capacity);
        tablesReserved.forEach(tablesToInsert -> {
            final ReservationTable reservationTable = new ReservationTable(null, reservation, tablesToInsert);

            reservationTableRepository.save(reservationTable);
        });
    }

    private List<Table> getTablesReserved(final List<Table> tablesAvailable, int missingPeople) {
        final List<Table> tablesReserved = new ArrayList<>();
        Table table;

        for (int i = 0; i < tablesAvailable.size() && missingPeople > 0; i++) {
            table = tablesAvailable.get(i);
            if (table.getCapacity() <= missingPeople) {
                tablesReserved.add(table);
                missingPeople -= table.getCapacity();
            }
        }
        tablesAvailable.removeAll(tablesReserved);

        for (int i = tablesAvailable.size() - 1; i >= 0 && missingPeople > 0; i--) {
            table = tablesAvailable.get(i);
            tablesReserved.add(table);
            missingPeople -= table.getCapacity();
        }

        if (missingPeople > 0) {
            throw new ValidationException("Not enough tables to reserve");
        }
        return tablesReserved;
    }

    public void deleteTablesReservation(final UUID reservationIdentification, final LocalDate dateReservation) {
        final List<ReservationTable> reservationTables = reservationRepository.findAllTablesReservedByReservation(reservationIdentification, dateReservation);
        if (reservationTables.isEmpty()) {
            throw new NotFoundException();
        }
        reservationTables.forEach(reservationTableRepository::delete);
    }
}
