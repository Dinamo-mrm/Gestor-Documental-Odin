package com.odin.odin.view;

import com.odin.odin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RadicadoDetalleView {
    @Autowired private RadicadosRepository radicadosRepository;
    @Autowired private DocumentosRepository documentosRepository;
    @Autowired private HistorialRadicadoRepository historialRepository;
    @Autowired private ObservacionesRepository observacionesRepository;

    @GetMapping("/view/radicados/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var radicado = radicadosRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Radicado no encontrado: " + id));
        model.addAttribute("radicado", radicado);
        model.addAttribute("documentos", documentosRepository.buscarPorRadicado(id));
        model.addAttribute("historial", historialRepository.findById_radicadoOrderByFechaDesc(id));
        model.addAttribute("observaciones", observacionesRepository.findById_radicadoOrderByFechaDesc(id));
        return "radicados/radicado-detalle";
    }
}
