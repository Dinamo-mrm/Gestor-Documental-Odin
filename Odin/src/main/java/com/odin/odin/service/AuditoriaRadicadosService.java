package com.odin.odin.service;

import com.odin.odin.model.AuditoriaRadicados;
import com.odin.odin.repository.AuditoriaRadicadosRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaRadicadosService {
    private final AuditoriaRadicadosRepository repository;
    private final UsuariosRepository usuariosRepository;

    public AuditoriaRadicadosService(AuditoriaRadicadosRepository repository, UsuariosRepository usuariosRepository) {
        this.repository = repository; this.usuariosRepository = usuariosRepository;
    }

    public void registrar(Long radicado, Long usuario, String accion, String campo,
                          String anterior, String nuevo, String comentario) {
        if (radicado == null || usuario == null || !usuariosRepository.existsById(usuario)) return;
        repository.save(AuditoriaRadicados.builder().id_radicado(radicado).id_usuario(usuario)
                .accion(accion).campo_modificado(campo).valor_anterior(anterior).valor_nuevo(nuevo)
                .fecha(LocalDateTime.now()).comentario(comentario).tabla_afectada("radicados")
                .registro_afectado(radicado).build());
    }

    public List<AuditoriaRadicados> porRadicado(Long id) { return repository.findById_radicadoOrderByFechaDesc(id); }
}
