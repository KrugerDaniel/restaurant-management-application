package com.reservation.restaurant.client.repository;

import com.reservation.restaurant.client.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("""
            select (count(c) > 0)
            from   Client c
            where  c.cpf = :cpf
            """)
    boolean existsByCpf(String cpf);
}
