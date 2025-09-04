package com.example.pruebaTecnicaSpringBoot.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.example.pruebaTecnicaSpringBoot.models.Vuelo;

import jakarta.annotation.PostConstruct;

// Implementación en memoria del repositorio de vuelos
@Repository
public class VueloRepositoryImplementacion implements VueloRepository {

    private final ConcurrentHashMap<Integer, Vuelo> tabla = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger(0);

    // Carga de datos
    @PostConstruct
    void cargarDatosIniciales() {

        create(Vuelo.builder()
                .nombreVuelo("H001-V")
                .empresa("Iberia")
                .lugarSalida("Madrid")
                .lugarLlegada("Buenos Aires")
                .fechaSalida(LocalDate.of(2025, 3, 10))
                .fechaLlegada(LocalDate.of(2025, 3, 11))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H002-V")
                .empresa("Emirates")
                .lugarSalida("Santiago de Compostela")
                .lugarLlegada("Dubai")
                .fechaSalida(LocalDate.of(2025, 11, 12))
                .fechaLlegada(LocalDate.of(2025, 11, 12))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H003-V")
                .empresa("Air Canada")
                .lugarSalida("Barcelona")
                .lugarLlegada("Vancouver")
                .fechaSalida(LocalDate.of(2025, 12, 4))
                .fechaLlegada(LocalDate.of(2025, 12, 4))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H004-V")
                .empresa("Vueling")
                .lugarSalida("Madrid")
                .lugarLlegada("Helsinki")
                .fechaSalida(LocalDate.of(2026, 1, 23))
                .fechaLlegada(LocalDate.of(2026, 1, 23))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H005-V")
                .empresa("AirEuropa")
                .lugarSalida("Porto")
                .lugarLlegada("París")
                .fechaSalida(LocalDate.of(2026, 2, 8))
                .fechaLlegada(LocalDate.of(2026, 2, 8))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H006-V")
                .empresa("Iberia")
                .lugarSalida("Londres")
                .lugarLlegada("Buenos Aires")
                .fechaSalida(LocalDate.of(2026, 3, 19))
                .fechaLlegada(LocalDate.of(2026, 3, 19))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H007-V")
                .empresa("Iberia")
                .lugarSalida("Amsterdam")
                .lugarLlegada("Sidney")
                .fechaSalida(LocalDate.of(2026, 4, 2))
                .fechaLlegada(LocalDate.of(2026, 4, 2))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H008-V")
                .empresa("Vueling")
                .lugarSalida("Madrid")
                .lugarLlegada("Bogotá")
                .fechaSalida(LocalDate.of(2026, 5, 15))
                .fechaLlegada(LocalDate.of(2026, 5, 15))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H009-V")
                .empresa("Turkish")
                .lugarSalida("Roma")
                .lugarLlegada("Japón")
                .fechaSalida(LocalDate.of(2026, 6, 27))
                .fechaLlegada(LocalDate.of(2026, 6, 27))
                .build());

        create(Vuelo.builder()
                .nombreVuelo("H010-V")
                .empresa("Emirates")
                .lugarSalida("Barcelona")
                .lugarLlegada("New York")
                .fechaSalida(LocalDate.of(2026, 7, 10))
                .fechaLlegada(LocalDate.of(2026, 7, 10))
                .build());
    }

    @Override
    public List<Vuelo> findAll() {
        return new ArrayList<>(tabla.values());
    }

    @Override
    public Optional<Vuelo> findById(int id) {
        return Optional.ofNullable(tabla.get(id));
    }

    @Override
    public Vuelo create(Vuelo vuelo) {
        int nuevoId = secuencia.incrementAndGet();
        vuelo.setId(nuevoId);
        tabla.put(nuevoId, vuelo);
        return vuelo;
    }

    @Override
    public Vuelo update(Vuelo vuelo) {
        int id = vuelo.getId();
        if (!tabla.containsKey(id)) {
            throw new NoSuchElementException("No existe el vuelo con id: " + id);
        }
        tabla.put(id, vuelo);
        return vuelo;
    }

    @Override
    public void deleteById(int id) {
        tabla.remove(id);
    }
}
