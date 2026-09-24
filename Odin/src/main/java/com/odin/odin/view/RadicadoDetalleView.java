package com.odin.odin.view;

import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.HistorialRadicadoRepository;
import com.odin.odin.repository.ObservacionesRepository;
import com.odin.odin.repository.RadicadosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RadicadoDetalleView {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private DocumentosRepository documentosRepository;

    @Autowired
    private HistorialRadicadoRepository historialRepository;

    @Autowired
    private ObservacionesRepository observacionesRepository;


    @GetMapping("/view/radicados/detalle/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        var radicadoOpt = radicadosRepository.findById(id);

        if (radicadoOpt.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "El radicado solicitado no existe."
            );

            return "redirect:/view/tramites";
        }

        var radicado = radicadoOpt.get();

        model.addAttribute(
                "radicado",
                radicado
        );

        model.addAttribute(
                "documentos",
                documentosRepository.buscarPorRadicado(id)
        );

        model.addAttribute(
                "historial",
                historialRepository.findByRadicadoOrderByFechaDesc(id)
        );

        model.addAttribute(
                "observaciones",
                observacionesRepository.findByRadicadoOrderByFechaDesc(id)
        );

        return "radicados/radicado-detalle";
    }
}