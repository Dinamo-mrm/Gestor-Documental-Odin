package com.odin.odin.service;

import com.odin.odin.dto.DashboardResumen;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.repository.DocumentosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final RadicadosRepository radicadosRepository;
    private final UsuariosRepository usuariosRepository;
    private final DocumentosRepository documentosRepository;

    public DashboardResumen obtenerResumen() {
        DashboardResumen r = new DashboardResumen();
        r.setTotalRadicados(radicadosRepository.count());
        r.setPendientes(radicadosRepository.count());
        r.setUsuariosActivos(usuariosRepository.count());
        r.setDocumentosCargados(documentosRepository.count());
        r.setEnTramite(0L);
        r.setFinalizados(0L);
        r.setRechazados(0L);
        r.setVencidos(0L);
        r.setAnexosPendientes(0L);
        return r;
    }
}