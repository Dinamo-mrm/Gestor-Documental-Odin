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
    @GetMapping("/buscar") public List<Radicados> buscar(@RequestParam(required=false) String texto,@RequestParam(required=false) Integer estado,@RequestParam(required=false) Long dependencia,@RequestParam(required=false) Integer tramite) { return radicadosRepository.buscar(texto, estado, dependencia, tramite); }
    @GetMapping("/{id}") public ResponseEntity<Radicados> getById(@PathVariable Long id) { return radicadosRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @GetMapping("/{id}/historial") public ResponseEntity<List<HistorialRadicado>> historial(@PathVariable Long id) { if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build(); return ResponseEntity.ok(historialRepository.findById_radicadoOrderByFechaDesc(id)); }
    @GetMapping("/{id}/observaciones") public ResponseEntity<List<Observaciones>> observaciones(@PathVariable Long id) { if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build(); return ResponseEntity.ok(observacionesRepository.findById_radicadoOrderByFechaDesc(id)); }

    @PostMapping("/{id}/observaciones")
    public ResponseEntity<?> agregarObservacion(@PathVariable Long id,@RequestBody Map<String,Object> payload) {
        if (!radicadosRepository.existsById(id)) return ResponseEntity.notFound().build();
        String comentario = texto(payload.get("comentario"));
        if (comentario.isBlank()) return ResponseEntity.badRequest().body(Map.of("error","El comentario es obligatorio"));
        Integer usuario = numero(payload.get("usuario"));
        Observaciones item = Observaciones.builder().id_radicado(id).id_usuario(usuario).comentario(comentario).fecha(LocalDateTime.now().toString()).build();
        Observaciones saved = observacionesRepository.save(item); registrar(id, usuario, "observacion", comentario); return ResponseEntity.ok(saved);
    }

    @PostMapping public Radicados create(@RequestBody Radicados radicado) { Radicados saved=radicadosRepository.save(radicado); registrar(saved.getId_radicado(),saved.getId_usuario(),"radicado","Radicado creado"); return saved; }
    @PutMapping("/{id}") public ResponseEntity<Radicados> update(@PathVariable Long id,@RequestBody Radicados radicado) { return radicadosRepository.findById(id).map(existing->{ radicado.setId_radicado(id); Radicados saved=radicadosRepository.save(radicado); registrar(id,saved.getId_usuario(),"modificacion","Radicado actualizado"); return ResponseEntity.ok(saved); }).orElse(ResponseEntity.notFound().build()); }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,@RequestBody Map<String,Integer> payload,@RequestHeader(value="X-User-Id",required=false) Integer actor) {
        if (!puedeModificar(actor)) return ResponseEntity.status(403).body(Map.of("error","El rol no tiene permiso para cambiar estados")); Integer nuevo=payload.get("estado"); if(nuevo==null||nuevo<=0)return ResponseEntity.badRequest().body(Map.of("error","Estado inválido"));
        return radicadosRepository.findById(id).map(r->{Integer anterior=r.getId_estado();r.setId_estado(nuevo);Radicados saved=radicadosRepository.save(r);registrar(id,actor!=null?actor:r.getId_usuario(),"cambio_estado","Estado "+anterior+" → "+nuevo);return ResponseEntity.ok(saved);}).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<?> asignar(@PathVariable Long id,@RequestBody Map<String,Integer> payload,@RequestHeader(value="X-User-Id",required=false) Integer actor) {
        if(!puedeModificar(actor))return ResponseEntity.status(403).body(Map.of("error","El rol no tiene permiso para asignar")); Integer usuario=payload.get("usuario"); if(usuario==null||usuario<=0)return ResponseEntity.badRequest().body(Map.of("error","Usuario inválido"));
        return radicadosRepository.findById(id).map(r->{Integer anterior=r.getId_usuario();r.setId_usuario(usuario);Radicados saved=radicadosRepository.save(r);registrar(id,actor!=null?actor:usuario,"asignacion","Responsable "+anterior+" → "+usuario);return ResponseEntity.ok(saved);}).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reasignar")
    public ResponseEntity<?> reasignar(@PathVariable Long id,@RequestBody Map<String,Object> payload,@RequestHeader(value="X-User-Id",required=false) Integer actor) {
        if(!puedeModificar(actor))return ResponseEntity.status(403).body(Map.of("error","El rol no tiene permiso para reasignar")); Integer nuevo=numero(payload.get("usuarioNuevo")),dep=numero(payload.get("dependenciaNueva")); if(nuevo==null||nuevo<=0||dep==null||dep<=0)return ResponseEntity.badRequest().body(Map.of("error","usuarioNuevo y dependenciaNueva son obligatorios"));
        return radicadosRepository.findById(id).map(r->{Integer anterior=r.getId_usuario();int updated=radicadosRepository.actualizarAsignacion(id,nuevo,dep);if(updated==0)return ResponseEntity.internalServerError().body(Map.of("error","No se actualizó la asignación"));Reasignaciones re=new Reasignaciones();re.setId_radicado(id.intValue());re.setId_usuario_anterior(anterior);re.setId_usuario_nuevo(nuevo);re.setId_dependencia_nueva(dep);re.setFecha(LocalDateTime.now().toString());reasignacionesRepository.save(re);registrar(id,actor!=null?actor:nuevo,"reasignacion","Responsable "+anterior+" → "+nuevo);return ResponseEntity.ok(radicadosRepository.findById(id).orElse(r));}).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable Long id,@RequestBody(required=false) Map<String,Object> payload,@RequestHeader(value="X-User-Id",required=false) Integer actor) {
        if(!puedeModificar(actor))return ResponseEntity.status(403).body(Map.of("error","El rol no tiene permiso para cerrar"));
        return radicadosRepository.findById(id).map(r->{Integer estado=payload==null?null:numero(payload.get("estado"));if(estado==null||estado<=0)estado=3;r.setId_estado(estado);r.setFecha_cierre(LocalDateTime.now().toString());r.setId_usuario_cierre(actor!=null?actor:r.getId_usuario());Radicados saved=radicadosRepository.save(r);String detalle=texto(payload==null?null:payload.get("observacion"));registrar(id,r.getId_usuario_cierre(),"cierre",detalle.isBlank()?"Radicado cerrado":detalle);if(!detalle.isBlank())observacionesRepository.save(Observaciones.builder().id_radicado(id).id_usuario(r.getId_usuario_cierre()).comentario(detalle).fecha(LocalDateTime.now().toString()).build());return ResponseEntity.ok(saved);}).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/permisos") public ResponseEntity<Map<String,Object>> permisos(@PathVariable Long id,@RequestParam Integer usuario){if(!radicadosRepository.existsById(id))return ResponseEntity.notFound().build();return ResponseEntity.ok(Map.of("usuario",usuario,"puedeModificar",puedeModificar(usuario)));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(radicadosRepository.existsById(id)){radicadosRepository.deleteById(id);return ResponseEntity.noContent().build();}return ResponseEntity.notFound().build();}

    private void registrar(Long id,Integer usuario,String accion,String descripcion){historialRepository.save(HistorialRadicado.builder().id_radicado(id).id_usuario(usuario).accion(accion).descripcion(descripcion).fecha(LocalDateTime.now().toString()).build());}
    private boolean puedeModificar(Integer usuarioId){if(usuarioId==null)return true;return usuariosRepository.findById((long)usuarioId).map(Usuarios::getId_rol).flatMap(rolesRepository::findById).map(Roles::getRol).map(this::rolPermitido).orElse(false);}
    private boolean rolPermitido(String rol){if(rol==null)return false;String r=rol.toLowerCase();return r.contains("admin")||r.contains("coordin")||r.contains("gestor")||r.contains("oper")||r.contains("recep");}
    private Integer numero(Object value){if(value==null)return null;try{return value instanceof Number?((Number)value).intValue():Integer.valueOf(value.toString().trim());}catch(Exception e){return null;}}
    private String texto(Object value){return value==null?"":value.toString().trim();}
}
