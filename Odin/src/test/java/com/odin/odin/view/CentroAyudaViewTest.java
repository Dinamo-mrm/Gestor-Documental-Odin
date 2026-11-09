package com.odin.odin.view;

import com.odin.odin.dto.ayuda.*;
import com.odin.odin.service.CentroAyudaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias del Controlador CentroAyudaView")
class CentroAyudaViewTest {

    private CentroAyudaView view;
    private CentroAyudaService service;

    @BeforeEach
    void setUp() throws Exception {
        view = new CentroAyudaView();
        service = new CentroAyudaService();

        // Inyección del servicio en el controlador
        Field field = CentroAyudaView.class.getDeclaredField("centroAyudaService");
        field.setAccessible(true);
        field.set(view, service);
    }

    @Test
    @DisplayName("index() debe retornar la vista 'ayuda/index' y poblar el modelo con estadísticas y contenidos")
    void testIndex() {
        Model model = new ConcurrentModel();
        String viewName = view.index(model);

        assertEquals("ayuda/index", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        assertNotNull(model.getAttribute("videoGeneral"));
        assertNotNull(model.getAttribute("tutoriales"));
        assertNotNull(model.getAttribute("manuales"));
        assertNotNull(model.getAttribute("faqs"));
        assertNotNull(model.getAttribute("categoriasFaq"));
        assertTrue((int) model.getAttribute("totalTutoriales") > 0);
    }

    @Test
    @DisplayName("tutoriales() debe retornar 'ayuda/tutoriales' y listar todos los módulos")
    void testTutoriales() {
        Model model = new ConcurrentModel();
        String viewName = view.tutoriales(model);

        assertEquals("ayuda/tutoriales", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        @SuppressWarnings("unchecked")
        List<TutorialModuloDto> list = (List<TutorialModuloDto>) model.getAttribute("tutoriales");
        assertNotNull(list);
        assertEquals(12, list.size());
    }

    @Test
    @DisplayName("tutorialDetalle() con slug válido debe retornar 'ayuda/tutorial-detalle'")
    void testTutorialDetalleValido() {
        Model model = new ConcurrentModel();
        String viewName = view.tutorialDetalle("radicacion", model);

        assertEquals("ayuda/tutorial-detalle", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        TutorialModuloDto tutorial = (TutorialModuloDto) model.getAttribute("tutorial");
        assertNotNull(tutorial);
        assertEquals("radicacion", tutorial.getSlug());
        assertNotNull(model.getAttribute("otrosTutoriales"));
    }

    @Test
    @DisplayName("tutorialDetalle() con slug inválido debe redirigir a '/view/ayuda/tutoriales'")
    void testTutorialDetalleInvalido() {
        Model model = new ConcurrentModel();
        String viewName = view.tutorialDetalle("modulo-inexistente", model);

        assertEquals("redirect:/view/ayuda/tutoriales", viewName);
    }

    @Test
    @DisplayName("manual() debe retornar 'ayuda/manual' con los procedimientos cargados")
    void testManual() {
        Model model = new ConcurrentModel();
        String viewName = view.manual(model);

        assertEquals("ayuda/manual", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        assertNotNull(model.getAttribute("manuales"));
        assertTrue((int) model.getAttribute("totalManuales") > 0);
    }

    @Test
    @DisplayName("faq() debe retornar 'ayuda/faq' con las preguntas y categorías")
    void testFaq() {
        Model model = new ConcurrentModel();
        String viewName = view.faq(model);

        assertEquals("ayuda/faq", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        assertNotNull(model.getAttribute("faqs"));
        assertNotNull(model.getAttribute("categorias"));
    }

    @Test
    @DisplayName("buscar() vista debe retornar 'ayuda/buscar' con los resultados y término")
    void testBuscarVista() {
        Model model = new ConcurrentModel();
        String viewName = view.buscar("radicado", model);

        assertEquals("ayuda/buscar", viewName);
        assertEquals("ayuda", model.getAttribute("activeMenu"));
        assertEquals("radicado", model.getAttribute("query"));
        assertNotNull(model.getAttribute("resultados"));
        assertTrue((int) model.getAttribute("totalResultados") > 0);
    }

    @Test
    @DisplayName("buscarApi() REST debe responder con la lista de resultados filtrada")
    void testBuscarApi() {
        List<ResultadoBusquedaDto> resultados = view.buscarApi("tramite");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        assertTrue(resultados.stream().anyMatch(r -> r.getTitulo().toLowerCase().contains("trámite") || r.getSnippet().toLowerCase().contains("trámite") || r.getModulo().toLowerCase().contains("trámite")));
    }
}
