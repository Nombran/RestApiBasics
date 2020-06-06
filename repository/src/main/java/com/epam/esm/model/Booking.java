package com.epam.esm.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Booking {
    private long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    
    private short roomNumber;

    private long guestId;
}
