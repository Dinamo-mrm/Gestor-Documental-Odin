package com.odin.odin.service;

import com.odin.odin.dto.DashboardResumen;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RadicadosRepository radicadosRepository;
    private final UsuariosRepository usuariosRepository;
    private final DocumentosRepository documentosRepository;

    public DashboardResumen obtenerResumen() {
        DashboardResumen resumen = new DashboardResumen();

        resumen.setTotalRadicados(valor(radicadosRepository.count()));
        resumen.setPendientes(valor(radicadosRepository.countPendientes()));
        resumen.setEnTramite(valor(radicadosRepository.countEnTramite()));
        resumen.setFinalizados(valor(radicadosRepository.countFinalizados()));
        resumen.setRechazados(valor(radicadosRepository.countRechazados()));
        resumen.setVencidos(valor(radicadosRepository.countVencidos()));
        resumen.setUsuariosActivos(valor(usuariosRepository.count()));
        resumen.setDocumentosCargados(valor(documentosRepository.count()));

        // La tabla de anexos aún no está definida en el modelo actual.
        // Se mantiene en cero hasta contar con su repositorio real.
        resumen.setAnexosPendientes(0L);

        return resumen;
    }

    private Long valor(Long valor) {
        return valor == null ? 0L : valor;
    }
}
