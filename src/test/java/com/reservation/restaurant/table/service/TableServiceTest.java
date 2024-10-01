package com.reservation.restaurant.table.service;

import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.restaurant.repository.RestaurantRepository;
import com.reservation.restaurant.table.domain.dto.TableDto;
import com.reservation.restaurant.table.domain.dto.TableInformationDto;
import com.reservation.restaurant.table.domain.dto.TablesDto;
import com.reservation.restaurant.table.domain.entity.Table;
import com.reservation.restaurant.table.repository.TableRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TableServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private TableRepository repository;

    @Mock
    private ModelMapper mapper;

    @Spy
    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ensure collect of all tables")
    void verifyGetAllTables() {
        final Table table = new Table();
        final UUID id = UUID.randomUUID();

        table.setIdTable(id);
        table.setRestaurant(new Restaurant());

        when(repository.findAll()).thenReturn(List.of(table));

        final List<TableDto> response = tableService.getAllTables();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(id, response.get(0).getId());
    }

    @Test
    @DisplayName("Ensure the correct table is got")
    void verifyGetTableById() {
        final UUID id = UUID.randomUUID();
        final Table table = new Table();

        table.setIdTable(id);

        when(repository.findById(id)).thenReturn(Optional.of(table));

        final Table response = tableService.getById(id);

        assertNotNull(response);
        assertEquals(id, response.getIdTable());
    }

    @Test
    @DisplayName("Validate if an exception is raised when no table is found by id")
    void verifyNotFoundExceptionGetTableById() {
        final UUID id = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> tableService.getById(id));
    }

    @Test
    @DisplayName("Ensure correct table collect")
    void verifyTableById() {
        final UUID id = UUID.randomUUID();
        final Table table = new Table();

        table.setIdTable(id);
        table.setRestaurant(new Restaurant());

        doReturn(table).when(tableService).getById(id);

        final TableDto response = tableService.getTableById(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Ensure correct table insertion")
    void verifyTableInsertion() {
        final UUID id = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant();
        final Table table = new Table(id, null, restaurant);
        final TableInformationDto tableInformation = new TableInformationDto();

        restaurant.setIdRestaurant(UUID.randomUUID());
        tableInformation.setRestaurant(restaurant.getIdRestaurant());

        when(restaurantRepository.findById(restaurant.getIdRestaurant())).thenReturn(Optional.of(restaurant));
        when(repository.save(any())).thenReturn(table);

        final Table response = tableService.insertTable(tableInformation);

        verify(repository).save(any());

        assertEquals(id, response.getIdTable());
    }

    @Test
    @DisplayName("Ensure correct table delete")
    void verifyTableDelete() {
        final UUID id = UUID.randomUUID();
        final Table table = new Table(id, null, null);

        doReturn(table).when(tableService).getById(id);

        tableService.deleteTable(id);

        verify(repository).delete(table);
    }

    @Test
    @DisplayName("Ensure correct table update")
    void verifyTableUpdate() {
        final UUID id = UUID.randomUUID();
        final Table table = new Table();
        final TableInformationDto tableInformationDto = new TableInformationDto();

        table.setIdTable(id);
        table.setRestaurant(new Restaurant());

        doReturn(table).when(tableService).getById(id);

        final TableDto response = tableService.updateTable(id, tableInformationDto);

        verify(mapper).map(tableInformationDto, table);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    @DisplayName("Ensure correct table list insert")
    void verifyTableListInsert() {
        final List<Integer> capacities = List.of(1, 2, 3, 4);
        final Restaurant restaurant = new Restaurant();

        restaurant.setIdRestaurant(UUID.randomUUID());

        final TablesDto tables = new TablesDto(capacities, restaurant.getIdRestaurant());

        when(restaurantRepository.findById(restaurant.getIdRestaurant())).thenReturn(Optional.of(restaurant));

        tableService.insertTables(tables);

        verify(repository, times(4)).save(any());
    }

    @Test
    @DisplayName("Ensure correct collect of available tables")
    void verifyAllAvailableTables() {
        final UUID id1 = UUID.randomUUID();
        final UUID id2 = UUID.randomUUID();
        final Table table1 = new Table(id1, null, new Restaurant());
        final Table table2 = new Table(id2, null, new Restaurant());
        final LocalDate date = LocalDate.now();
        final UUID restaurantId = UUID.randomUUID();

        when(repository.findAllAvailableTableSortByCapacityDesc(date, restaurantId)).thenReturn(List.of(table1, table2));

        final List<TableDto> response = tableService.getAllAvailableTables(date, restaurantId);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        assertEquals(id1, response.get(0).getId());
        assertEquals(id2, response.get(1).getId());
    }

    @Test
    @DisplayName("Validate if an exception is raised when no available table is found")
    void verifyExceptionNoAvailableTableIsFound() {
        assertThrows(NotFoundException.class, () -> tableService.getAllAvailableTables(LocalDate.now(), UUID.randomUUID()));
    }
}