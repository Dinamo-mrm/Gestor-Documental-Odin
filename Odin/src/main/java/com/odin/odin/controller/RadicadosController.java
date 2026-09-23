package com.odin.odin.controller;

import com.odin.odin.model.HistorialRadicado;
import com.odin.odin.model.Observaciones;
import com.odin.odin.model.Radicados;
import com.odin.odin.model.Reasignaciones;
import com.odin.odin.model.Usuarios;

import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.HistorialRadicadoRepository;
import com.odin.odin.repository.ObservacionesRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.service.RadicacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/radicados")
public class RadicadosController {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @Autowired
    private HistorialRadicadoRepository historialRepository;

    @Autowired
    private ObservacionesRepository observacionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RadicacionService radicacionService;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping
    public List<Radicados> getAll() {
        return radicadosRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/buscar")
    public List<Radicados> buscar(
            @RequestParam(required = false)
            String texto,

            @RequestParam(required = false)
            Integer estado,

            @RequestParam(required = false)
            Long dependencia,

            @RequestParam(required = false)
            Long tramite) {

        return radicadosRepository.buscar(
                texto,
                estado,
                dependencia,
                tramite
        );
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/vencidos")
    public List<Radicados> vencidos() {
        return radicadosRepository.findVencidos();
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/proximos-a-vencer")
    public List<Radicados> proximosAVencer(
            @RequestParam(defaultValue = "3")
            Integer dias) {

        if (dias == null
                || dias < 0
                || dias > 365) {

            dias = 3;
        }

        return radicadosRepository
                .findProximosAVencer(dias);
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/{id}")
    public ResponseEntity<Radicados> getById(
            @PathVariable
            Long id) {

        return radicadosRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @PreAuthorize("hasAuthority('ver_bitacora')")
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialRadicado>>
    historial(
            @PathVariable
            Long id) {

        if (!radicadosRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                historialRepository
                        .findByRadicadoOrderByFechaDesc(id)
        );
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/{id}/reasignaciones")
    public ResponseEntity<List<Reasignaciones>>
    reasignaciones(
            @PathVariable
            Long id) {

        if (!radicadosRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                reasignacionesRepository
                        .findByRadicadoOrderByFechaDesc(
                                id.intValue()
                        )
        );
    }

    @PreAuthorize("hasAuthority('ver_radicados')")
    @GetMapping("/{id}/observaciones")
    public ResponseEntity<List<Observaciones>>
    observaciones(
            @PathVariable
            Long id) {

        if (!radicadosRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                observacionesRepository
                        .findByRadicadoOrderByFechaDesc(id)
        );
    }

    @PreAuthorize("hasAuthority('gestionar_documentos')")
    @PostMapping("/{id}/observaciones")
    public ResponseEntity<?> agregarObservacion(
            @PathVariable
            Long id,

            @RequestBody
            Map<String, Object> payload,

            Authentication authentication) {

        if (!radicadosRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(
                        authentication
                );

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "No se pudo identificar el usuario autenticado"
                            )
                    );
        }

        String comentario =
                texto(
                        payload.get(
                                "comentario"
                        )
                );

        if (comentario.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "La observación no puede estar vacía"
                            )
                    );
        }

        Long idUsuario =
                usuarioActual
                        .get()
                        .getId_usuario();

        Observaciones observacion = new Observaciones();
        observacion.setId_radicado(id);
        observacion.setId_usuario(idUsuario);
        observacion.setComentario(comentario);
        observacion.setFecha(LocalDateTime.now());

        Observaciones saved =
                observacionesRepository
                        .save(observacion);

        registrarHistorial(
                id,
                idUsuario,
                "observacion",
                comentario
        );

        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasAuthority('crear_radicado')")
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody
            Radicados radicado,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(
                        authentication
                );

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        if (radicado.getId_usuario() == null) {

            radicado.setId_usuario(
                    usuarioActual
                            .get()
                            .getId_usuario()
            );
        }

        try {
            RadicacionService.ResultadoRadicacion resultado =
                    radicacionService.guardar(radicado, null);

            Radicados saved = resultado.radicado();

            registrarHistorial(
                    saved.getId_radicado(),
                    usuarioActual.get().getId_usuario(),
                    "radicacion",
                    "Radicado creado"
            );

            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('editar_radicado')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable
            Long id,

            @RequestBody
            Radicados radicado,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(
                        authentication
                );

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        Optional<Radicados> existente =
                radicadosRepository
                        .findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        radicado.setId_radicado(id);

        try {
            RadicacionService.ResultadoRadicacion resultado =
                    radicacionService.guardar(radicado, null);

            Radicados saved = resultado.radicado();

            registrarHistorial(
                    id,
                    usuarioActual.get().getId_usuario(),
                    "modificacion",
                    "Radicado actualizado"
            );

            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('gestionar_tramites')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable
            Long id,

            @RequestBody
            Map<String, Object> payload,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(authentication);

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        Integer nuevoEstado =
                numero(
                        payload.get("estado")
                );

        if (nuevoEstado == null
                || nuevoEstado <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Estado inválido"
                            )
                    );
        }

        Optional<Radicados> resultado =
                radicadosRepository
                        .findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Radicados radicado =
                resultado.get();

        Integer estadoAnterior =
                radicado.getId_estado();

        if (!transicionPermitida(
                estadoAnterior,
                nuevoEstado
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Transición de estado no permitida: "
                                            + estadoAnterior
                                            + " -> "
                                            + nuevoEstado
                            )
                    );
        }

        radicado.setId_estado(
                nuevoEstado
        );

        Radicados saved =
                radicadosRepository
                        .save(radicado);

        registrarHistorial(
                id,
                usuarioActual
                        .get()
                        .getId_usuario(),
                "cambio_estado",
                "Estado "
                        + estadoAnterior
                        + " -> "
                        + nuevoEstado
        );

        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasAuthority('asignar_tramites')")
    @PatchMapping("/{id}/asignar")
    public ResponseEntity<?> asignar(
            @PathVariable
            Long id,

            @RequestBody
            Map<String, Object> payload,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(authentication);

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        Long nuevoUsuario =
                numeroLong(
                        payload.get("usuario")
                );

        if (nuevoUsuario == null
                || nuevoUsuario <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario inválido"
                            )
                    );
        }

        if (!usuariosRepository.existsById(
                nuevoUsuario
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "El usuario responsable no existe"
                            )
                    );
        }

        Optional<Radicados> resultado =
                radicadosRepository
                        .findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Radicados radicado =
                resultado.get();

        Long usuarioAnterior =
                radicado.getId_usuario();

        radicado.setId_usuario(
                nuevoUsuario
        );

        Radicados saved =
                radicadosRepository
                        .save(radicado);

        registrarHistorial(
                id,
                usuarioActual
                        .get()
                        .getId_usuario(),
                "asignacion",
                "Responsable "
                        + usuarioAnterior
                        + " -> "
                        + nuevoUsuario
        );

        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasAuthority('trasladar_radicado')")
    @PostMapping("/{id}/reasignar")
    @Transactional
    public ResponseEntity<?> reasignar(
            @PathVariable
            Long id,

            @RequestBody
            Map<String, Object> payload,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(authentication);

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        Long usuarioNuevo =
                numeroLong(
                        payload.get("usuarioNuevo")
                );

        Long dependenciaNueva =
                numeroLong(
                        payload.get("dependenciaNueva")
                );

        if (usuarioNuevo == null
                || usuarioNuevo <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Debe seleccionar un usuario de destino válido"
                            )
                    );
        }

        if (dependenciaNueva == null
                || dependenciaNueva <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "Debe seleccionar una dependencia válida"
                            )
                    );
        }

        if (!usuariosRepository.existsById(
                usuarioNuevo
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "El usuario de destino no existe"
                            )
                    );
        }

        if (!dependenciasRepository.existsById(
                dependenciaNueva
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "La dependencia de destino no existe"
                            )
                    );
        }

        Optional<Radicados> resultado =
                radicadosRepository
                        .findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Radicados radicado =
                resultado.get();

        Long usuarioAnterior =
                radicado.getId_usuario();

        int actualizados =
                radicadosRepository
                        .actualizarAsignacion(
                                id,
                                usuarioNuevo,
                                dependenciaNueva
                        );

        if (actualizados == 0) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "error",
                                    "No fue posible actualizar la asignación del radicado"
                            )
                    );
        }

        Reasignaciones reasignacion =
                new Reasignaciones();

        reasignacion.setId_radicado(
                id.intValue()
        );

        reasignacion.setId_usuario_anterior(
                usuarioAnterior
        );

        reasignacion.setId_usuario_nuevo(
                usuarioNuevo
        );

        reasignacion.setId_dependencia_nueva(
                dependenciaNueva
        );

        reasignacion.setFecha(
                LocalDateTime.now()
        );

        reasignacionesRepository
                .save(reasignacion);

        registrarHistorial(
                id,
                usuarioActual
                        .get()
                        .getId_usuario(),
                "reasignacion",
                "Responsable "
                        + usuarioAnterior
                        + " -> "
                        + usuarioNuevo
                        + " | Dependencia destino: "
                        + dependenciaNueva
        );

        return ResponseEntity.ok(
                radicadosRepository
                        .findById(id)
                        .orElse(radicado)
        );
    }

    @PreAuthorize("hasAuthority('finalizar_radicado')")
    @PatchMapping("/{id}/cerrar")
    @Transactional
    public ResponseEntity<?> cerrar(
            @PathVariable
            Long id,

            @RequestBody(required = false)
            Map<String, Object> payload,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(authentication);

        if (usuarioActual.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        Optional<Radicados> resultado =
                radicadosRepository
                        .findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        Radicados radicado =
                resultado.get();

        Integer estadoAnterior =
                radicado.getId_estado();

        Integer estadoFinal =
                payload == null
                        ? null
                        : numero(
                        payload.get("estado")
                );

        if (estadoFinal == null) {
            estadoFinal = 3;
        }

        if (!transicionPermitida(
                estadoAnterior,
                estadoFinal
        )) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    "El radicado no puede finalizarse desde el estado actual"
                            )
                    );
        }

        Long idUsuarioActual =
                usuarioActual
                        .get()
                        .getId_usuario();

        radicado.setId_estado(
                estadoFinal
        );

        radicado.setFecha_cierre(
                LocalDateTime.now()
        );

        radicado.setId_usuario_cierre(
                idUsuarioActual
        );

        Radicados saved =
                radicadosRepository
                        .save(radicado);

        String observacion =
                texto(
                        payload == null
                                ? null
                                : payload.get(
                                "observacion"
                        )
                );

        registrarHistorial(
                id,
                idUsuarioActual,
                "cierre",
                observacion.isBlank()
                        ? "Radicado cerrado"
                        : observacion
        );

        if (!observacion.isBlank()) {

            Observaciones nuevaObservacion = new Observaciones();
            nuevaObservacion.setId_radicado(id);
            nuevaObservacion.setId_usuario(idUsuarioActual);
            nuevaObservacion.setComentario(observacion);
            nuevaObservacion.setFecha(LocalDateTime.now());

            observacionesRepository
                    .save(nuevaObservacion);
        }

        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(
            @PathVariable
            Long id,

            Authentication authentication) {

        Optional<Usuarios> usuarioActual =
                obtenerUsuarioActual(authentication);

        if (usuarioActual.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "Usuario autenticado no encontrado"
                            )
                    );
        }

        if (!radicadosRepository.existsById(id)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        registrarHistorial(
                id,
                usuarioActual
                        .get()
                        .getId_usuario(),
                "eliminacion",
                "Radicado eliminado"
        );

        radicadosRepository
                .deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/usuario-actual")
    public ResponseEntity<?> usuarioActual(
            Authentication authentication) {

        Optional<Usuarios> usuario =
                obtenerUsuarioActual(authentication);

        if (usuario.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "error",
                                    "No existe una sesión válida"
                            )
                    );
        }

        Usuarios u =
                usuario.get();

        return ResponseEntity.ok(
                Map.of(
                        "id",
                        u.getId_usuario(),

                        "nombre",
                        u.getNombre(),

                        "correo",
                        u.getCorreo(),

                        "rol",
                        u.getId_rol(),

                        "dependencia",
                        u.getId_dependencia()
                )
        );
    }

    private Optional<Usuarios> obtenerUsuarioActual(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            return Optional.empty();
        }

        return usuariosRepository
                .findByCorreoIgnoreCase(
                        authentication.getName()
                );
    }

    private void registrarHistorial(
            Long idRadicado,
            Long idUsuario,
            String accion,
            String descripcion) {

        if (idRadicado == null
                || idUsuario == null) {

            return;
        }

        if (!usuariosRepository.existsById(
                idUsuario
        )) {

            return;
        }

        HistorialRadicado historial =
                HistorialRadicado
                        .builder()
                        .id_radicado(idRadicado)
                        .id_usuario(idUsuario)
                        .accion(accion)
                        .descripcion(descripcion)
                        .fecha(LocalDateTime.now())
                        .build();

        historialRepository
                .save(historial);
    }

    private boolean transicionPermitida(
            Integer actual,
            Integer nuevo) {

        if (nuevo == null
                || nuevo <= 0) {

            return false;
        }

        if (actual == null) {
            return true;
        }

        if (actual.equals(nuevo)) {
            return false;
        }

        if (actual == 3
                || actual == 4) {

            return false;
        }

        return nuevo >= 1
                && nuevo <= 4;
    }

    private Integer numero(
            Object valor) {

        if (valor == null) {
            return null;
        }

        try {

            if (valor instanceof Number) {
                return ((Number) valor)
                        .intValue();
            }

            String texto =
                    valor
                            .toString()
                            .trim();

            if (texto.isEmpty()) {
                return null;
            }

            return Integer.valueOf(
                    texto
            );

        } catch (Exception e) {

            return null;
        }
    }

    private Long numeroLong(
            Object valor) {

        if (valor == null) {
            return null;
        }

        try {

            if (valor instanceof Number) {
                return ((Number) valor)
                        .longValue();
            }

            String texto =
                    valor
                            .toString()
                            .trim();

            if (texto.isEmpty()) {
                return null;
            }

            return Long.valueOf(
                    texto
            );

        } catch (Exception e) {

            return null;
        }
    }

    private String texto(
            Object valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .toString()
                .trim();
    }
}