package com.odin.odin.service;

import com.odin.odin.model.Expedientes;
import com.odin.odin.repository.ExpedientesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;

@Service
public class ExpedientesService {
    private final ExpedientesRepository repository;
    public ExpedientesService(ExpedientesRepository repository) { this.repository = repository; }

    @Transactional
    public Expedientes crear(String nombre, String descripcion) {
        String base = "EXP-" + Year.now().getValue() + "-";
        long secuencia = repository.count() + 1;
        String codigo = base + String.format("%06d", secuencia);
        while (repository.findByCodigo_expediente(codigo).isPresent()) {
            secuencia++;
            codigo = base + String.format("%06d", secuencia);
        }
        return repository.save(Expedientes.builder().codigo_expediente(codigo).nombre(nombre)
                .descripcion(descripcion).fecha_creacion(LocalDateTime.now()).estado("ABIERTO").build());
    }
}
