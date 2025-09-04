package com.example.pruebaTecnicaSpringBoot.repositories;

import com.example.pruebaTecnicaSpringBoot.models.Vuelo;

import java.util.List;
import java.util.Optional;

public interface VueloRepository {
    List<Vuelo> findAll();
    Optional<Vuelo> findById(int id);
    Vuelo create(Vuelo vuelo);
    Vuelo update(Vuelo vuelo);
    void deleteById(int id);
}
