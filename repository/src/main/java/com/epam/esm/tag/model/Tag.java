package com.epam.esm.tag.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Tag {

    private long id;

    @NonNull
    @NotBlank
    private String name;
}
