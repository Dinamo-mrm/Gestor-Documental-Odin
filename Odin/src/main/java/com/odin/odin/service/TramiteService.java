package com.odin.odin.service;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TramiteService {

    private final TramitesRepository tramitesRepository;
    private final DependenciasRepository dependenciasRepository;
    private final EstadosRepository estadosRepository;

    public TramiteService(
            TramitesRepository tramitesRepository,
            DependenciasRepository dependenciasRepository,
            EstadosRepository estadosRepository) {
        this.tramitesRepository = tramitesRepository;
        this.dependenciasRepository = dependenciasRepository;
        this.estadosRepository = estadosRepository;
    }

    @Transactional
    public Tramites guardar(Tramites tramite) {
        normalizar(tramite);

        if (tramite.getIdTramite() == null
                && tramitesRepository.existsByNombreIgnoreCase(tramite.getNombre())) {
            throw new IllegalArgumentException("Ya existe un trámite con ese nombre");
        }

        if (tramite.getIdTramite() != null
                && tramitesRepository.existsByNombreIgnoreCaseAndIdTramiteNot(
                    tramite.getNombre(), tramite.getIdTramite())) {
            throw new IllegalArgumentException("Ya existe otro trámite con ese nombre");
        }

        if (tramite.getIdDependenciaResponsable() == null
                || !dependenciasRepository.existsById(tramite.getIdDependenciaResponsable())) {
            throw new IllegalArgumentException("Debe seleccionar una dependencia responsable válida");
        }

        if (tramite.getIdEstadoInicial() == null
                || !estadosRepository.existsById(tramite.getIdEstadoInicial())) {
            throw new IllegalArgumentException("Debe seleccionar un estado inicial válido");
        }

        if (tramite.getDiasRespuesta() == null || tramite.getDiasRespuesta() < 0) {
            throw new IllegalArgumentException("Los días de respuesta deben ser mayores o iguales a cero");
        }

        if (tramite.getPrioridadDefault() == null
                || !tramite.getPrioridadDefault().matches("(?i)BAJA|MEDIA|ALTA|URGENTE")) {
            throw new IllegalArgumentException("La prioridad por defecto no es válida");
        }

        if (tramite.getActivo() == null) {
            tramite.setActivo(true);
        }
        if (tramite.getRequiereRespuesta() == null) {
            tramite.setRequiereRespuesta(true);
        }

        if (tramite.getFechaCreacion() == null) {
            tramite.setFechaCreacion(LocalDateTime.now());
        }
        tramite.setFechaActualizacion(LocalDateTime.now());
        tramite.setFechaLimite(
                LocalDateTime.now().plusDays(tramite.getDiasRespuesta())
        );

        return tramitesRepository.save(tramite);
    }

    @Transactional
    public void desactivar(Long id) {
        Tramites tramite = tramitesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El trámite no existe"));
        tramite.setActivo(false);
        tramite.setFechaActualizacion(LocalDateTime.now());
        tramitesRepository.save(tramite);
    }

    private void normalizar(Tramites tramite) {
        if (tramite.getNombre() == null || tramite.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del trámite es obligatorio");
        }
        if (tramite.getDescripcion() == null || tramite.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción del trámite es obligatoria");
        }
        tramite.setNombre(tramite.getNombre().trim());
        tramite.setDescripcion(tramite.getDescripcion().trim());
        if (tramite.getPrioridadDefault() != null) {
            tramite.setPrioridadDefault(tramite.getPrioridadDefault().trim().toUpperCase());
        }
    }
}
