package com.epam.esm.certificate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class CertificateDto {
    private long id;
    @NonNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;
    @NonNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String description;
    @NonNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "20000.0")
    @NotNull
    private BigDecimal price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modificationDate;
    @NonNull
    @Min(1)
    @Max(100)
    private int duration;
    @NonNull
    @NotNull
    private List<String> tags;
}
