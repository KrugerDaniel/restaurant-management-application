package com.reservation.restaurant.table.controller;

import com.reservation.restaurant.reservation.domain.dto.RestaurantIdDto;
import com.reservation.restaurant.table.domain.dto.TableDto;
import com.reservation.restaurant.table.domain.dto.TableInformationDto;
import com.reservation.restaurant.table.domain.dto.TablesDto;
import com.reservation.restaurant.table.domain.entity.Table;
import com.reservation.restaurant.table.service.TableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tables")
public class TablesController {

    private final TableService service;

    @Autowired
    public TablesController(final TableService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TableDto>> getAllTables() {
        return ResponseEntity.ok(service.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDto> getTableById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getTableById(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TableInformationDto> insertTable(@RequestBody @Valid TableInformationDto tableToInsert) {
        final Table table = service.insertTable(tableToInsert);

        final URI uri = UriComponentsBuilder.fromPath("/tables/{id}").buildAndExpand(table.getIdTable()).toUri();

        return ResponseEntity.created(uri).body(new TableInformationDto(table));
    }

    @PostMapping("/list")
    @Transactional
    public ResponseEntity<TablesDto> insertTables(@RequestBody @Valid TablesDto tablesDto) {
        service.insertTables(tablesDto);

        return ResponseEntity.ok(tablesDto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTable(@PathVariable UUID id) {
        service.deleteTable(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TableDto> updateTable(@PathVariable UUID id, @RequestBody @Valid TableInformationDto tables) {
        return ResponseEntity.ok(service.updateTable(id, tables));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TableDto>> getAllAvailableTables(@RequestParam LocalDate dateReservation, @RequestBody @Valid RestaurantIdDto restaurant) {
        return ResponseEntity.ok(service.getAllAvailableTables(dateReservation, restaurant.getRestaurant()));
    }
}
