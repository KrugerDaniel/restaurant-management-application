package com.reservation.restaurant.table.domain.entity;

import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import com.reservation.restaurant.table.domain.dto.TableInformationDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@jakarta.persistence.Table(name = "tables")
@Entity(name = "Table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idTable")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTable;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "idRestaurant")
    private Restaurant restaurant;

    public Table(final TableInformationDto tables, final Restaurant restaurant) {
        setCapacity(tables.getCapacity());
        setRestaurant(restaurant);
    }
}
