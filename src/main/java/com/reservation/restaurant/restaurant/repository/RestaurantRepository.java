package com.reservation.restaurant.restaurant.repository;

import com.reservation.restaurant.restaurant.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query("""
            select (count(r) > 0)
            from   Restaurant r
            where  r.name = :name
              and  r.branch = :branch
            """)
    boolean existsRestaurantByNameAndBranch(String name, Integer branch);
}
