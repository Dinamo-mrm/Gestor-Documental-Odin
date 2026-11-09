package com.odin.odin.service;

import com.odin.odin.dto.ayuda.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias del Servicio Centro de Ayuda")
class CentroAyudaServiceTest {

    private CentroAyudaService service;

    @BeforeEach
    void setUp() {
        service = new CentroAyudaService();
    }

    @Test
    @DisplayName("Debe inicializar el video general introductorio con sus temas")
    void testVideoGeneral() {
        VideoIntroductorioDto video = service.obtenerVideoGeneral();
        assertNotNull(video, "El video general no debe ser nulo");
        assertNotNull(video.getTitulo(), "El título del video general debe estar definido");
        assertFalse(video.getTemasTratados().isEmpty(), "Debe contener temas tratados");
    }

    @Test
    @DisplayName("Debe sanitizar correctamente enlaces de YouTube a formato embed seguro")
    void testSanitizarUrlsYoutube() {
        // Enlace estándar watch?v=
        String standard = CentroAyudaService.sanitizarEmbedUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        assertEquals("https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ?rel=0&modestbranding=1", standard);

        // Enlace acortado youtu.be/
        String shortUrl = CentroAyudaService.sanitizarEmbedUrl("https://youtu.be/dQw4w9WgXcQ");
        assertEquals("https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ?rel=0&modestbranding=1", shortUrl);

        // Enlace embed existente
        String embedUrl = CentroAyudaService.sanitizarEmbedUrl("https://www.youtube.com/embed/dQw4w9WgXcQ");
        assertEquals("https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ?rel=0&modestbranding=1", embedUrl);

        // ID directo de 11 caracteres
        String directId = CentroAyudaService.sanitizarEmbedUrl("dQw4w9WgXcQ");
        assertEquals("https://www.youtube-nocookie.com/embed/dQw4w9WgXcQ?rel=0&modestbranding=1", directId);

        // Nulo o vacío debe retornar null de forma segura
        assertNull(CentroAyudaService.sanitizarEmbedUrl(null));
        assertNull(CentroAyudaService.sanitizarEmbedUrl(""));
        assertNull(CentroAyudaService.sanitizarEmbedUrl("   "));
        assertNull(CentroAyudaService.sanitizarEmbedUrl("invalido"));
    }

    @Test
    @DisplayName("Debe cargar los 12 módulos funcionales reales de ODIN")
    void testTutorialesModulosReales() {
        List<TutorialModuloDto> tutoriales = service.obtenerTodosTutoriales();
        assertEquals(12, tutoriales.size(), "Debe contener exactamente los 12 módulos documentados");

        String[] slugsEsperados = {
                "dashboard", "radicacion", "tramites", "consulta",
                "series", "subseries", "documentos", "dependencias",
                "usuarios", "roles", "bitacora", "reportes"
        };

        for (String slug : slugsEsperados) {
            TutorialModuloDto tut = service.obtenerTutorialPorSlug(slug);
            assertNotNull(tut, "El módulo con slug '" + slug + "' debe existir");
            assertNotNull(tut.getTitulo(), "El título no debe ser nulo para " + slug);
            assertNotNull(tut.getObjetivo(), "El objetivo no debe ser nulo para " + slug);
            assertFalse(tut.getFuncionalidadesExplicadas().isEmpty(), "Debe explicar funcionalidades para " + slug);
            assertFalse(tut.getPasos().isEmpty(), "Debe incluir pasos detallados para " + slug);
        }
    }

    @Test
    @DisplayName("Debe retornar null al buscar un slug inexistente")
    void testSlugInexistente() {
        assertNull(service.obtenerTutorialPorSlug("modulo_inexistente_xyz"));
        assertNull(service.obtenerTutorialPorSlug(null));
    }

    @Test
    @DisplayName("Debe contener procedimientos estructurados en el manual de usuario")
    void testManualesProcedimientos() {
        List<ManualProcedimientoDto> manuales = service.obtenerTodosManuales();
        assertFalse(manuales.isEmpty(), "El manual debe contener procedimientos");

        for (ManualProcedimientoDto m : manuales) {
            assertNotNull(m.getId());
            assertNotNull(m.getCodigo());
            assertNotNull(m.getNombreProcedimiento());
            assertNotNull(m.getObjetivo());
            assertNotNull(m.getResultadoEsperado());
            assertFalse(m.getPasos().isEmpty(), "El procedimiento debe tener pasos numerados");
        }
    }

    @Test
    @DisplayName("Debe contener preguntas frecuentes organizadas con categorías válidas")
    void testPreguntasFrecuentes() {
        List<PreguntaFrecuenteDto> faqs = service.obtenerTodasPreguntas();
        assertFalse(faqs.isEmpty(), "Debe contener preguntas frecuentes");

        List<String> categorias = service.obtenerCategoriasFaq();
        assertFalse(categorias.isEmpty(), "Debe listar categorías de FAQ");

        for (PreguntaFrecuenteDto faq : faqs) {
            assertNotNull(faq.getId());
            assertNotNull(faq.getPregunta());
            assertNotNull(faq.getRespuesta());
            assertNotNull(faq.getCategoria());
            assertTrue(categorias.contains(faq.getCategoria()), "La categoría debe figurar en la lista global");
        }
    }

    @Test
    @DisplayName("Debe buscar contenido de forma insensible a mayúsculas y acentos")
    void testBusquedaUnificada() {
        // Búsqueda por término común con y sin tilde
        List<ResultadoBusquedaDto> resConTilde = service.buscar("radicación");
        List<ResultadoBusquedaDto> resSinTilde = service.buscar("radicacion");

        assertFalse(resConTilde.isEmpty(), "Debe encontrar resultados para 'radicación'");
        assertEquals(resConTilde.size(), resSinTilde.size(), "La búsqueda con y sin tilde debe coincidir");

        // Búsqueda con longitud insuficiente debe retornar lista vacía
        List<ResultadoBusquedaDto> resCorta = service.buscar("a");
        assertTrue(resCorta.isEmpty(), "Búsquedas menores a 2 caracteres deben ignorarse");

        // Búsqueda nula
        assertTrue(service.buscar(null).isEmpty());
    }
}
