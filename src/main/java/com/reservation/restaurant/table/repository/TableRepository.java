package com.reservation.restaurant.table.repository;

import com.reservation.restaurant.table.domain.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TableRepository extends JpaRepository<Table, UUID> {

    @Query("""
            select t
            from   Table t
            where  t.idTable not in (
                    select tab.idTable
                    from   Table tab join ReservationTable rt on (tab.idTable = rt.table.idTable)
                    where  rt.reservation.dateReservation = :date
            )
            and t.restaurant.idRestaurant = :idRestaurant
            order by t.capacity desc
            """)
    List<Table> findAllAvailableTableSortByCapacityDesc(LocalDate date, UUID idRestaurant);

    @Query("""
            select t
            from   Table t
            where  t.idTable = :idTable
              and  t.restaurant.idRestaurant = :idRestaurant
            """)
    Optional<Table> findTableByIdAndRestaurantId(UUID idTable, UUID idRestaurant);
}
