package com.odin.odin.view;

import com.odin.odin.model.Dependencias;
import com.odin.odin.model.Documentos;
import com.odin.odin.model.Radicados;

import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.SubseriesRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.UUID;

@Controller
public class RadicadosView {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SubseriesRepository subseriesRepository;

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DocumentosRepository documentosRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @GetMapping("/view/radicados")
    public String inicio(Model model) {

        model.addAttribute(
                "radicado",
                new Radicados()
        );

        cargarCatalogos(model);

        return "radicados/radicacion_Documental";
    }

    @GetMapping("/view/radicados/documental")
    public String documental(Model model) {

        model.addAttribute(
                "radicado",
                new Radicados()
        );

        cargarCatalogos(model);

        return "radicados/radicacion_Documental";
    }

    private void cargarCatalogos(Model model) {

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );

        model.addAttribute(
                "series",
                seriesRepository.findAll()
        );

        model.addAttribute(
                "subseries",
                subseriesRepository.findAll()
        );

        model.addAttribute(
                "tramites",
                tramitesRepository.findAll()
        );

        model.addAttribute(
                "estados",
                estadosRepository.findAll()
        );

        model.addAttribute(
                "usuarios",
                usuariosRepository.findAll()
        );
    }

    @PostMapping({
            "/view/radicados/save",
            "/view/radicados/documental/save"
    })
    public String save(
            @ModelAttribute Radicados radicado,
            RedirectAttributes ra,
            HttpServletRequest request) {

        boolean isUpdate =
                radicado.getId_radicado() != null
                        && radicado.getId_radicado() > 0;

        prepararRadicado(
                radicado,
                request.getRequestURI()
        );

        Radicados savedRadicado =
                radicadosRepository.save(
                        radicado
                );

        int archivosGuardados =
                guardarArchivos(
                        savedRadicado,
                        radicado.getArchivos()
                );

        String mensaje =
                construirMensajeGuardado(
                        isUpdate,
                        request.getRequestURI(),
                        savedRadicado
                );

        ra.addFlashAttribute(
                "success",
                mensaje
        );

        ra.addFlashAttribute(
                "mensaje",
                mensaje
        );

        ra.addFlashAttribute(
                "savedRadicado",
                savedRadicado
        );

        ra.addFlashAttribute(
                "archivos",
                archivosGuardados
        );

        return "redirect:"
                + obtenerDestinoFormulario(
                request.getRequestURI()
        );
    }

    @GetMapping("/view/radicados/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        Radicados radicado =
                radicadosRepository
                        .findById(id)
                        .orElse(
                                new Radicados()
                        );

        model.addAttribute(
                "radicado",
                radicado
        );

        cargarCatalogos(model);

        return "radicados/radicacion_Documental";
    }

    @PostMapping("/view/radicados/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra) {

        radicadosRepository.deleteById(id);

        ra.addFlashAttribute(
                "success",
                "Radicacion eliminada con exito"
        );

        ra.addFlashAttribute(
                "mensaje",
                "Radicacion eliminada con exito"
        );

        return "redirect:/view/radicados/documental";
    }

    private String obtenerDestinoFormulario(
            String uri) {

        return "/view/radicados/documental";
    }

    private String construirMensajeGuardado(
            boolean isUpdate,
            String uri,
            Radicados radicado) {

        String accion =
                isUpdate
                        ? "actualizada"
                        : "guardada";

        return "Radicacion documental "
                + accion
                + " con exito";
    }

    private void prepararRadicado(
            Radicados radicado,
            String uri) {

        /*
         * Generamos número automáticamente
         * cuando el formulario no lo trae.
         */
        if (!tieneTexto(
                radicado.getNumero_radicado()
        )) {

            String consecutivo =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyyMMddHHmmss"
                                    )
                            );

            radicado.setNumero_radicado(
                    "RAD-" + consecutivo
            );
        }

        /*
         * Fecha de radicación.
         */
        if (!tieneTexto(
                radicado.getFecha_radicado()
        )) {

            String fecha =
                    tieneTexto(
                            radicado.getFechaDocumento()
                    )
                            ? radicado.getFechaDocumento()
                            : LocalDateTime.now()
                            .toLocalDate()
                            .toString();

            radicado.setFecha_radicado(fecha);
        }

        /*
         * Evita mandar "" hacia las FK
         * de serie y subserie.
         */
        if (!tieneTexto(
                radicado.getCodigo_serie()
        )) {

            radicado.setCodigo_serie(null);
        }

        if (!tieneTexto(
                radicado.getCodigo_subserie()
        )) {

            radicado.setCodigo_subserie(null);
        }

        Integer tipoPQRSId =
                convertirAEnteroSeguro(
                        radicado.getTipoPQRS()
                );

        Integer tipoDocId =
                convertirAEnteroSeguro(
                        radicado.getTipoDocumento()
                );

        Integer prioridadId =
                convertirAEnteroSeguro(
                        radicado.getPrioridad()
                );

        Integer canalRecepcionId =
                convertirAEnteroSeguro(
                        radicado.getCanalRecepcion()
                );

        Integer dependenciaId =
                radicado.getId_dependencia() != null
                        ? radicado
                        .getId_dependencia()
                        .intValue()
                        : convertirAEnteroSeguro(
                        radicado.getDependencia()
                );

        Integer depeDestinoId =
                convertirAEnteroSeguro(
                        radicado.getDependenciaDestino()
                );

        Integer depeOrigenId =
                convertirAEnteroSeguro(
                        radicado.getDependenciaOrigen()
                );

        Integer responsableId =
                convertirAEnteroSeguro(
                        radicado.getResponsable()
                );

        /*
         * TRÁMITE
         */
        if (!tieneNumero(
                radicado.getId_tramite()
        )) {

            radicado.setId_tramite(
                    primerId(
                            tipoPQRSId,
                            tipoDocId,
                            prioridadId,
                            1
                    )
            );
        }

        /*
         * ESTADO
         */
        if (!tieneNumero(
                radicado.getId_estado()
        )) {

            radicado.setId_estado(
                    idValorODefecto(
                            canalRecepcionId,
                            1
                    )
            );
        }

        /*
         * DEPENDENCIA
         *
         * Esta es una de las correcciones
         * más importantes.
         */
        Integer idDepElegido =
                primerId(
                        dependenciaId,
                        depeDestinoId,
                        depeOrigenId,
                        null
                );

        Dependencias depBaseDatos =
                idDepElegido == null
                        ? null
                        : dependenciasRepository
                        .findById(
                                idDepElegido
                                        .longValue()
                        )
                        .orElse(null);

        /*
         * Persistimos la relación real.
         */
        radicado.setDependencias(
                depBaseDatos
        );

        /*
         * USUARIO RESPONSABLE
         */
        if (!tieneNumero(
                radicado.getId_usuario()
        )) {

            radicado.setId_usuario(
                    idValorODefecto(
                            responsableId,
                            2
                    )
            );
        }

        /*
         * REMITENTE
         */
        if (!tieneTexto(
                radicado.getRemitente()
        )) {

            radicado.setRemitente(
                    valorODefecto(
                            radicado.getResponsable(),
                            "Anónimo"
                    )
            );
        }

        /*
         * ASUNTO
         */
        if (!tieneTexto(
                radicado.getAsunto()
        )) {

            radicado.setAsunto(
                    valorODefecto(
                            radicado.getObservaciones(),
                            "Sin asunto"
                    )
            );
        }
    }

    private int guardarArchivos(
            Radicados radicado,
            MultipartFile[] archivos) {

        if (archivos == null
                || archivos.length == 0) {

            return 0;
        }

        int guardados = 0;

        try {

            Path dir =
                    Paths.get(uploadDir)
                            .toAbsolutePath();

            Files.createDirectories(dir);

            String fechaSubida =
                    LocalDateTime.now()
                            .toString();

            for (MultipartFile archivo
                    : archivos) {

                if (archivo == null
                        || archivo.isEmpty()) {

                    continue;
                }

                String nombreOriginal =
                        StringUtils.cleanPath(
                                archivo
                                        .getOriginalFilename()
                                        == null
                                        ? "archivo"
                                        : archivo
                                        .getOriginalFilename()
                        );

                String nombreAlmacenado =
                        radicado.getId_radicado()
                                + "_"
                                + UUID.randomUUID()
                                + "_"
                                + nombreOriginal;

                Path destino =
                        dir.resolve(
                                nombreAlmacenado
                        );

                archivo.transferTo(destino);

                Documentos documento =
                        new Documentos();

                documento.setId_radicado(
                        (long)
                                radicado
                                        .getId_radicado()
                );

                documento.setTamano(
                        (int)
                                archivo.getSize()
                );

                documento.setNombre(
                        nombreOriginal
                );

                documento.setNombre_archivo(
                        nombreAlmacenado
                );

                documento.setRuta_archivo(
                        destino.toString()
                );

                documento.setTipo(
                        obtenerTipoArchivo(
                                archivo,
                                nombreOriginal
                        )
                );

                documento.setFecha_subida(
                        fechaSubida
                );

                documentosRepository.save(
                        documento
                );

                guardados++;
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudieron guardar los archivos adjuntos: "
                            + e.getMessage(),
                    e
            );
        }

        return guardados;
    }

    private String obtenerTipoArchivo(
            MultipartFile archivo,
            String nombre) {

        if (tieneTexto(
                archivo.getContentType()
        )) {

            return archivo.getContentType();
        }

        int punto =
                nombre.lastIndexOf('.');

        return punto >= 0
                ? nombre.substring(
                punto + 1
        )
                : "desconocido";
    }

    private String valorODefecto(
            String valor,
            String valorPorDefecto) {

        return tieneTexto(valor)
                ? valor
                : valorPorDefecto;
    }

    private boolean tieneTexto(
            String valor) {

        return valor != null
                && !valor.trim().isEmpty();
    }

    private boolean tieneNumero(
            Integer valor) {

        return valor != null
                && valor > 0;
    }

    private Integer convertirAEnteroSeguro(
            String texto) {

        if (texto == null
                || texto.trim().isEmpty()) {

            return null;
        }

        try {

            return Integer.parseInt(
                    texto.trim()
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    private Integer primerId(
            Integer id1,
            Integer id2,
            Integer id3,
            Integer idPorDefecto) {

        if (id1 != null && id1 > 0) {
            return id1;
        }

        if (id2 != null && id2 > 0) {
            return id2;
        }

        if (id3 != null && id3 > 0) {
            return id3;
        }

        return idPorDefecto;
    }

    private Integer idValorODefecto(
            Integer valor,
            Integer idPorDefecto) {

        return valor != null
                && valor > 0
                ? valor
                : idPorDefecto;
    }
}