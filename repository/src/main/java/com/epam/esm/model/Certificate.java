package com.epam.esm.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Certificate {

    private long id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDateTime creationDate;

    private LocalDateTime modificationDate;

    private int duration;
}
