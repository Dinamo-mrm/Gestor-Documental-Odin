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

        DashboardResumen r = new DashboardResumen();

        Long totalRadicados = radicadosRepository.count();

        r.setPendientes(totalRadicados);

        r.setEnTramite(0L);
        r.setFinalizados(0L);
        r.setRechazados(0L);
        r.setVencidos(0L);

        r.setUsuariosActivos(
                usuariosRepository.count());

        r.setDocumentosCargados(
                documentosRepository.count());

        r.setAnexosPendientes(0L);

        return r;
    }

    public Long totalRadicados() {
        return radicadosRepository.count();
    }

    public Long radicadosPendientes() {
        return 0L;
    }

    public Long radicadosVencidos() {
        return 0L;
    }

    public Long usuariosActivos() {
        return usuariosRepository.count();
    }
}