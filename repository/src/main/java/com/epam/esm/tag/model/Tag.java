package com.epam.esm.tag.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Tag {

    private long id;

    @NotBlank
    private String name;
}
