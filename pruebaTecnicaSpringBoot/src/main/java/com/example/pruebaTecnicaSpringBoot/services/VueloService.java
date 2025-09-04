package com.example.pruebaTecnicaSpringBoot.services;

import com.example.pruebaTecnicaSpringBoot.dtos.VueloActualizacionDTO;
import com.example.pruebaTecnicaSpringBoot.dtos.VueloDTO;
import com.example.pruebaTecnicaSpringBoot.models.Vuelo;
import com.example.pruebaTecnicaSpringBoot.repositories.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VueloService {
    private final VueloRepository vueloRepository;

    public List<VueloDTO> listar(String empresa, String lugarLlegada, LocalDate fechaSalida, String ordenarPor){
        final String ord = (ordenarPor == null || ordenarPor.isBlank()) ? "fechaSalida" : ordenarPor;

        Comparator<Vuelo> comparator = switch (ord) {
            case "empresa" -> Comparator
                    .comparing((Vuelo v) -> nvl(v.getEmpresa()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Vuelo::getFechaSalida, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Vuelo::getId);
            case "lugarLlegada" -> Comparator
                    .comparing((Vuelo v) -> nvl(v.getLugarLlegada()), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Vuelo::getFechaSalida, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Vuelo::getId);
            case "fechaSalida" -> Comparator
                    .comparing(Vuelo::getFechaSalida, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Vuelo::getId);
            default -> throw new IllegalArgumentException("Parámetro ordenarPor no válido: " + ordenarPor);
        };

        return vueloRepository.findAll().stream()
                .filter(v -> isBlank(empresa) || equalsIgnoreCase(v.getEmpresa(), empresa))
                .filter(v -> isBlank(lugarLlegada) || equalsIgnoreCase(v.getLugarLlegada(), lugarLlegada))
                .filter(v -> fechaSalida == null || Objects.equals(v.getFechaSalida(), fechaSalida))
                .sorted(comparator)
                .map(this::toDto)
                .toList();
    }
    //Obtiene un vuelo por su ID
    public VueloDTO obtenerPorId(int id) {
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el vuelo con id=" + id));
        return toDto(vuelo);
    }
    //Crea un vuelo a partir del DTO de entrada
    public VueloDTO crear(VueloActualizacionDTO dto) {
        validarNegocio(dto);
        Vuelo entidad = Vuelo.builder()
                .id(0) // la implementación del repository asignará el id definitivo
                .nombreVuelo(dto.getNombreVuelo())
                .empresa(dto.getEmpresa())
                .lugarSalida(dto.getLugarSalida())
                .lugarLlegada(dto.getLugarLlegada())
                .fechaSalida(dto.getFechaSalida())
                .fechaLlegada(dto.getFechaLlegada())
                .build();

        Vuelo guardado = vueloRepository.create(entidad);
        return toDto(guardado);
    }
    //Actualiza un vuelo existente con el id indicado
    public VueloDTO actualizar(int id, VueloActualizacionDTO dto) {

        vueloRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el vuelo con id=" + id));

        validarNegocio(dto);

        Vuelo actualizado = Vuelo.builder()
                .id(id)
                .nombreVuelo(dto.getNombreVuelo())
                .empresa(dto.getEmpresa())
                .lugarSalida(dto.getLugarSalida())
                .lugarLlegada(dto.getLugarLlegada())
                .fechaSalida(dto.getFechaSalida())
                .fechaLlegada(dto.getFechaLlegada())
                .build();

        Vuelo resultado = vueloRepository.update(actualizado);
        return toDto(resultado);
    }
    //Elimina un vuelo por id
    public void eliminar(int id) {

        vueloRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el vuelo con id=" + id));
        vueloRepository.deleteById(id);
    }

    private VueloDTO toDto(Vuelo v) {
        long duracion = 0;
        if (v.getFechaSalida() != null && v.getFechaLlegada() != null) {
            duracion = ChronoUnit.DAYS.between(v.getFechaSalida(), v.getFechaLlegada());
        }

        return VueloDTO.builder()
                .id(v.getId())
                .nombreVuelo(v.getNombreVuelo())
                .empresa(v.getEmpresa())
                .lugarSalida(v.getLugarSalida())
                .lugarLlegada(v.getLugarLlegada())
                .fechaSalida(v.getFechaSalida())
                .fechaLlegada(v.getFechaLlegada())
                .duracionDias(duracion)
                .build();
    }
    // Reglas de negocio(Fechas con sentido)
    private void validarNegocio(VueloActualizacionDTO dto) {
        if (dto.getFechaSalida() != null && dto.getFechaLlegada() != null
                && dto.getFechaSalida().isAfter(dto.getFechaLlegada())) {
            throw new IllegalArgumentException("La fecha de salida no puede ser posterior a la fecha de llegada.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static boolean equalsIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
