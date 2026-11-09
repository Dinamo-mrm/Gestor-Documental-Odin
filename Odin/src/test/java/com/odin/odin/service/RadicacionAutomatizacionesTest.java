package com.odin.odin.service;

import com.odin.odin.controller.DependenciasController;
import com.odin.odin.controller.RadicadosController;
import com.odin.odin.model.Dependencias;
import com.odin.odin.model.Radicados;
import com.odin.odin.model.Tramites;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RadicacionAutomatizacionesTest {

    @BeforeAll
    static void initEnv() {
        loadDotEnv();
    }

    private static void loadDotEnv() {
        Path[] candidates = new Path[] {
                Paths.get(".env"),
                Paths.get("Odin", ".env"),
                Paths.get("..", "Odin", ".env")
        };
        for (Path p : candidates) {
            if (Files.isRegularFile(p)) {
                try (BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq <= 0) continue;
                        String key = line.substring(0, eq).trim();
                        String value = line.substring(eq + 1).trim();
                        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        if (System.getenv(key) == null && System.getProperty(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                    break;
                } catch (Exception ignored) {}
            }
        }
    }

    @Autowired
    private ClasificacionDocumentalService clasificacionService;

    @Autowired
    private DependenciasController dependenciasController;

    @Autowired
    private RadicadosController radicadosController;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    // =========================================================================
    // AUTOMATIZACIÓN 1: RESPONSABLE AUTOMÁTICO SEGÚN LA DEPENDENCIA
    // =========================================================================

    @Test
    @DisplayName("Auto 1.1 - Debe obtener el responsable real activo cuando se consulta una dependencia existente")
    void testResponsableSegunDependenciaExistente() {
        List<Dependencias> dependencias = dependenciasRepository.findAll();
        assertFalse(dependencias.isEmpty(), "Deben existir dependencias en la base de datos");

        Dependencias dep1 = dependencias.get(0);
        ResponseEntity<?> resp = dependenciasController.getResponsable(dep1.getId_dependencia());
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode().value());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertNotNull(data);
        assertNotNull(data.get("id_usuario"), "Debe tener id_usuario del responsable");
        assertEquals(dep1.getId_dependencia(), ((Number) data.get("id_dependencia")).longValue());
    }

    @Test
    @DisplayName("Auto 1.2 - Al cambiar de dependencia, el responsable debe cambiar al funcionario correspondiente")
    void testCambioDeDependenciaActualizaResponsable() {
        List<Dependencias> dependencias = dependenciasRepository.findAll();
        if (dependencias.size() >= 2) {
            Dependencias depA = dependencias.get(0);
            Dependencias depB = dependencias.get(1);

            ResponseEntity<?> respA = dependenciasController.getResponsable(depA.getId_dependencia());
            ResponseEntity<?> respB = dependenciasController.getResponsable(depB.getId_dependencia());

            @SuppressWarnings("unchecked")
            Map<String, Object> dataA = (Map<String, Object>) ((Map<String, Object>) respA.getBody()).get("data");
            @SuppressWarnings("unchecked")
            Map<String, Object> dataB = (Map<String, Object>) ((Map<String, Object>) respB.getBody()).get("data");

            assertNotEquals(dataA.get("id_dependencia"), dataB.get("id_dependencia"));
        }
    }

    @Test
    @DisplayName("Auto 1.3 - Dependencia sin responsable o inexistente debe devolver 404 y no asignar usuario erróneo")
    void testDependenciaSinResponsableRetorna404() {
        Long idInexistente = 999999L;
        ResponseEntity<?> resp = dependenciasController.getResponsable(idInexistente);
        assertNotNull(resp);
        assertEquals(404, resp.getStatusCode().value());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertTrue(((String) body.get("message")).contains("No se encontró ningún responsable activo"));
    }

    @Test
    @DisplayName("Auto 1.4 - Radicado creado en backend debe asociar el responsable de la dependencia seleccionada")
    void testRadicadoAsociaResponsableDeDependencia() {
        List<Dependencias> dependencias = dependenciasRepository.findAll();
        assertFalse(dependencias.isEmpty());
        Dependencias dep = dependencias.get(0);

        List<Usuarios> activos = usuariosRepository.findActivosByDependencia(dep.getId_dependencia());
        assertFalse(activos.isEmpty(), "La dependencia debe tener usuarios activos");

        Radicados rad = Radicados.builder()
                .asunto("Prueba de automatización de responsable " + System.currentTimeMillis())
                .remitente("Ciudadano Prueba")
                .tipoRadicacion("ENTRADA")
                .id_tramite(5L) // Petición
                .id_dependencia(dep.getId_dependencia())
                .id_usuario(null) // Omitido a propósito para validar autollenado
                .build();

        Radicados guardado = radicadosController.create(rad);
        assertNotNull(guardado);
        assertNotNull(guardado.getId_radicado());
        assertEquals(activos.get(0).getId_usuario(), guardado.getId_usuario(),
                "El radicado debe asignarse automáticamente al responsable de la dependencia");

        // Limpieza de prueba
        jdbcTemplate.update("DELETE FROM auditoria_radicados WHERE id_radicado = ?", guardado.getId_radicado());
        radicadosRepository.deleteById(guardado.getId_radicado());
    }

    // =========================================================================
    // AUTOMATIZACIÓN 2: SERIE Y SUBSERIE DOCUMENTAL AUTOMÁTICAS (CCD/TRD)
    // =========================================================================

    @Test
    @DisplayName("Auto 2.1 - Trámite Petición / Tipo PETICION determina Serie 1.1012-27 y Subserie 1.1012-27-01")
    void testClasificacionPeticion() {
        var res = clasificacionService.determinarClasificacion(5L, "PETICION");
        assertTrue(res.isDeterminada());
        assertEquals("1.1012-27", res.getCodigoSerie());
        assertEquals("1.1012-27-01", res.getCodigoSubserie());
        assertTrue(res.getTextoSerie().contains("DERECHOS DE PETICIÓN"));
        assertTrue(res.getTextoSubserie().contains("Peticiones Generales"));
    }

    @Test
    @DisplayName("Auto 2.2 - Trámite Queja / Reclamo / Sugerencia determina Serie 1.1012-27 y Subserie 1.1012-27-02")
    void testClasificacionQueja() {
        // Queja (ID 24) o Tipo Documento QUEJA
        var res = clasificacionService.determinarClasificacion(24L, "QUEJA");
        assertTrue(res.isDeterminada());
        assertEquals("1.1012-27", res.getCodigoSerie());
        assertEquals("1.1012-27-02", res.getCodigoSubserie());
        assertTrue(res.getTextoSubserie().contains("Quejas y Reclamos"));
    }

    @Test
    @DisplayName("Auto 2.3 - Trámite Tutela determina Subserie Tutelas 1.1012-27-03")
    void testClasificacionTutela() {
        var res = clasificacionService.determinarClasificacion(17L, "OFICIO");
        assertTrue(res.isDeterminada());
        assertEquals("1.1012-27", res.getCodigoSerie());
        assertEquals("1.1012-27-03", res.getCodigoSubserie());
        assertTrue(res.getTextoSubserie().contains("Tutelas"));
    }

    @Test
    @DisplayName("Auto 2.4 - Trámite Información y Copias determina Subserie 1.1013-27-01")
    void testClasificacionInformacionYCopias() {
        var res = clasificacionService.determinarClasificacion(18L, "PETICION");
        assertTrue(res.isDeterminada());
        assertEquals("1.0010-27", res.getCodigoSerie());
        assertEquals("1.1013-27-01", res.getCodigoSubserie());
        assertTrue(res.getTextoSubserie().contains("Peticiones de Información"));
    }

    @Test
    @DisplayName("Auto 2.5 - Tipo Documento OFICIO o MEMORANDO determina Serie 1.1010-42 (Informes / Comunicaciones)")
    void testClasificacionOficioMemorando() {
        var res = clasificacionService.determinarClasificacion(2L, "OFICIO");
        assertTrue(res.isDeterminada());
        assertEquals("1.1010-42", res.getCodigoSerie());
        assertNull(res.getCodigoSubserie());
        assertTrue(res.getTextoSerie().contains("INFORMES"));
    }

    @Test
    @DisplayName("Auto 2.6 - Endpoint REST /api/radicados/clasificacion-automatica responde correctamente")
    void testEndpointClasificacionAutomatica() {
        ResponseEntity<?> resp = radicadosController.clasificacionAutomatica(5L, "PETICION");
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode().value());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));

        ClasificacionDocumentalService.ResultadoClasificacion data =
                (ClasificacionDocumentalService.ResultadoClasificacion) body.get("data");
        assertNotNull(data);
        assertEquals("1.1012-27", data.getCodigoSerie());
        assertEquals("1.1012-27-01", data.getCodigoSubserie());
    }

    @Test
    @DisplayName("Auto 2.7 - Backend valida y sobreescribe intentos de manipulación de Serie y Subserie")
    void testBackendProteccionContraClasificacionManipulada() {
        // Enviar trámite Petición pero con series y subseries manipuladas manualmente
        Radicados rad = Radicados.builder()
                .asunto("Prueba de intento de manipulación clasificación " + System.currentTimeMillis())
                .remitente("Atacante Prueba")
                .tipoRadicacion("ENTRADA")
                .id_tramite(5L) // Petición (debe ser 1.1012-27 y 1.1012-27-01)
                .tipoDocumento("PETICION")
                .codigo_serie("SERIE_FALSA_999") // Intento de manipulación
                .codigo_subserie("SUBSERIE_FALSA_999")
                .id_dependencia(2L)
                .build();

        Radicados guardado = radicadosController.create(rad);
        assertNotNull(guardado);
        assertNotNull(guardado.getId_radicado());

        // Debe haber sido corregido y protegido por el backend
        assertEquals("1.1012-27", guardado.getCodigo_serie(), "El backend debe garantizar la serie correspondiente");
        assertEquals("1.1012-27-01", guardado.getCodigo_subserie(), "El backend debe garantizar la subserie correspondiente");

        // Limpieza de prueba
        jdbcTemplate.update("DELETE FROM auditoria_radicados WHERE id_radicado = ?", guardado.getId_radicado());
        radicadosRepository.deleteById(guardado.getId_radicado());
    }
}
