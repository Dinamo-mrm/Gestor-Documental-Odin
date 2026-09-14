package com.odin.odin.controller;

import com.odin.odin.model.*;
import com.odin.odin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radicados")
public class RadicadosController {
    @Autowired private RadicadosRepository radicadosRepository;
    @Autowired private ReasignacionesRepository reasignacionesRepository;
    @Autowired private HistorialRadicadoRepository historialRepository;
    @Autowired private ObservacionesRepository observacionesRepository;
    @Autowired private RolesRepository rolesRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping public List<Radicados> getAll() { return radicadosRepository.findAll(); }

    @GetMapping("/buscar")
    public List<Radicados> buscar(@RequestParam(required=false) String texto,
                                  @RequestParam(required=false) Integer estado,
                                  @RequestParam(required=false) Long dependencia,
                                  @RequestParam(required=false) Integer tramite) {
        return radicadosRepository.buscar(texto, estado, dependencia, tramite);
    }

    @GetMapping("/vencidos")
    public List<Radicados> vencidos() { return radicadosRepository.findVencidos(); }

    @GetMapping("/proximos-a-vencer")
    public List<Radicados> proximosAVencer(@RequestParam(defaultValue="3") Integer dias) {
        if (dias == null || dias < 0 || dias > 365) dias = 3;
        return radicadosRepository.findProximosAVencer(dias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Radicados> getById(@PathVariable Long id) {
        return radicadosRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialRadicado>> historial(@PathVariable Long id) {
        if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(historialRepository.findById_radicadoOrderByFechaDesc(id));
    }

    @GetMapping("/{id}/reasignaciones")
    public ResponseEntity<List<Reasignaciones>> reasignaciones(@PathVariable Long id) {
        if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reasignacionesRepository.findById_radicadoOrderByFechaDesc(id.intValue()));
    }

    @GetMapping("/{id}/observaciones")
    public ResponseEntity<List<Observaciones>> observaciones(@PathVariable Long id) {
        if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(observacionesRepository.findById_radicadoOrderByFechaDesc(id));
    }

    @PostMapping("/{id}/observaciones")
    public ResponseEntity<?> agregarObservacion(@PathVariable Long id, @RequestBody Map<String,Object> payload,
                                                 @RequestHeader(value="X-User-Id", required=false) Long actor) {
        if (!puedeModificar(actor)) return prohibido("agregar observaciones");
        if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        String comentario = texto(payload.get("comentario"));
        Long usuario = actor != null ? actor : numeroLong(payload.get("usuario"));
        if (comentario.isBlank() || usuario == null || !usuariosRepository.existsById(usuario))
            return ResponseEntity.badRequest().body(Map.of("error", "Comentario y usuario válido son obligatorios"));
        Observaciones saved = observacionesRepository.save(Observaciones.builder().id_radicado(id).id_usuario(usuario).comentario(comentario).fecha(LocalDateTime.now()).build());
        registrar(id, usuario, "observacion", comentario);
        return ResponseEntity.ok(saved);
    }

    @PostMapping
    public Radicados create(@RequestBody Radicados radicado) {
        Radicados saved = radicadosRepository.save(radicado);
        registrar(saved.getId_radicado(), usuario(saved), "radicacion", "Radicado creado");
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Radicados radicado) {
        return radicadosRepository.findById(id).map(existing -> {
            radicado.setId_radicado(id);
            Radicados saved = radicadosRepository.save(radicado);
            registrar(id, usuario(saved), "modificacion", "Radicado actualizado");
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String,Object> payload, @RequestHeader(value="X-User-Id", required=false) Long actor) {
        if (!puedeModificar(actor)) return prohibido("cambiar estados");
        Integer nuevo = numero(payload.get("estado"));
        if (nuevo == null || nuevo <= 0) return ResponseEntity.badRequest().body(Map.of("error", "Estado inválido"));
        return radicadosRepository.findById(id).map(r -> {
            Integer anterior = r.getId_estado();
            if (!transicionPermitida(anterior, nuevo)) return ResponseEntity.badRequest().body(Map.of("error", "Transición de estado no permitida: " + anterior + " -> " + nuevo));
            r.setId_estado(nuevo);
            Radicados saved = radicadosRepository.save(r);
            registrar(id, actor != null ? actor : usuario(saved), "cambio_estado", "Estado " + anterior + " -> " + nuevo);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<?> asignar(@PathVariable Long id, @RequestBody Map<String,Object> payload, @RequestHeader(value="X-User-Id", required=false) Long actor) {
        if (!puedeModificar(actor)) return prohibido("asignar radicados");
        Integer nuevo = numero(payload.get("usuario"));
        if (nuevo == null || nuevo <= 0 || !usuariosRepository.existsById(nuevo.longValue())) return ResponseEntity.badRequest().body(Map.of("error", "Usuario responsable inexistente"));
        return radicadosRepository.findById(id).map(r -> {
            Integer anterior = r.getId_usuario();
            r.setId_usuario(nuevo);
            Radicados saved = radicadosRepository.save(r);
            registrar(id, actor != null ? actor : nuevo.longValue(), "asignacion", "Responsable " + anterior + " -> " + nuevo);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reasignar")
    public ResponseEntity<?> reasignar(@PathVariable Long id, @RequestBody Map<String,Object> payload, @RequestHeader(value="X-User-Id", required=false) Long actor) {
        if (!puedeModificar(actor)) return prohibido("reasignar radicados");
        Integer nuevo=numero(payload.get("usuarioNuevo"));
        Integer dependencia=numero(payload.get("dependenciaNueva"));
        if (nuevo==null || dependencia==null || nuevo<=0 || dependencia<=0 || !usuariosRepository.existsById(nuevo.longValue())) return ResponseEntity.badRequest().body(Map.of("error", "Usuario o dependencia inválidos"));
        return radicadosRepository.findById(id).map(r -> {
            Integer anterior=r.getId_usuario();
            if(radicadosRepository.actualizarAsignacion(id,nuevo,dependencia)==0) return ResponseEntity.internalServerError().body(Map.of("error","No se actualizó la asignación"));
            Reasignaciones re=new Reasignaciones();
            re.setId_radicado(id.intValue());
            re.setId_usuario_anterior(anterior);
            re.setId_usuario_nuevo(nuevo);
            re.setId_dependencia_nueva(dependencia);
            re.setFecha(LocalDateTime.now().toString());
            reasignacionesRepository.save(re);
            registrar(id,actor!=null?actor:nuevo.longValue(),"reasignacion","Responsable " + anterior + " -> " + nuevo);
            return ResponseEntity.ok(radicadosRepository.findById(id).orElse(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable Long id,@RequestBody(required=false) Map<String,Object> payload,@RequestHeader(value="X-User-Id",required=false) Long actor) {
        if(!puedeModificar(actor)) return prohibido("cerrar radicados");
        return radicadosRepository.findById(id).map(r->{
            Integer anterior=r.getId_estado();
            Integer estado=payload==null?null:numero(payload.get("estado"));
            if(estado==null) estado=3;
            if(!transicionPermitida(anterior,estado)) return ResponseEntity.badRequest().body(Map.of("error","El radicado no puede cerrarse desde el estado actual"));
            Long usuarioCierre=actor!=null?actor:usuario(r);
            if(usuarioCierre==null||!usuariosRepository.existsById(usuarioCierre)) return ResponseEntity.badRequest().body(Map.of("error","Usuario de cierre inválido"));
            r.setId_estado(estado);
            r.setFecha_cierre(LocalDateTime.now());
            r.setId_usuario_cierre(usuarioCierre);
            Radicados saved=radicadosRepository.save(r);
            String obs=texto(payload==null?null:payload.get("observacion"));
            registrar(id,usuarioCierre,"cierre",obs.isBlank()?"Radicado cerrado":obs);
            if(!obs.isBlank()) observacionesRepository.save(Observaciones.builder().id_radicado(id).id_usuario(usuarioCierre).comentario(obs).fecha(LocalDateTime.now()).build());
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/permisos")
    public ResponseEntity<Map<String,Object>> permisos(@PathVariable Long id,@RequestParam Long usuario){
        if(!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("usuario",usuario,"puedeModificar",puedeModificar(usuario)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        Radicados r=radicadosRepository.findById(id).orElse(null);
        Long actor=usuario(r);
        registrar(id,actor,"eliminacion","Radicado eliminado");
        radicadosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean transicionPermitida(Integer actual,Integer nuevo){
        if(actual==null) return true;
        if(actual.equals(nuevo)) return false;
        if(actual==3||actual==4) return false;
        return nuevo>=1&&nuevo<=4;
    }

    private void registrar(Long id,Long usuario,String accion,String descripcion){
        if(id==null||usuario==null||!usuariosRepository.existsById(usuario)) return;
        historialRepository.save(HistorialRadicado.builder().id_radicado(id).id_usuario(usuario).accion(accion).descripcion(descripcion).fecha(LocalDateTime.now()).build());
    }

    private boolean puedeModificar(Long id){
        if(id==null)return true;
        return usuariosRepository.findById(id).map(Usuarios::getId_rol).flatMap(rolesRepository::findById).map(r->rolPermitido(r.getRol())||rolPermitido(r.getNombre())).orElse(false);
    }

    private boolean rolPermitido(String r){if(r==null)return false;String v=r.toLowerCase();return v.contains("admin")||v.contains("coordin")||v.contains("gestor")||v.contains("oper")||v.contains("recep");}
    private Long usuario(Radicados r){return r==null||r.getId_usuario()==null?null:r.getId_usuario().longValue();}
    private Integer numero(Object v){try{return v==null?null:v instanceof Number?((Number)v).intValue():Integer.valueOf(v.toString().trim());}catch(Exception e){return null;}}
    private Long numeroLong(Object v){try{return v==null?null:v instanceof Number?((Number)v).longValue():Long.valueOf(v.toString().trim());}catch(Exception e){return null;}}
    private String texto(Object v){return v==null?"":v.toString().trim();}
    private ResponseEntity<Map<String,String>> prohibido(String a){return ResponseEntity.status(403).body(Map.of("error","El rol no tiene permiso para "+a));}
}
