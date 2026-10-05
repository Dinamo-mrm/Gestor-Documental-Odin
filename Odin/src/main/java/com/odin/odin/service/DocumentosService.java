package com.odin.odin.service;

import com.odin.odin.model.Documentos;
import com.odin.odin.model.VersionesDocumento;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.VersionesDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentosService {
    private final DocumentosRepository documentosRepository;
    private final VersionesDocumentoRepository versionesRepository;

    public DocumentosService(DocumentosRepository documentosRepository,
                             VersionesDocumentoRepository versionesRepository) {
        this.documentosRepository = documentosRepository;
        this.versionesRepository = versionesRepository;
    }

    @Transactional
    public Documentos guardar(Documentos documento, Long usuario, String observacion) {
        if (documento.getVersion_actual() == null) documento.setVersion_actual(1);
        Documentos saved = documentosRepository.save(documento);
        VersionesDocumento version = VersionesDocumento.builder()
                .id_documento(saved.getId_documento())
                .version_numero(saved.getVersion_actual())
                .ruta_archivo(saved.getRuta_archivo())
                .observacion(observacion == null ? "Versión inicial" : observacion)
                .fecha_version(LocalDateTime.now())
                .creado_por(usuario)
                .build();
        versionesRepository.save(version);
        return saved;
    }

    @Transactional
    public Documentos crearNuevaVersion(Long idDocumento, String ruta, String observacion, Long usuario) {
        Documentos documento = documentosRepository.findById(idDocumento)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado: " + idDocumento));
        int siguiente = documento.getVersion_actual() == null ? 1 : documento.getVersion_actual() + 1;
        documento.setRuta_archivo(ruta);
        documento.setVersion_actual(siguiente);
        Documentos saved = documentosRepository.save(documento);
        versionesRepository.save(VersionesDocumento.builder()
                .id_documento(idDocumento).version_numero(siguiente).ruta_archivo(ruta)
                .observacion(observacion).fecha_version(LocalDateTime.now()).creado_por(usuario).build());
        return saved;
    }

    public List<VersionesDocumento> versiones(Long idDocumento) {
        return versionesRepository.findById_documentoOrderByVersion_numeroDesc(idDocumento);
    }
}
