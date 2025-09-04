package com.example.pruebaTecnicaSpringBoot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class VueloActualizacionDTO {
    @NotBlank
    private String nombreVuelo;
    @NotBlank
    private String empresa;
    @NotBlank
    private String lugarSalida;
    @NotBlank
    private String lugarLlegada;
    @NotNull
    private LocalDate fechaSalida;
    @NotNull
    private LocalDate fechaLlegada;
}
