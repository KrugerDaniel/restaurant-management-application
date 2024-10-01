package com.reservation.restaurant.table.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.restaurant.repository.RestaurantRepository;
import com.reservation.restaurant.table.domain.dto.TableDto;
import com.reservation.restaurant.table.domain.dto.TableInformationDto;
import com.reservation.restaurant.table.domain.dto.TablesDto;
import com.reservation.restaurant.table.domain.entity.Table;
import com.reservation.restaurant.table.repository.TableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TableService {

    private final RestaurantRepository restaurantRepository;
    private final TableRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public TableService(final RestaurantRepository restaurantRepository, final TableRepository repository, final ModelMapper mapper) {
        this.restaurantRepository = restaurantRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TableDto> getAllTables() {
        return repository.findAll().stream().map(TableDto::new).toList();
    }

    public Table getById(final UUID id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public TableDto getTableById(final UUID id) {
        return new TableDto(getById(id));
    }

    public Table insertTable(final TableInformationDto tableToInsert) {
        final Restaurant restaurant = restaurantRepository.findById(tableToInsert.getRestaurant()).orElseThrow(NotFoundException::new);
        final Table table = new Table(tableToInsert, restaurant);

        return repository.save(table);
    }

    public void deleteTable(final UUID id) {
        final Table table = getById(id);

        repository.delete(table);
    }

    public TableDto updateTable(final UUID id, final TableInformationDto tablesToUpdate) {
        final Table table = getById(id);
        mapper.map(tablesToUpdate, table);

        return new TableDto(table);
    }

    public void insertTables(final TablesDto tablesList) {
        final Restaurant restaurant = restaurantRepository.findById(tablesList.getRestaurant()).orElseThrow(NotFoundException::new);
        tablesList.getTables().forEach(table -> {
            final Table tables = new Table(null, table, restaurant);

            repository.save(tables);
        });
    }

    public List<TableDto> getAllAvailableTables(final LocalDate dateReservation, final UUID restaurant) {
        final List<TableDto> tables = repository.findAllAvailableTableSortByCapacityDesc(dateReservation, restaurant)
                .stream().map(TableDto::new).toList();
        if (tables.isEmpty()) {
            throw new NotFoundException();
        }
        return tables;
    }
}
