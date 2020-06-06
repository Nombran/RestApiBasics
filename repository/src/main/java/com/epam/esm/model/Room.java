package com.epam.esm.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Room {
    private long roomNumber;

    private BigDecimal pricePerNight;

    private short floor;

    private boolean hasShower;

    private short maxPersons;
}
