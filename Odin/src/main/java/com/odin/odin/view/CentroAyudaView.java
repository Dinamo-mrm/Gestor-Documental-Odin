package com.odin.odin.view;

import com.odin.odin.dto.ayuda.*;
import com.odin.odin.service.CentroAyudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de vistas para el Centro de Ayuda del Sistema ODIN.
 * Accesible para todos los usuarios autenticados.
 */
@Controller
public class CentroAyudaView {

    @Autowired
    private CentroAyudaService centroAyudaService;

    /**
     * Portada principal del Centro de Ayuda.
     */
    @GetMapping("/view/ayuda")
    public String index(Model model) {
        List<TutorialModuloDto> tutoriales = centroAyudaService.obtenerTodosTutoriales();
        List<ManualProcedimientoDto> manuales = centroAyudaService.obtenerTodosManuales();
        List<PreguntaFrecuenteDto> faqs = centroAyudaService.obtenerTodasPreguntas();

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("videoGeneral", centroAyudaService.obtenerVideoGeneral());
        model.addAttribute("tutoriales", tutoriales);
        model.addAttribute("manuales", manuales);
        model.addAttribute("faqs", faqs);
        model.addAttribute("categoriasFaq", centroAyudaService.obtenerCategoriasFaq());
        model.addAttribute("totalTutoriales", tutoriales.size());
        model.addAttribute("totalManuales", manuales.size());
        model.addAttribute("totalFaqs", faqs.size());

        return "ayuda/index";
    }

    /**
     * Catálogo completo de tutoriales organizados por módulos funcionales reales.
     */
    @GetMapping("/view/ayuda/tutoriales")
    public String tutoriales(Model model) {
        List<TutorialModuloDto> tutoriales = centroAyudaService.obtenerTodosTutoriales();

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("tutoriales", tutoriales);
        model.addAttribute("totalTutoriales", tutoriales.size());

        return "ayuda/tutoriales";
    }

    /**
     * Vista de detalle de un tutorial por módulo funcional.
     */
    @GetMapping("/view/ayuda/tutoriales/{slug}")
    public String tutorialDetalle(@PathVariable String slug, Model model) {
        TutorialModuloDto tutorial = centroAyudaService.obtenerTutorialPorSlug(slug);
        if (tutorial == null) {
            return "redirect:/view/ayuda/tutoriales";
        }

        List<TutorialModuloDto> otros = centroAyudaService.obtenerTodosTutoriales().stream()
                .filter(t -> !t.getSlug().equalsIgnoreCase(slug))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("tutorial", tutorial);
        model.addAttribute("otrosTutoriales", otros);

        return "ayuda/tutorial-detalle";
    }

    /**
     * Manual de usuario estructurado por procedimientos estándar.
     */
    @GetMapping("/view/ayuda/manual")
    public String manual(Model model) {
        List<ManualProcedimientoDto> manuales = centroAyudaService.obtenerTodosManuales();

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("manuales", manuales);
        model.addAttribute("totalManuales", manuales.size());

        return "ayuda/manual";
    }

    /**
     * Sección de Preguntas Frecuentes con acordeón interactivo.
     */
    @GetMapping("/view/ayuda/faq")
    public String faq(Model model) {
        List<PreguntaFrecuenteDto> faqs = centroAyudaService.obtenerTodasPreguntas();
        List<String> categorias = centroAyudaService.obtenerCategoriasFaq();

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("faqs", faqs);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalFaqs", faqs.size());

        return "ayuda/faq";
    }

    /**
     * Vista de búsqueda global dentro del Centro de Ayuda.
     */
    @GetMapping("/view/ayuda/buscar")
    public String buscar(@RequestParam(required = false, defaultValue = "") String q, Model model) {
        List<ResultadoBusquedaDto> resultados = centroAyudaService.buscar(q);

        model.addAttribute("activeMenu", "ayuda");
        model.addAttribute("query", q);
        model.addAttribute("resultados", resultados);
        model.addAttribute("totalResultados", resultados.size());

        return "ayuda/buscar";
    }

    /**
     * API REST asíncrona para el buscador interactivo instantáneo.
     */
    @GetMapping("/api/ayuda/buscar")
    @ResponseBody
    public List<ResultadoBusquedaDto> buscarApi(@RequestParam(required = false, defaultValue = "") String q) {
        return centroAyudaService.buscar(q);
    }
}
