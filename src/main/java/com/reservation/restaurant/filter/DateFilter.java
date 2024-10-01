package com.reservation.restaurant.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateFilter {

    private LocalDate beginDate;
    private LocalDate endDate;
}
